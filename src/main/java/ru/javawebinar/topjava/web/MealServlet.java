package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    private LocalDate minDate;
    private LocalDate maxDate;
    private LocalTime minTime;
    private LocalTime maxTime;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")), null);

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);

        if (meal.isNew()) mealRestController.create(meal);
        else mealRestController.update(meal);

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, null) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":

                if (request.getParameter("minD").equals("")) minDate = LocalDate.MIN;
                else minDate = LocalDate.parse(request.getParameter("minD"));

                if (request.getParameter("maxD").equals("")) maxDate = LocalDate.MAX;
                else maxDate = LocalDate.parse(request.getParameter("maxD"));

                if (request.getParameter("minT").equals("")) minTime = LocalTime.MIN;
                else minTime = LocalTime.parse(request.getParameter("minT"));

                if (request.getParameter("maxT").equals("")) maxTime = LocalTime.MAX;
                else maxTime = LocalTime.parse(request.getParameter("maxT"));

                log.info("getAll Filtered");

                request.setAttribute("meals", mealRestController.getAll(minDate,maxDate,minTime,maxTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;

            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAll(LocalDate.MIN,LocalDate.MAX,LocalTime.MIN, LocalTime.MAX));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

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

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);


    private MealRestController mealRestController;

    //private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
        //repository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")), authUserId());

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealRestController.create(meal);
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
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, authUserId()) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                LocalDate minDate = LocalDate.parse(request.getParameter("minD"));
                LocalDate maxDate = LocalDate.parse(request.getParameter("maxD"));
                LocalTime minTime = LocalTime.parse(request.getParameter("minT"));
                LocalTime maxTime = LocalTime.parse(request.getParameter("maxT"));

                request.setAttribute("minDate", minDate);
                request.setAttribute("maxDate", maxDate);
                request.setAttribute("minTime", minTime);
                request.setAttribute("maxTime", maxTime);


                log.info("getAll Filtered");

                List<MealTo> list = MealsUtil.getWithExcess(mealRestController.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);

                List<MealTo> listDT = list.stream()
                        .filter(mealTo -> mealTo.getDateTime().toLocalDate().compareTo(minDate) >= 0)
                        .filter(mealTo -> mealTo.getDateTime().toLocalDate().compareTo(maxDate) <= 0)
                        .filter(mealTo -> mealTo.getDateTime().toLocalTime().compareTo(minTime) >= 0)
                        .filter(mealTo -> mealTo.getDateTime().toLocalTime().compareTo(maxTime) <= 0)
                        .collect(Collectors.toList());

                request.setAttribute("meals", listDT);


                request.getRequestDispatcher("/meals.jsp").forward(request, response);


                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", MealsUtil.getWithExcess(mealRestController.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));

                request.setAttribute("minDate", mealRestController.minDate());
                request.setAttribute("maxDate", mealRestController.maxDate());
                request.setAttribute("minTime", mealRestController.minTime());
                request.setAttribute("maxTime", mealRestController.maxTime());

                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

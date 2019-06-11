package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class MealEditServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to meals");
        String action = req.getParameter("action");
        if (action.equalsIgnoreCase("save")) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("DataTime"), formatter);

            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));

            Meal meal = new Meal(dateTime, description, calories);

            int id = Integer.parseInt(req.getParameter("Id"));

            MealService.service.updateMeal(id, meal);

            resp.sendRedirect("meals.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to edit");

        String action = req.getParameter("action");
        if (action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(req.getParameter("mealId"));

            Meal meal = MealService.service.getMealById(mealId);
            System.out.println(mealId);

            req.setAttribute("Id", mealId);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("/edit.jsp").forward(req, resp);
        }



    }
}


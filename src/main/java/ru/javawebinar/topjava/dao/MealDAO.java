package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealDAO implements DAO {
    private static int id = 0;
    private static List<Meal> meals = Arrays.asList(
                new Meal( LocalDateTime.of(2015, Month.MAY, 28, 9, 0), "Завтрак", 500),
                new Meal( LocalDateTime.of(2015, Month.MAY, 28, 12, 0), "Обед", 1000),
                new Meal( LocalDateTime.of(2015, Month.MAY, 28, 20, 0), "Ужин", 500),
                new Meal( LocalDateTime.of(2015, Month.MAY, 29, 10, 10), "Завтрак", 1000),
                new Meal( LocalDateTime.of(2015, Month.MAY, 29, 13, 40), "Обед", 500),
                new Meal( LocalDateTime.of(2015, Month.MAY, 29, 19, 0), "Ужин", 510),
                new Meal( LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal( LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal( LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal( LocalDateTime.of(2015, Month.MAY, 31, 10, 20), "Завтрак", 1000),
                new Meal( LocalDateTime.of(2015, Month.MAY, 31, 14, 0), "Обед", 500),
                new Meal( LocalDateTime.of(2015, Month.MAY, 31, 20, 30), "Ужин", 510)
        );

    @Override
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    @Override
    public void deleteMeal(int id) {
        meals.remove(id);
    }

    @Override
    public void updateMeal(int id, Meal meal) {
        meals.set(id, meal);    //ERROR
    }

    @Override
    public List<Meal> getAllMeal() {
        return meals;
    }

    @Override
    public Meal getMealById(int id) {
        return meals.get(id);
    }

    @Override
     public int getId(Meal meal){
        return meals.indexOf(meal);
     }



}

package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.DAO;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;


public class MealService {

    public static MealService service = new MealService();

    DAO mealDAO;

    public MealService() {
        mealDAO = new MealDAO();
    }

    public void addMeal(Meal meal) {
        mealDAO.addMeal(meal);
    }

    public void deleteMeal(int id) {
        mealDAO.deleteMeal(id);
    }

    public void updateMeal(int id, Meal newMeal) {
        mealDAO.updateMeal(id, newMeal);
    }

    public List<Meal> getAllMeal() {
        return mealDAO.getAllMeal();
    }

    public Meal getMealById(int id) {
        return mealDAO.getMealById(id);
    }

    public int getId(Meal meal) {
        return mealDAO.getId(meal);
    }
}

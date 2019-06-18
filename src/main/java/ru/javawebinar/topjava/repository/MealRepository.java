package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int loggedUserId);

    // false if not found
    boolean delete(int id, int loggedUserId);

    // null if not found
    Meal get(int id, int loggedUserId);

    List<Meal> getAll(int loggedUserId);
}

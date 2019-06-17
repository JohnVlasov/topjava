package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.Collection;

public interface MealService {

    Meal create(Meal meal);

    void delete(int id,int loggedUserId) throws NotFoundException;

    Meal get(int id,int loggedUserId) throws NotFoundException;

    void update(Meal meal, int loggedUserId);

    Collection<Meal> getAll(int loggedUserId);

}
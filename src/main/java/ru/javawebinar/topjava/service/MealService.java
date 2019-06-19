package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    Meal create(Meal meal, int loggedUserId) throws NotFoundException;

    void delete(int id, int loggedUserId) throws NotFoundException;

    Meal get(int id, int loggedUserId) throws NotFoundException;

    void update(Meal meal, int loggedUserId) throws NotFoundException;

    List<Meal> getAll(LocalDate minDate, LocalDate maxDate, int loggedUserId) throws NotFoundException;

}
package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    private LocalDate minDate, maxDate;
    private LocalTime minTime, maxTime;


    @Override
    public Meal save(Meal meal, int loggedUserId) {
        if (!repository.containsKey(loggedUserId)) repository.put(loggedUserId, new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(loggedUserId);
            repository.get(loggedUserId).put(meal.getId(), meal);
            return meal;
        } else {        // treat case: update, but absent in storage
            meal.setUserId(loggedUserId);
            return repository.get(loggedUserId).computeIfPresent(meal.getId(), (id, oldMeal) ->
                    (oldMeal.getUserId() == loggedUserId) ? meal : oldMeal
            );
        }
    }

    @Override
    public boolean delete(int id, int loggedUserId) {
        if (!repository.containsKey(loggedUserId)) return false;
        return repository.get(loggedUserId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int loggedUserId) {
        if (!repository.containsKey(loggedUserId)) return null;
        return repository.get(loggedUserId).get(id);
    }

    @Override
    public List<Meal> getAll(LocalDate minDate, LocalDate maxDate, int loggedUserId) {
        if (!repository.containsKey(loggedUserId)) return new ArrayList<Meal>();
/*
        return repository.get(loggedUserId).values()
                .stream()
                //.filter(meal -> meal.getUserId() == loggedUserId)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
  */

        return repository.get(loggedUserId).values().stream()
                .filter(mealTo -> mealTo.getDateTime().toLocalDate().compareTo(minDate) >= 0 && mealTo.getDateTime().toLocalDate().compareTo(maxDate) <= 0)
                //.filter(mealTo -> mealTo.getDateTime().toLocalTime().compareTo(minTime) >= 0 && mealTo.getDateTime().toLocalTime().compareTo(maxTime) <= 0)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }

}


package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int loggedUserId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(loggedUserId);
            repository.put(meal.getId(), meal);
            return meal;
        } else {        // treat case: update, but absent in storage
            meal.setUserId(loggedUserId);
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) ->
            (oldMeal.getUserId() == loggedUserId) ? meal : oldMeal
        );}
    }

    @Override
    public boolean delete(int id, int loggedUserId) {
        if (repository.get(id).getUserId() == loggedUserId) return repository.remove(id) != null;
        else return false;
    }

    @Override
    public Meal get(int id, int loggedUserId) {
        if (repository.get(id).getUserId() == loggedUserId) return repository.get(id);
        else return null;
    }

    @Override
    public List<Meal> getAll(int loggedUserId) {

        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == loggedUserId)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }

}


package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATETIME = new Sort(Sort.Direction.DESC, "dateTime");
    @Autowired
    private CrudMealRepository crudRepository;
    @Autowired
    private UserService userService;

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userService.get(userId));
        if (meal.isNew()) {
            return crudRepository.save(meal);
        } else if (get(meal.getId(), userId) == null) {
            return null;
        } else return crudRepository.save(meal);
    }


    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findById(id, userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(userId, SORT_DATETIME);

    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findAll(startDate, endDate, userId, SORT_DATETIME);
    }
}

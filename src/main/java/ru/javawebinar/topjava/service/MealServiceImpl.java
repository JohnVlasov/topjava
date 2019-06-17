package ru.javawebinar.topjava.service;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id, int loggedUserId) throws NotFoundException {
        if (repository.get(id).getUserId() == loggedUserId) checkNotFoundWithId(repository.delete(id), id);
        else throw new NotFoundException("Meal id=" + id + " for User id=" + authUserId() + " not found");
    }

    @Override
    public Meal get(int id, int loggedUserId) throws NotFoundException {
        if (repository.get(id).getUserId() == loggedUserId) return checkNotFoundWithId(repository.get(id), id);
        else throw new NotFoundException("Meal id=" + id + " for User id=" + authUserId() + " not found");
    }

    @Override
    public void update(Meal meal, int loggedUserId) throws NotFoundException {
        if (meal.getUserId() == loggedUserId) checkNotFoundWithId(repository.save(meal), meal.getId());
        else throw new NotFoundException("Meal id=" + meal.getId() + " for User id=" + authUserId() + " not found");
    }

    @Override
    public Collection<Meal> getAll(int loggedUserId) throws NotFoundException {
        return checkNotFoundWithId(repository.getByUserId(loggedUserId), loggedUserId);
    }

}
package ru.javawebinar.topjava.service;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;


@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int loggedUserId) throws NotFoundException{
        return repository.save(meal, loggedUserId);
    }

    @Override
    public void delete(int id, int loggedUserId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id,loggedUserId), id);
    }

    @Override
    public Meal get(int id, int loggedUserId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id,loggedUserId), id);
    }

    @Override
    public void update(Meal meal, int loggedUserId) throws NotFoundException {
       checkNotFoundWithId(repository.save(meal,loggedUserId), meal.getId());
    }

    @Override
    public List<Meal> getAll(int loggedUserId) throws NotFoundException {
        return checkNotFoundWithId(repository.getAll(loggedUserId), loggedUserId);
    }

}
package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController {





    @Override
    public List<Meal> getAll() {
        return super.getAll();
    }

    @Override
    public Meal get(int id) {
        return super.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        return super.create(meal);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public void update(Meal meal) {
        super.update(meal);
    }

    public LocalDate minDate(){
        return getAll().stream().min((m1,m2)-> m1.getDate().compareTo(m2.getDate())).get().getDate();
    }

    public LocalDate maxDate(){
        return getAll().stream().max((m1,m2)-> m1.getDate().compareTo(m2.getDate())).get().getDate();
    }

    public LocalTime minTime(){
        return getAll().stream().min((m1,m2)-> m1.getTime().compareTo(m2.getTime())).get().getTime();
    }

    public LocalTime maxTime(){
        return getAll().stream().max((m1,m2)-> m1.getTime().compareTo(m2.getTime())).get().getTime();
    }

    public List<Meal> getAallByFilter(LocalDate minDate, LocalDate maxDate, LocalTime minTime, LocalTime maxTime){

        return  super.getAll()  ;
    }


}
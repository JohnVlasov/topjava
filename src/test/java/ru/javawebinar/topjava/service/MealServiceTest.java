package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal testMeal = service.get(meal4.getId(), ADMIN_ID);
        assertMatch(testMeal, meal4);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(meal1.getId(), ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = meal1;
        updated.setCalories(999);
        updated.setDescription("Спортивный завтрак User");
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(meal1, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(meal1.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), meal2, meal3);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(meal1.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Meal> mealList = service.getBetweenDates(LocalDate.parse("2019-01-08",formatter), LocalDate.parse("2019-01-11",formatter), ADMIN_ID);
        assertMatch(mealList, meal4, meal5, meal6, meal7, meal8);
    }

    @Test
    public void getBetweenDateTimes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Meal> mealList = service.getBetweenDateTimes(LocalDateTime.parse("2019-01-08 08:00:00",formatter), LocalDateTime.parse("2019-01-11 23:00:00",formatter), ADMIN_ID);
        assertMatch(mealList, meal4, meal5, meal6, meal7, meal8);
    }

    @Test
    public void getAll() {
        List<Meal> mealList = service.getAll(ADMIN_ID);
        assertMatch(mealList, meal4, meal5, meal6, meal7, meal8, meal9, meal10, meal11);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "Перекус Admin", 500);
        Meal resultMeal = service.get(service.create(newMeal, ADMIN_ID).getId(), ADMIN_ID);

        assertMatch(resultMeal, newMeal);
    }
}
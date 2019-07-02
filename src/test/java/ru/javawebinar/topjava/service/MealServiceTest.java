package ru.javawebinar.topjava.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.*;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.TimeUnit;


import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Rule
    public TestRule timeout = new Timeout(10000);

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String testName = description.getMethodName();
            System.out.println("Test" + testName + " finished, spent " + TimeUnit.NANOSECONDS.toMillis(nanos) + " milliseconds");
        }
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Transactional
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test
    @Transactional
    public void deleteNotOwn() {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    @Transactional
    public void create() {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    @Transactional
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    @Transactional
    public void getNotFound() {
        thrown.expect(NoResultException.class);
        service.get(1, USER_ID);
    }

    @Test
    @Transactional
    public void getNotOwn() {
        thrown.expect(NoResultException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    @Transactional
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    @Transactional
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @Test
    @Transactional
    public void update() {
        Meal updated = getUpdated();

        service.update(updated, USER_ID);
        Meal meal = service.get(MEAL1_ID, USER_ID);

        assertMatch(meal, updated);
    }

    @Test
    @Transactional
    public void updateNotFound() {
        thrown.expect(Exception.class);
        service.update(MEAL1, ADMIN_ID);
    }


    @Test
    @Transactional
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

}
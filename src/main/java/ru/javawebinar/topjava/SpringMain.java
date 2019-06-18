package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static ru.javawebinar.topjava.model.Role.ROLE_ADMIN;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "John", "email@mail.ru", "password", Role.ROLE_ADMIN));
            adminUserController.create(new User(null, "Vasya", "vasya@mail.ru", "qwerty", ROLE_USER));
            adminUserController.create(new User(null, "Petya", "petya@mail.ru", "12345", ROLE_USER));
            adminUserController.create(new User(null, "John", "john@mail.ru", "12345", ROLE_ADMIN, ROLE_USER));
            adminUserController.getAll().forEach(System.out::println);

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll().forEach(System.out::println);
            //mealRestController.delete(4);
            //mealRestController.getAll().forEach(System.out::println);
            mealRestController.create(new Meal(LocalDateTime.of(2019, Month.JUNE, 17, 20, 16), "Вкусный ужин", 777, null));
            mealRestController.getAll().forEach(System.out::println);
            mealRestController.update(new Meal(7, LocalDateTime.of(2020, Month.JUNE, 17, 20, 16), "Вкусный ужин!!!", 777, null));
            mealRestController.getAll().forEach(System.out::println);


        }
    }
}

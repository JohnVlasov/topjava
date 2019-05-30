package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceeded_1(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
//        .toLocalDate();
//        .toLocalTime();
    }

    //вариант через циклы
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> summCaloriesPerDay = new HashMap<>(); //карта всех возможных дат и сумм каллорий по датам.

        mealList.forEach((userMeal) -> summCaloriesPerDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), (v, nv) -> v + nv));

        List<UserMealWithExceed> listUserMealWithExceed = new ArrayList<>(); // список еды который нужно вернуть

        mealList.forEach((u) -> { // добавляем в список UserMealWithExceed между startTime и endTime
            if (TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime)) {
                listUserMealWithExceed.add(new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), (summCaloriesPerDay.get(u.getDateTime().toLocalDate()) > caloriesPerDay)));
            }
        });

        return listUserMealWithExceed;
    }

    // вариант через stream()
    public static List<UserMealWithExceed> getFilteredWithExceeded_1(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> summCaloriesPerDay = mealList.stream().collect(Collectors.toMap((u) -> u.getDateTime().toLocalDate(), (u) -> u.getCalories(), (v, nv) -> v + nv));

        return mealList.stream()
                .filter(u -> TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime))
                .map((u) -> new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), (summCaloriesPerDay.get(u.getDateTime().toLocalDate()) > caloriesPerDay)))
                .collect(Collectors.toList());
    }
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

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
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> listUserMealWithExceed = new ArrayList<>(); // список еды который нужно вернуть

        Map<LocalDate, Integer> summCaloriesPerDay = new HashMap<>(); //карта всех возможных дат и сумм каллорий по датам.

        for (UserMeal userMeal : mealList) { //вычисляем полное количество калорий за каждый день
            if (summCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) == null) {
                summCaloriesPerDay.put(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
            } else {
                int calories = summCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) + userMeal.getCalories();
                summCaloriesPerDay.put(userMeal.getDateTime().toLocalDate(), calories);
            }
        }

        for (UserMeal userMeal : mealList) { // добавляем в список UserMealWithExceed между startTime и endTime в дни обжорства

            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime) && summCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay) {
                listUserMealWithExceed.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
            }
        }

        return listUserMealWithExceed;
    }
}

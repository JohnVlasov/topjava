package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Meal meal1 = new Meal(100002, LocalDateTime.parse("2019-01-08 10:05:06", formatter), "Завтрак User", 500);
    public static Meal meal2 = new Meal(100003, LocalDateTime.parse("2019-01-08 14:00:00", formatter), "Обед User", 800);
    public static Meal meal3 = new Meal(100004, LocalDateTime.parse("2019-01-08 20:05:06", formatter), "Ужин User", 800);
    public static Meal meal4 = new Meal(100005, LocalDateTime.parse("2019-01-08 09:05:06", formatter), "Завтрак Admin", 1000);
    public static Meal meal5 = new Meal(100006, LocalDateTime.parse("2019-01-08 13:05:06", formatter), "Обед Admin", 1000);
    public static Meal meal6 = new Meal(100007, LocalDateTime.parse("2019-01-11 10:05:06", formatter), "Завтрак Admin", 1000);
    public static Meal meal7 = new Meal(100008, LocalDateTime.parse("2019-01-11 15:05:06", formatter), "Обед Admin", 1000);
    public static Meal meal8 = new Meal(100009, LocalDateTime.parse("2019-01-11 21:05:06", formatter), "ужин Admin", 1000);
    public static Meal meal9 = new Meal(100010, LocalDateTime.parse("2019-01-12 08:05:06", formatter), "Завтрак Admin", 1000);
    public static Meal meal10 = new Meal(100011, LocalDateTime.parse("2019-01-12 12:05:06", formatter), "Обед Admin", 1000);
    public static Meal meal11 = new Meal(100012, LocalDateTime.parse("2019-01-12 19:05:06", formatter), "ужин Admin", 1000);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user_id");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }

}

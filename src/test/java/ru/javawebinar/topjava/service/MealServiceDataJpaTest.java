package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

//@ActiveProfiles(profiles ={"datajpa", "postgres"})
@ActiveProfiles(Profiles.JDBC)
public class MealServiceDataJpaTest extends MealServiceTest {
}

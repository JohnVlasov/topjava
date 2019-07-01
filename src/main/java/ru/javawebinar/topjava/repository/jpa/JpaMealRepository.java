package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        User user = em.find(User.class, userId);
        meal.setUser(user);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            if (userId == get(meal.getId(), userId).getUser().getId()) {
                return em.merge(meal);
            }
            else return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createQuery("DELETE FROM Meal m WHERE m.user.id=:user_id AND m.id=:id")
                .setParameter("user_id", userId)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    @Transactional
    public Meal get(int id, int userId) {
        return em.createQuery("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.user.id=:user_id AND m.id=:id", Meal.class)
                .setParameter("user_id", userId)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    @Transactional
    public List<Meal> getAll(int userId) {
        List<Meal> meals = em.createQuery("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.user.id=:user_id ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
        return meals;
    }

    @Override
    @Transactional
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        List<Meal> meals = em.createQuery("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.user.id=:user_id AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("user_id", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        return meals;
    }
}
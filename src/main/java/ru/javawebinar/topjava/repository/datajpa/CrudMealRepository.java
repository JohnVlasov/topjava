package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int delete(@Param("id") int id, @Param("user_id") int user_id);

    //@Transactional
    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    Optional<Meal> findById(@Param("id") int id, @Param("user_id") int user_id);

    //@Transactional
    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id")
    List<Meal> findAll(@Param("user_id") int user_id, Sort sort);

    //@Transactional
    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime BETWEEN :startDate AND :endDate")
    List<Meal> findAll(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId, Sort sort);
}

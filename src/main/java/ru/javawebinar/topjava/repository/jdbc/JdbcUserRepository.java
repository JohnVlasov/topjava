package ru.javawebinar.topjava.repository.jdbc;

import org.hibernate.hql.internal.ast.tree.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("enabled", user.isEnabled())
                .addValue("registered", user.getRegistered())
                .addValue("calories_per_day", user.getCaloriesPerDay());

        if (user.isNew()) {

            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());

        } else {
            if (namedParameterJdbcTemplate.update("" +
                            "UPDATE users " +
                            "SET id=:id, name=:name, email=:email, password=:password, " +
                            "enabled=:enabled, registered=:registered, calories_per_day=:calories_per_day" +
                            " WHERE id=:id"
                    , map) == 0) {
                return null;
            } else {
                jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            }
        }

        String sql = "INSERT INTO user_roles (user_id, role) VALUES(?,?)";
        user.getRoles().forEach(role -> {
            jdbcTemplate.update(sql, user.getId(), role.toString());
        });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=" + id);
        Set<Role> set = list.stream().map(m -> Role.valueOf(m.get("role").toString())).collect(Collectors.toSet());

        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE users.id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) user.setRoles(set);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=" + user.getId());
            Set<Role> set = list.stream().map(m -> Role.valueOf(m.get("role").toString())).collect(Collectors.toSet());
            user.setRoles(set);
        }
        return user;
    }

    @Override
    public List<User> getAll() {

        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT user_id, role FROM user_roles");
        Map<Integer, HashSet<Role>> map = new HashMap<>();
        HashSet<Role> set;
        for (Map<String, Object> m : list) {
            int userId = Integer.parseInt(m.get("user_id").toString());
            Role userRole = Role.valueOf(m.get("role").toString());

            if (map.get(userId) != null) {
                set = map.get(userId);
                set.add(userRole);
            } else
                set = new HashSet<>();
            set.add(userRole);
            map.putIfAbsent(userId, set);
        }

        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.forEach(user -> user.setRoles(map.get(user.getId())));
        return users;
    }
}

package ru.javawebinar.topjava.repository.jdbc;

import org.hibernate.hql.internal.ast.tree.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
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
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            namedParameterJdbcTemplate.update("DELETE FROM user_roles WHERE user_id=:id", parameterSource);
        }
        Set<Role> set = (Set) parameterSource.getValue("roles");
        int id = (Integer) parameterSource.getValue("id");
        for (Role r : set) {
            String sql = "INSERT INTO user_roles (user_id, role) values('" + id + "', '" + r.toString() + "')";
            jdbcTemplate.update(sql);
        }
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
        Map<Integer, Set<Role>> map = new HashMap<>();

        for (Map<String, Object> m : list) {
            int userId = Integer.parseInt(m.get("user_id").toString());
            Role userRole = Role.valueOf(m.get("role").toString());
// не работает.....
            if (map.get(userId) != null) {
                Set<Role> set = map.get(userId);
                set.add(userRole);
                map.put(userId, set);
                set = null;
            } else map.putIfAbsent(userId, Set.of(userRole));
        }

        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.forEach(user -> user.setRoles(map.get(user.getId())));
        return users;
    }
}

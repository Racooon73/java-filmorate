package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
@Component("DbUserStorage")
public class DbUserStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    public DbUserStorage(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public User addUser(User user) throws NotFoundException {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();

            return getUserById(id);
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        String sqlQuery = "update USERS set EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getUserById(user.getId());
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> users = new HashMap<>();
        String sqlQuery = "select * from users";
        for (User user : jdbcTemplate.query(sqlQuery, this::mapRowToUser)) {
            users.put(user.getId(),user);
        }

            return users;
    }
    public User getUserById(int id) throws NotFoundException {
        if(userDao.getUserById(id).isPresent()){
            return userDao.getUserById(id).get();
        }else{
            throw new NotFoundException();
        }
    }
    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday"));

    }
}

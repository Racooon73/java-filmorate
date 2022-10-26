package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Optional<User> getUserById(int id) throws NotFoundException {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);

        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));
            return Optional.of(user);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into FRIENDS(USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);

    }
    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?" ;
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);

    }

    @Override
    public List<User> getFriends(Integer id) {

        String sqlQuery = "select distinct id, email, login, name, birthday " +
                "from friends " +
                "join users u on u.id = friends.friend_id " +
                "where friends.user_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        String sqlQuery = "select distinct friend1.id,friend1.email, friend1.login, friend1.name, friend1.birthday " +
                "from (select id, email, login, name, birthday from friends" +
                "        left join users u on u.id = friends.friend_id" +
                "      where friends.user_id = ?1) as friend1" +
                " join (select id, email, login, name, birthday from friends " +
                "        left join users u on u.id = friends.friend_id " +
                "where friends.user_id = ?2) as friend2 on friend1.id = friend2.id " +
                "where friend1.id = friend2.id and friend2.id = friend1.id";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId,otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday"));

    }
}

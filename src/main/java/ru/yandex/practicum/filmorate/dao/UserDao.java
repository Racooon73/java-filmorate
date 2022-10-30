package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao {
    Optional<User> getUserById(int id) throws NotFoundException;
    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<User> getFriends(Integer id);
    List<User> getCommonFriends(int userId, int otherId);

}

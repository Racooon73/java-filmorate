package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    public User addUser(User user) throws NotFoundException;
    public User updateUser(User user) throws NotFoundException;
    public Map<Integer, User> getUsers();
}

package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;


@Component
public class inMemoryUserStorage implements UserStorage{
    private int id = 0;
    private final Map<Integer,User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(++id);
        if(user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(),user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
       users.put(user.getId(),user);
       return users.get(user.getId());
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

}

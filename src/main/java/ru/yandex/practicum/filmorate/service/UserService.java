package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;
    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("DbUserStorage") UserStorage storage, UserDao userDao){
        this.storage = storage;

        this.userDao = userDao;
    }
    public User addUser(User user) throws NotFoundException {
        return storage.addUser(user);
    }
    public User updateUser(User user) throws NotFoundException {
        if(userDao.getUserById(user.getId()).isEmpty()){
            throw new NotFoundException();
        }
        return storage.updateUser(user);
    }
    public List<User> getUsers(){
      return new ArrayList<>(storage.getUsers().values());
    }
    public User getUser(int id) throws NotFoundException {
        if(userDao.getUserById(id).isEmpty()){
            throw new NotFoundException();
        }
        return userDao.getUserById(id).get();
    }
    public void addFriend(Integer id, Integer friendId) throws NotFoundException {
        if(userDao.getUserById(id).isEmpty() || userDao.getUserById(friendId).isEmpty()){
            throw new NotFoundException();
        }
        userDao.addFriend(id,friendId);
    }
    public void deleteFriend(Integer id, Integer friendId) throws NotFoundException {
        if(userDao.getUserById(id).isEmpty() || userDao.getUserById(friendId).isEmpty()){
            throw new NotFoundException();
        }
        userDao.deleteFriend(id,friendId);

    }
    public List<User> getFriends(Integer id) throws NotFoundException {
        if(userDao.getUserById(id).isEmpty()){
            throw new NotFoundException();
        }
        return userDao.getFriends(id);
    }
    public List<User> getMutualFriends(Integer id, Integer otherId) throws NotFoundException {

        if(userDao.getUserById(id).isEmpty() || userDao.getUserById(otherId).isEmpty()){
            throw new NotFoundException();
        }

        return userDao.getCommonFriends(id,otherId);


    }

}

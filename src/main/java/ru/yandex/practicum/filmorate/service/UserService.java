package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage storage;
    @Autowired
    public UserService(UserStorage storage){
        this.storage = storage;

    }
    public User addUser(User user){
        return storage.addUser(user);
    }
    public User updateUser(User user) throws NotFoundException {
        if(!storage.getUsers().containsKey(user.getId())){
            throw new NotFoundException();
        }
        return storage.updateUser(user);
    }
    public List<User> getUsers(){
      return new ArrayList<>(storage.getUsers().values());
    }
    public User getUser(int id) throws NotFoundException {
        if(!storage.getUsers().containsKey(id)){
            throw new NotFoundException();
        }
        return storage.getUsers().get(id);
    }
    public void addFriend(Integer id, Integer friendId) throws NotFoundException {
        if(!storage.getUsers().containsKey(id) || !storage.getUsers().containsKey(friendId)){
            throw new NotFoundException();
        }
        storage.getUsers().get(id).getFriends().add(friendId);
        storage.getUsers().get(friendId).getFriends().add(id);
    }
    public void deleteFriend(Integer id, Integer friendId) throws NotFoundException {
        if(!storage.getUsers().containsKey(id) || !storage.getUsers().containsKey(friendId)){
            throw new NotFoundException();
        }
        storage.getUsers().get(id).getFriends().remove(friendId);
        storage.getUsers().get(friendId).getFriends().remove(id);

    }
    public Set<User> getFriends(Integer id) throws NotFoundException {
        if(!storage.getUsers().containsKey(id)){
            throw new NotFoundException();
        }
        Set<User> friends = new HashSet<>();
        for (Integer friend : storage.getUsers().get(id).getFriends()) {
            friends.add(storage.getUsers().get(friend));
        }
        return friends;
    }
    public Set<User> getMutualFriends(Integer id, Integer otherId) throws NotFoundException {

        if(!storage.getUsers().containsKey(id) || !storage.getUsers().containsKey(otherId)){
            throw new NotFoundException();
        }
        Set<User> mutual = new HashSet<>();
        Set<Integer> firstUserFriends = storage.getUsers().get(id).getFriends();
        Set<Integer> secondUserFriends = storage.getUsers().get(otherId).getFriends();

        for (Integer firstUserFriend : firstUserFriends) {
            if (secondUserFriends.contains(firstUserFriend)) {
                mutual.add(storage.getUsers().get(firstUserFriend));
            }
        }
        return mutual;


    }
    public UserStorage getStorage(){
        return storage;
    }

}

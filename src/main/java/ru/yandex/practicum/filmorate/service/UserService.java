package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage storage;
    @Autowired
    public UserService(UserStorage storage){
        this.storage = storage;

    }
    public void addFriend(Integer firstUserId, Integer secondUserId){
        storage.getUsers().get(firstUserId).getFriends().add(secondUserId);
        storage.getUsers().get(secondUserId).getFriends().add(firstUserId);
    }
    public void deleteFriend(Integer firstUserId, Integer secondUserId){
        storage.getUsers().get(firstUserId).getFriends().remove(secondUserId);
        storage.getUsers().get(secondUserId).getFriends().remove(firstUserId);

    }
    public Set<User> getFriends(Integer userId){
        Set<User> friends = new HashSet<>();
        for (Integer friend : storage.getUsers().get(userId).getFriends()) {
            friends.add(storage.getUsers().get(friend));
        }
        return friends;
    }
    public Set<User> getMutualFriends(Integer firstUserId, Integer secondUserId){

        Set<User> mutual = new HashSet<>();
        Set<Integer> firstUserFriends = storage.getUsers().get(firstUserId).getFriends();
        Set<Integer> secondUserFriends = storage.getUsers().get(secondUserId).getFriends();

        for (Integer firstUserFriend : firstUserFriends) {
            if (secondUserFriends.contains(firstUserFriend)) {
                mutual.add(storage.getUsers().get(firstUserFriend));
            }
        }
        return mutual;


    }

}

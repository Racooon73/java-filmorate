package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;
    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;

    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        log.info("Получен запрос POST /users.");
        userStorage.addUser(user);
        return userStorage.getUsers().get(user.getId());
    }
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("Получен запрос PUT /users.");
        if(!userStorage.getUsers().containsKey(user.getId())){
             throw new NotFoundException();
        }
        userStorage.updateUser(user);
        return userStorage.getUsers().get(user.getId());

    }
    @GetMapping("/users")
    public List <User> getUsers(){
        log.info("Получен запрос GET /users.");
        return new ArrayList<>(userStorage.getUsers().values());
    }
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /users/"+id);
        if(!userStorage.getUsers().containsKey(id)){
            throw new NotFoundException();
        }
        return userStorage.getUsers().get(id);
    }
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос PUT /users/"+ id +"/friends/" + friendId);
        if(!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)){
            throw new NotFoundException();
        }
        userService.addFriend(id,friendId);
        log.info(id + " теперь друг " + friendId);

    }
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос DELETE /users/"+ id +"/friends/" + friendId);
        if(!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)){
            throw new NotFoundException();
        }
        userService.deleteFriend(id,friendId);
        log.info(id + " больше не друг " + friendId);
    }
    @GetMapping("/users/{id}/friends")
    public Set<User> getUserFriends(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /users/"+id+"/friends");
        if(!userStorage.getUsers().containsKey(id)){
            throw new NotFoundException();
        }
        return userService.getFriends(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getUserFriends(@PathVariable int id, @PathVariable int otherId) throws NotFoundException {
        log.info("Получен запрос GET /users/"+id+"/friends/common"+otherId);
        if(!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(otherId)){
            throw new NotFoundException();
        }
        return userService.getMutualFriends(id,otherId);
    }

}

package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("Получен запрос POST /users.");
        return userService.addUser(user);
    }
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("Получен запрос PUT /users.");
        return userService.updateUser(user);

    }
    @GetMapping("/users")
    public List <User> getUsers(){
        log.info("Получен запрос GET /users.");
        return userService.getUsers();
    }
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /users/"+id);
        return userService.getUser(id);
    }
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос PUT /users/"+ id +"/friends/" + friendId);
        userService.addFriend(id,friendId);
        log.info(id + " теперь друг " + friendId);

    }
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос DELETE /users/"+ id +"/friends/" + friendId);
        userService.deleteFriend(id,friendId);
        log.info(id + " больше не друг " + friendId);
    }
    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /users/"+id+"/friends");
        return userService.getFriends(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getUserFriends(@PathVariable int id, @PathVariable int otherId) throws NotFoundException {
        log.info("Получен запрос GET /users/"+id+"/friends/common"+otherId);
        return userService.getMutualFriends(id,otherId);
    }

}

package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer,User> users = new HashMap<>();

    @PostMapping("/users")
    User addUser(@Valid @RequestBody User user){
        users.put(user.getId(),user);
        return users.get(user.getId());
    }
    @PutMapping("/users")
    User updateUser(@Valid @RequestBody User user){
        users.put(user.getId(),user);
        return users.get(user.getId());
    }
    @GetMapping("/users")
    List <User> getUsers(){
        return new ArrayList<>(users.values());
    }
}

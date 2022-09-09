package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
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
    private int id = 0;

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        log.info("Получен запрос POST /users.");
        user.setId(++id);
        if(user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(),user);
        return users.get(user.getId());
    }
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("Получен запрос PUT /users.");
        if(!users.containsKey(user.getId())){
            throw new NotFoundException();
        }
        users.put(user.getId(),user);
        return users.get(user.getId());
    }
    @GetMapping("/users")
    public List <User> getUsers(){
        log.info("Получен запрос GET /users.");
        return new ArrayList<>(users.values());
    }
}

package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;

    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        log.info("Получен запрос POST /films.");
        return filmService.addFilm(film);

    }
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException, ValidationException {
        log.info("Получен запрос PUT /films.");
        return filmService.updateFilm(film);

    }
    @GetMapping("/films")
    public List <Film> getFilms(){
        log.info("Получен запрос GET /films.");
        return filmService.getFilms();
    }
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /films/"+id);
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        log.info("Получен запрос PUT /films/"+id+"like/"+userId);
        filmService.addLike(id,userId);
        log.info("Фильм "+id+" понравился пользователю "+userId);

    }
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        log.info("Получен запрос DELETE /films/"+id+"like/"+userId);
        filmService.deleteLike(id,userId);
        log.info("Фильм "+id+" не понравился пользователю "+userId);

    }
    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10",required = false) int count){
        log.info("Получен запрос GET /films/popular?count="+count);
        return filmService.getPopularFilms(count);
    }

}

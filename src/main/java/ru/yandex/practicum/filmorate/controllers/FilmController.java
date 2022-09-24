package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserStorage userStorage;
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;

    }
    void validation(Film film) throws ValidationException {
        if(film.getReleaseDate().isBefore(MIN_DATE)){
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

    }
    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос POST /films.");
        validation(film);
        log.info("Добавлен фильм: {}",film);
        return filmStorage.addFilm(film);

    }
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        log.info("Получен запрос PUT /films.");
        if(!filmStorage.getFilms().containsKey(film.getId())){
            throw new NotFoundException();
        }
           return filmStorage.updateFilm(film);

    }
    @GetMapping("/films")
    public List <Film> getFilms(){
        log.info("Получен запрос GET /films.");

       return new ArrayList<>(filmStorage.getFilms().values());
    }
    @GetMapping("/films/{id}")
    public Film getFilms(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /films/"+id);
        if(!filmStorage.getFilms().containsKey(id)){
            throw new NotFoundException();
        }
        return filmStorage.getFilms().get(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        log.info("Получен запрос PUT /films/"+id+"like/"+userId);
        if(!filmStorage.getFilms().containsKey(id) || !userStorage.getUsers().containsKey(userId)){
            throw new NotFoundException();
        }
        filmService.addLike(id,userId);
        log.info("Фильм "+id+" понравился пользователю "+userId);

    }
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        log.info("Получен запрос DELETE /films/"+id+"like/"+userId);
        if(!filmStorage.getFilms().containsKey(id) || !userStorage.getUsers().containsKey(userId)){
            throw new NotFoundException();
        }
        filmService.deleteLike(id,userId);
        log.info("Фильм "+id+" не понравился пользователю "+userId);

    }
    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10",required = false) int count){
        log.info("Получен запрос GET /films/popular?count="+count);
        return filmService.getPopularFilms(count);
    }

}

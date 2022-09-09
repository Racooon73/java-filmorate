package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос POST /films.");
        validation(film);
        film.setId(++id);
        films.put(film.getId(),film);
        log.info("Добавлен фильм: {}",film);
        return films.get(film.getId());

    }
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        log.info("Получен запрос PUT /films.");
        if(!films.containsKey(film.getId())){
            log.error("Такого фильма не существует в базе");
            throw new NotFoundException();
        }
            films.put(film.getId(),film);
            log.info("Обновлен фильм: {}",film);
            return films.get(film.getId());

    }
    @GetMapping("/films")
    public List <Film> getFilms(){
        log.info("Получен запрос GET /films.");

        return new ArrayList<>(films.values());
    }


    void validation(Film film) throws ValidationException {
        if(film.getReleaseDate().isBefore(MIN_DATE)){
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

    }

}

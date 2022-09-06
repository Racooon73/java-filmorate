package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate MIN_DATE = LocalDate.of(1985, 12, 28);

    @PostMapping("/films")
    Film addFilm(@Valid @RequestBody Film film){
        films.put(film.getId(),film);

        return films.get(film.getId());
    }
    @PutMapping("/films")
    Film updateFilm(@Valid @RequestBody Film film){
        films.put(film.getId(),film);
        return films.get(film.getId());
    }
    @GetMapping("/films")
    List <Film> getFilms(){
        return new ArrayList<>(films.values());
    }

    boolean validation(Film film){
        return !film.getReleaseDate().isBefore(MIN_DATE);
    }



}

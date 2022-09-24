package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class inMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(),film);

        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film)  {

        films.put(film.getId(),film);
        log.info("Обновлен фильм: {}",film);
        return films.get(film.getId());
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }
}

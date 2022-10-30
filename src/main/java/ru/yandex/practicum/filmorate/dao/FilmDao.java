package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> getFilmById(int id) throws NotFoundException;
    void addLike(int filmId, int userId);
    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

}

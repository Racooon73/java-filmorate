package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    public Film addFilm(Film film) throws NotFoundException;
    public Film updateFilm(Film film) throws NotFoundException;
    public Map<Integer, Film> getFilms();

}

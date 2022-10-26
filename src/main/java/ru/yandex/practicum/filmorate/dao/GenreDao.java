package ru.yandex.practicum.filmorate.dao;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getGenres();
    Genre getGenreById(int id) throws NotFoundException;
}

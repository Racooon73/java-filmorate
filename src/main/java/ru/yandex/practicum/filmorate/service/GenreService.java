package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao){

        this.genreDao = genreDao;
    }
    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Genre getGenreById(int id) throws NotFoundException {
        return genreDao.getGenreById(id);
    }
}

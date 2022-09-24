package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;


@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(Integer filmId, Integer userId) {
        storage.getFilms().get(filmId).getLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        storage.getFilms().get(filmId).getLikes().remove(userId);

    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popFilms = new ArrayList<>(storage.getFilms().values());
        popFilms.sort((lhs, rhs) -> rhs.getLikes().size() - lhs.getLikes().size());
        List<Film> ans = new ArrayList<>();
        if(storage.getFilms().size() < count){
            count = storage.getFilms().size();
        }
        for (int i = 0; i < count; i++) {
            ans.add(popFilms.get(i));
        }

        return ans;
    }
}
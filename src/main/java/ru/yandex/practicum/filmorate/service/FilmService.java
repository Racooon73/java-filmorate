package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }
    void validation(Film film) throws ValidationException {
        if(film.getReleaseDate().isBefore(MIN_DATE)){
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

    }
    public Film addFilm(Film film) throws ValidationException {
        validation(film);
        return storage.addFilm(film);
    }
    public Film updateFilm(Film film) throws NotFoundException, ValidationException {
        validation(film);
        if(!storage.getFilms().containsKey(film.getId())){
            throw new NotFoundException();
        }
        return storage.updateFilm(film);
    }
    public List <Film> getFilms(){
        return new ArrayList<>(storage.getFilms().values());
    }
    public Film getFilm(int id) throws NotFoundException {
        if(!storage.getFilms().containsKey(id)){
            throw new NotFoundException();
        }
        return storage.getFilms().get(id);

    }
    public void addLike(Integer id, Integer userId) throws NotFoundException {
        if(!storage.getFilms().containsKey(id) || !userStorage.getUsers().containsKey(userId)){
            throw new NotFoundException();
        }
        storage.getFilms().get(id).getLikes().add(userId);
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundException {
        if(!storage.getFilms().containsKey(id) || !userStorage.getUsers().containsKey(userId)){
            throw new NotFoundException();
        }
        storage.getFilms().get(id).getLikes().remove(userId);

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
    public FilmStorage getStorage(){
        return storage;
    }
}
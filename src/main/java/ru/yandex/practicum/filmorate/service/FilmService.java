package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final FilmDao filmDao;
    private final UserDao userDao;
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    @Autowired
    public FilmService(@Qualifier("DbFilmStorage") FilmStorage storage, FilmDao filmDao, UserDao userDao) {
        this.storage = storage;
        this.filmDao = filmDao;
        this.userDao = userDao;
    }
    void validation(Film film) throws ValidationException {
        if(film.getReleaseDate().before(Date.valueOf(MIN_DATE))){
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

    }
    public Film addFilm(Film film) throws ValidationException, NotFoundException {
        validation(film);
        return storage.addFilm(film);
    }
    public Film updateFilm(Film film) throws NotFoundException, ValidationException {
        validation(film);
        if(filmDao.getFilmById(film.getId()).isEmpty()){
            throw new NotFoundException();
        }
        return storage.updateFilm(film);
    }
    public List <Film> getFilms(){
        return new ArrayList<>(storage.getFilms().values());
    }
    public Film getFilm(int id) throws NotFoundException {
        if(filmDao.getFilmById(id).isEmpty()){
            throw new NotFoundException();
        }
        if(filmDao.getFilmById(id).isPresent()){
        return filmDao.getFilmById(id).get();
        }else{
            throw new NotFoundException();
        }

    }
    public void addLike(Integer id, Integer userId) throws NotFoundException {
        if(filmDao.getFilmById(id).isEmpty() || userDao.getUserById(userId).isEmpty()){
            throw new NotFoundException();
        }
        filmDao.addLike(id,userId);
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundException {
        if(filmDao.getFilmById(id).isEmpty() || userDao.getUserById(userId).isEmpty()){
            throw new NotFoundException();
        }
        filmDao.deleteLike(id,userId);

    }

    public List<Film> getPopularFilms(int count) {
        List<Film> all = new ArrayList<>();
        List<Film> pop = filmDao.getPopularFilms(count);
        if(pop.size() == 0){
            pop = getFilms();
            for (int i = 0; i < count && i < pop.size(); i++) {
             all.add(pop.get(i));
            }
            return all;
        }
        return pop;
    }

}
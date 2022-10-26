package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.inMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.user.inMemoryUserStorage;


import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	FilmController filmController;
	Film film;
	User user;
	UserController userController;
	FilmService filmService;
	UserService userService;
	JdbcTemplate jdbcTemplate;
	UserDao userDao;
	FilmDao filmDao;
	@BeforeEach
	void beforeEach(){
		film = new Film();
		user = new User();
		jdbcTemplate = new JdbcTemplate();
		UserDao userDao = new UserDaoImpl(jdbcTemplate);
		FilmDao filmDao = new FilmDaoImpl(jdbcTemplate);
		inMemoryFilmStorage filmStorage = new inMemoryFilmStorage();
		inMemoryUserStorage userStorage = new inMemoryUserStorage();
		filmService = new FilmService(filmStorage, filmDao, userDao);

		userService = new UserService(userStorage, userDao);
		filmController = new FilmController( filmService);
		userController = new UserController(userService);
	}
	private final DbUserStorage userStorage;


	@Test
	void filmReleaseDateBorderTest() throws ValidationException, NotFoundException {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(new Date(1895, 12, 28));
		filmController.addFilm(film);
		Assertions.assertEquals(film
				,filmController.getFilms().get(0)
				,"Граничная дата не прошла валидацию");
	}
	@Test
	void filmReleaseDateTest() throws ValidationException, NotFoundException {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(new Date(1900, 12, 28));
		filmController.addFilm(film);
		Assertions.assertEquals(film
				,filmController.getFilms().get(0)
				,"Дата не прошла валидацию");
	}
	


}

package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.inMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.inMemoryUserStorage;


import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {
	FilmController filmController;
	Film film;
	User user;
	UserController userController;
	FilmService filmService;
	UserService userService;

	@BeforeEach
	void beforeEach(){
		film = new Film();
		user = new User();

		inMemoryFilmStorage filmStorage = new inMemoryFilmStorage();
		inMemoryUserStorage userStorage = new inMemoryUserStorage();
		filmService = new FilmService(filmStorage);
		userService = new UserService(userStorage);
		filmController = new FilmController(filmStorage, filmService,userStorage);
		userController = new UserController(userStorage, userService);
	}

	@Test
	void wrongFilmReleaseDateTest() {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(LocalDate.of(1894, 12, 28));
		Assertions.assertThrows(ValidationException.class
				, () -> filmController.addFilm(film)
				, "Пройдена валидация с неверной датой");

	}
	@Test
	void filmReleaseDateBorderTest() throws ValidationException, NotFoundException {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
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
		film.setReleaseDate(LocalDate.of(1900, 12, 28));
		filmController.addFilm(film);
		Assertions.assertEquals(film
				,filmController.getFilms().get(0)
				,"Дата не прошла валидацию");
	}
	@Test
	void filmEmptyUpdateTest() {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(LocalDate.of(1900, 12, 28));
		Assertions.assertThrows(NotFoundException.class
				, () -> filmController.updateFilm(film)
				, "Обновлен несуществующий фильм");
	}
	@Test
	void filmUpdateTest() throws ValidationException, NotFoundException {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(LocalDate.of(1900, 12, 28));
		filmController.addFilm(film);
		film.setDescription("UpdatedTest");
		filmController.updateFilm(film);
		Assertions.assertEquals(film,
				filmController.getFilms().get(0)
				, "Фильм не обновлён");
	}
	@Test
	void userEmptyUpdateTest() {
		user.setId(1);
		user.setName("Test");
		user.setLogin("test");
		user.setEmail("Test@test.com");
		user.setBirthday(LocalDate.of(1900, 12, 28));
		Assertions.assertThrows(NotFoundException.class
				, () -> userController.updateUser(user)
				, "Обновлен несуществующий пользователь");
	}
	@Test
	void userUpdateTest() throws ValidationException, NotFoundException {
		user.setId(1);
		user.setName("Test");
		user.setLogin("test");
		user.setEmail("Test@test.com");
		user.setBirthday(LocalDate.of(1900, 12, 28));
		userController.addUser(user);
		user.setName("UpdatedUser");
		userController.updateUser(user);
		Assertions.assertEquals(user,
				userController.getUsers().get(0)
				, "Пользователь не обновлён");
	}


}

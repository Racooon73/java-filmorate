package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.GenreController;
import ru.yandex.practicum.filmorate.controllers.MpaController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;


import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmorateApplicationTests {
	FilmController filmController;
	Film film;
	User user;
	UserController userController;

	GenreController genreController;
	MpaController mpaController;
	FilmService filmService;
	UserService userService;
	GenreService genreService;
	MpaService mpaService;
	JdbcTemplate jdbcTemplate;
	UserDao userDao;
	FilmDao filmDao;
	@Autowired
	private DataSource dataSource;
	@BeforeEach
	void beforeEach() {

		jdbcTemplate = new JdbcTemplate(dataSource);
		userDao = new UserDaoImpl(jdbcTemplate);
		filmDao = new FilmDaoImpl(jdbcTemplate);

		DbFilmStorage filmStorage = new DbFilmStorage(jdbcTemplate,filmDao);
		DbUserStorage userStorage = new DbUserStorage(jdbcTemplate,userDao);

		filmService = new FilmService(filmStorage, filmDao, userDao);
		userService = new UserService(userStorage, userDao);
		genreService = new GenreService(new GenreDaoImpl(jdbcTemplate));
		mpaService = new MpaService(new MpaDaoImpl(jdbcTemplate));
		filmController = new FilmController(filmService);
		userController = new UserController(userService);
		genreController = new GenreController(genreService);
		mpaController = new MpaController(mpaService);
		user = new User();
		film = new Film();
	}


	@Test
	void wrongFilmReleaseDateTest() {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(Date.valueOf(LocalDate.of(1894, 12, 28)));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
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
		film.setReleaseDate(new java.sql.Date(1895, 12, 28));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
		filmController.addFilm(film);
		Assertions.assertEquals(film
				,filmController.getFilm(1)
				,"Граничная дата не прошла валидацию");
	}
	@Test
	void filmReleaseDateTest() throws ValidationException, NotFoundException {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
		filmController.addFilm(film);
		Assertions.assertEquals(film
				,filmController.getFilm(1)
				,"Дата не прошла валидацию");
	}
	@Test
	void filmEmptyUpdateTest() {
		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
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
		film.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
		filmController.addFilm(film);
		film.setDescription("UpdatedTest");
		filmController.updateFilm(film);
		Assertions.assertEquals(film,
				filmController.getFilms().get(0)
				, "Фильм не обновлён");
	}
	@Test
	public void testFindUserById() throws NotFoundException {
		user.setId(1);
		user.setLogin("Test");
		user.setEmail("test@test.com");
		user.setName("test");
		user.setBirthday(new Date(2000, 12, 28));

		userController.addUser(user);
		Optional<User> userOptional = Optional.of(userController.getUser(1));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}
	@Test
	void userEmptyUpdateTest() {
		user.setId(1);
		user.setName("Test");
		user.setLogin("test");
		user.setEmail("Test@test.com");
		user.setBirthday(new Date(2000, 12, 28));
		Assertions.assertThrows(NotFoundException.class
				, () -> userController.updateUser(user)
				, "Обновлен несуществующий пользователь");
	}
	@Test
	void userUpdateTest() throws NotFoundException {
		user.setId(1);
		user.setName("Test");
		user.setLogin("test");
		user.setEmail("Test@test.com");
		user.setBirthday(new Date(2000, 12, 28));
		userController.addUser(user);
		user.setName("UpdatedUser");
		userController.updateUser(user);
		Assertions.assertEquals(user,
				userController.getUser(1)
				, "Пользователь не обновлён");
	}
	@Test
	void getGenreTest() throws NotFoundException {
		Genre genre = new Genre(1,"Комедия");
		Assertions.assertEquals(genre,genreController.getGenreById(1),"Жанр не найден");
	}
	@Test
	void getMpaTest() throws NotFoundException {
		Mpa mpa = new Mpa(1,"G");
		Assertions.assertEquals(mpa,mpaController.getMpaById(1),"Рейтинг не найден");
	}
	@Test
	void userAddFriendTest() throws NotFoundException {
		user.setId(1);
		user.setLogin("Test");
		user.setEmail("test@test.com");
		user.setName("test");
		user.setBirthday(new Date(2000, 12, 28));

		userController.addUser(user);
		User friend = new User(2,"Test2","Friend","Friend",new Date(2000, 12, 28));
		userController.addUser(friend);

		userController.addFriend(1,2);
		Assertions.assertEquals(friend,userController.getUserFriends(1).get(0),"Друг не добавлен");
	}
	@Test
	void userDeleteFriendTest() throws NotFoundException {
		user.setId(1);
		user.setLogin("Test");
		user.setEmail("test@test.com");
		user.setName("test");
		user.setBirthday(new Date(2000, 12, 28));

		userController.addUser(user);
		User friend = new User(2,"Test2","Friend","Friend",new Date(2000, 12, 28));
		userController.addUser(friend);


		userController.addFriend(1,2);
		userController.deleteFriend(1,2);
		Assertions.assertEquals(0,userController.getUserFriends(1).size(),"Друг не удалён");
	}
	@Test
	void filmAddLike() throws NotFoundException, ValidationException {
		user.setId(1);
		user.setLogin("Test");
		user.setEmail("test@test.com");
		user.setName("test");
		user.setBirthday(new Date(2000, 12, 28));

		userController.addUser(user);

		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
		filmController.addFilm(film);

		Film film2 = new Film();
		film2.setId(2);
		film2.setName("Test2");
		film2.setDuration(202);
		film2.setDescription("Test2");
		film2.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film2.getMpa().setId(1);
		filmController.addFilm(film2);

		filmController.addLikeFilm(2,1);
		Assertions.assertEquals(film2,filmController.getPopular(1).get(0),"Лайк не добавлен");
	}
	@Test
	void filmDeleteLike() throws NotFoundException, ValidationException {
		user.setId(1);
		user.setLogin("Test");
		user.setEmail("test@test.com");
		user.setName("test");
		user.setBirthday(new Date(2000, 12, 28));

		userController.addUser(user);

		film.setId(1);
		film.setName("Test");
		film.setDuration(20);
		film.setDescription("Test");
		film.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film.getMpa().setId(1);
		film.getMpa().setName("G");
		filmController.addFilm(film);

		Film film2 = new Film();
		film2.setId(2);
		film2.setName("Test2");
		film2.setDuration(202);
		film2.setDescription("Test2");
		film2.setReleaseDate(new java.sql.Date(1900, 12, 28));
		film2.getMpa().setId(1);
		filmController.addFilm(film2);

		filmController.addLikeFilm(2,1);
		filmController.deleteLikeFilm(2,1);
		Assertions.assertEquals(film,filmController.getPopular(1).get(0),"Лайк не удалён");
	}

}

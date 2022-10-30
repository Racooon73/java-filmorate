package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("DbFilmStorage")
public class DbFilmStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    private final FilmDao filmDao;

    public DbFilmStorage(JdbcTemplate jdbcTemplate, FilmDao filmDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
    }

    @Override
    public Film addFilm(Film film) throws NotFoundException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int newId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        jdbcTemplate.update("update FILMS set RATING_ID =  ?1 where ID = ?2"
                ,film.getMpa().getId(),newId);
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into FILM_GENRE ( GENRE_ID , FILM_ID ) values (?,?) "
                    ,genre.getId(),newId);
        }

        return getFilmById(newId);
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        String sqlQuery = "update films set name = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? ,RATING_ID = ?" +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        jdbcTemplate.update("delete from FILM_GENRE where FILM_ID = ?",film.getId());
        if(film.getGenres().size() == 0){
            jdbcTemplate.update("delete from FILM_GENRE where FILM_ID = ?",film.getId());
        }else{

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("merge into FILM_GENRE KEY (FILM_ID,GENRE_ID)  values (?,?)"
                    ,film.getId(),genre.getId());
        }
        }
        if(filmDao.getFilmById(film.getId()).isPresent()) {
            return filmDao.getFilmById(film.getId()).get();
        }else{
            throw new NotFoundException();
        }
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> films = new HashMap<>();
        String sqlQuery = "select * from FILMS";
        for (Film film : jdbcTemplate.query(sqlQuery, this::mapRowToFilm)) {
            for (Genre genre : jdbcTemplate.query("SELECT G2.genre_id,genre_name " +
                    "FROM FILM_GENRE join GENRE G2 on film_genre.genre_id = G2.GENRE_ID " +
                    "WHERE film_id = ?", this::mapRowToGenre, film.getId())) {
                film.getGenres().add(genre);

            }
            List<String> mpaName = jdbcTemplate.query("SELECT rating_name from RATINGS join films f on ratings.rating_id = f.rating_id " +
                            "where ratings.rating_id = ?",
                    (rs, rowNum) -> rs.getString("RATING_NAME"),film.getMpa().getId());
            film.getMpa().setName(mpaName.get(0));

            films.put(film.getId(),film);
        }

        return films;
    }
    public Film getFilmById(int id) throws NotFoundException {
        if(filmDao.getFilmById(id).isPresent()){
            return filmDao.getFilmById(id).get();
        }else{
            throw new NotFoundException();
        }
    }
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date"),
                resultSet.getInt("duration"),
                resultSet.getInt("rating_id"));

    }
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),
                resultSet.getString("genre_name"));
    }
}

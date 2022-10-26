package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public Optional<Film> getFilmById(int id) throws NotFoundException {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS where id = ?", id);

        if(filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date"),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rating_id"));
            List<Genre> genreRows = jdbcTemplate.query("select * from GENRE " +
                    "join FILM_GENRE FG on GENRE.GENRE_ID = FG.GENRE_ID where FILM_ID  = ?",(rs, rowNum) ->
                    new Genre(
                            rs.getInt("GENRE.GENRE_ID"),
                            rs.getString("GENRE_NAME")), id);
            for (Genre genre : genreRows) {
                film.getGenres().add(genre);
            }

            List<String> mpa = jdbcTemplate.query("select RATING_NAME from RATINGS where RATING_ID = ?",
                    (rs, rowNum) -> rs.getString("RATING_NAME"),
                    film.getMpa().getId());
            film.getMpa().setName(mpa.get(0));

            return Optional.of(film);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update("insert into LIKES (USER_ID, FILM_LIKE_ID) values (?,?)"
                ,userId,filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("delete from LIKES where USER_ID = ? and FILM_LIKE_ID = ?"
                ,userId,filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> pop =  new ArrayList<>();
        String sqlQuery = "SELECT film_like_id, COUNT( distinct user_id) as c," +
                " id, name, description, release_date, duration, rating_id " +
                "FROM likes join films f on f.id = likes.film_like_id " +
                "GROUP BY film_like_id order by c desc limit ?";

        for (Film film : jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count)) {
            pop.add(film);
        }
        return pop;
    }
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date"),
                resultSet.getInt("duration"),
                resultSet.getInt("rating_id"));

    }
}

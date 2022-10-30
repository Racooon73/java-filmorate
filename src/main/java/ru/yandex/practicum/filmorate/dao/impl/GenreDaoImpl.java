package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "select * from GENRE";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre));
    }

    @Override
    public Genre getGenreById(int id) throws NotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from GENRE where GENRE_ID = ?", id);

        if(filmRows.next()) {
            return new Genre(
                    filmRows.getInt("genre_id"),
                    filmRows.getString("genre_name"));
        }else {
            throw new NotFoundException();
        }
    }
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),
                resultSet.getString("genre_name"));
    }
}

package ru.yandex.practicum.filmorate.dao.impl;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpa() {
        String sqlQuery = "select * from RATINGS";

        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToMpa));

    }

    @Override
    public Mpa getMpaById(int id) throws NotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from RATINGS where RATING_ID = ?", id);

        if(filmRows.next()) {
            return new Mpa(
                    filmRows.getInt("rating_id"),
                    filmRows.getString("rating_name"));
        }else {
            throw new NotFoundException();
        }

    }
    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("rating_id"),
                resultSet.getString("rating_name"));
    }
}

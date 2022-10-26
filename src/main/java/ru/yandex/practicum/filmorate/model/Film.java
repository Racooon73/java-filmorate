package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Data
public class Film {

    @NotNull
    private int id;
    @NotBlank
    private String name;
    private java.sql.Date releaseDate;
    @Size(max = 200)
    private String description;
    @Positive
    private int duration;
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    private Mpa mpa = new Mpa();




    public Film() {
    }

    public Film(int id, String name, String description, Date releaseDate, int duration, int mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa.setId(mpa);

    }
    public Film(int id, String name, String description, Date releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;

    }

    public Map<String,Object> toMap() {

        Map<String, Object> values = new HashMap<>();
        values.put("name", this.name);
        values.put("description", this.description);
        values.put("release_date",this.releaseDate);
        values.put("duration", this.duration);
        values.put("rating_id", this.mpa.id);
        return values;

    }
}

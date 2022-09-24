package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Set<Integer> likes = new HashSet<>();
    @NotNull
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}

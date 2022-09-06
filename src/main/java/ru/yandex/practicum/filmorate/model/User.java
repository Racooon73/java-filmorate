package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    int id;
    @Email
    String email;
    @NotBlank
    String login;
    @NotBlank
    String name;
    @Past
    LocalDate birthday;
}

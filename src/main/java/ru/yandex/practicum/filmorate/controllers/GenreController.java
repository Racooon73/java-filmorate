package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
    @GetMapping("/genres")
    public List<Genre> getGenres(){
        log.info("Получен запрос GET /genre.");
        return genreService.getGenres();
    }
    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос GET /genre/"+id);
        return genreService.getGenreById(id);
    }
}

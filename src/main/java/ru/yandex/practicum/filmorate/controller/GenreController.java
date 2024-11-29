package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.GenreDto;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final FilmDbStorage filmDbStorage;

    @Autowired
    public GenreController(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    // Получить все жанры
    @GetMapping
    public List<GenreDto> getAllGenres() {
        return filmDbStorage.getAllGenres();
    }

    // Получить жанр по ID
    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable int id) {
        return filmDbStorage.getGenreById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found with id " + id));
    }
}

package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

/**
 * REST Controller for Films class.
 */
@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    /**
     * Service for keeping films in memory.
     */
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Create a new film.
     * @param film объект фильма, который нужно создать
     * @return созданный фильм с присвоенным ID
     * @throws IllegalArgumentException если фильм не валиден
     * <p>
     * Этот метод может быть переопределен в подклассах для добавления дополнительной логики создания фильма.
     * </p>
     */
    @PostMapping
     public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        return filmService.create(film);
    }

    /**
     * Update an existing film.
     */
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        return filmService.update(film);
    }

    /**
     * Get the list of all films.
     */
    @GetMapping
    public List<Film> list() {
        return filmService.getAllFilms();
    }
}

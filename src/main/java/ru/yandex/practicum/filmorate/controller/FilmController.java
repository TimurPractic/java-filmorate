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

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    /**
     * Create a new film.
     */
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        int id = films.size() + 1;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping
    /**
     * Update an existing film.
     */
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IllegalArgumentException("Фильм с ID " + film.getId() + " не найден.");
        }
        return film;
    }

    @GetMapping
    /**
     * Get the list of all films.
     */
    public List<Film> list() {
        return new ArrayList<>(films.values());
    }
}

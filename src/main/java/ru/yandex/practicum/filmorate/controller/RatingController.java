package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class RatingController {

    private final FilmDbStorage filmDbStorage;

    @Autowired
    public RatingController(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    // Получить все рейтинги (MPA)
    @GetMapping
    public List<Rating> getAllRatings() {
        return filmDbStorage.getAllRatings();
    }

    // Получить рейтинг по ID
    @GetMapping("/{id}")
    public Rating getRatingById(@PathVariable int id) {
        return filmDbStorage.getRatingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id " + id));
    }
}

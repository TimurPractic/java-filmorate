package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    /**
     * Get a film by its ID.
     * @param id уникальный идентификатор фильма
     * @return найденный фильм
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Запрос фильма с ID: {}", id);
        return filmService.getFilmById(id);
    }

    // Лайк фильма
    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с ID {} ставит лайк фильму с ID {}", userId, id);
        try {
            filmService.addLike(id, userId);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Удаление лайка фильма
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с ID {} удаляет лайк с фильма с ID {}", userId, id);
        try {
            filmService.removeLike(id, userId);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Получение списка популярных фильмов
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение списка популярных фильмов, количество: {}", count);
        try {
            return filmService.getPopularFilms(count);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error occurred");
        }
    }







}

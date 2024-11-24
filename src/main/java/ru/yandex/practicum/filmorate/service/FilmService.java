package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /**
     * Add like.
     */
    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }
        if (user == null) {
            throw new UserNotFoundException("Юзер с ID " + userId + " не найден.");
        }
        film.getLikes().add(userId);
    }

    /**
     * Delete like.
     */
    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }
        if (user == null) {
            throw new UserNotFoundException("Юзер с ID " + userId + " не найден.");
        }
        film.getLikes().remove(userId);
    }

    /**
     * Get a list of most liked movies.
     */
    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new IllegalArgumentException("Фильм с ID " + film.getId() + " не найден.");
        }
        if (!EnumSet.allOf(Genre.class).contains(film.getGenre())) {
            throw new IllegalArgumentException("Нет такого жанра: " + film.getGenre());
        }

        if (!EnumSet.allOf(Rating.class).contains(film.getRating())) {
            throw new IllegalArgumentException("Нет такого рейтинга: " + film.getRating());
        }
        return filmStorage.update(film);
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new IllegalArgumentException("Фильм с ID " + id + " не найден.");
        }
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getAllFilms();
        return films.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteFilm(int filmId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }
        filmStorage.deleteFilm(filmId);
    }
}

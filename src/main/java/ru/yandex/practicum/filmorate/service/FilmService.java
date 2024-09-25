package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // Добавление лайка
    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new IllegalArgumentException("Film not found"));

        film.getLikes().add(userId);
    }

    // Удаление лайка
    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new IllegalArgumentException("Film not found"));

        film.getLikes().remove(userId);
    }

    // Вывод 10 наиболее популярных фильмов по количеству лайков
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
        return filmStorage.update(film);
    }

}

package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    // Удаление фильма по ID
    void deleteFilm(int filmId);

    // Получение фильма по ID
    Optional<Film> getFilmById(int filmId);

    // Получение списка всех фильмов
    List<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

}
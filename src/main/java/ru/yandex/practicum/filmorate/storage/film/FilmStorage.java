package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    // Удаление фильма по ID
    void deleteFilm(int filmId);

    // Получение списка всех фильмов
    List<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(int id);
}
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void deleteFilm(int filmId);

    List<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(int id);
}
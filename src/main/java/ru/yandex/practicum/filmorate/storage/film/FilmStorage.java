package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    // Добавление нового фильма
    Film addFilm(Film film);

    // Обновление существующего фильма
    Film updateFilm(Film film);

    // Удаление фильма по ID
    void deleteFilm(int filmId);

    // Получение фильма по ID
    Optional<Film> getFilmById(int filmId);

    // Получение списка всех фильмов
    List<Film> getAllFilms();
}
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
        // Получаем фильм по его ID
        Film film = filmStorage.getFilmById(filmId);

        if (film == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        // Добавляем лайк (ID пользователя в список лайков фильма)
        film.getLikes().add(userId);
    }

    // Удаление лайка
    public void removeLike(int filmId, int userId) {
        // Получаем фильм по его ID
        Film film = filmStorage.getFilmById(filmId);

        if (film == null) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }

        // Удаляем лайк (ID пользователя из списка лайков фильма)
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

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }


    public List<Film> getPopularFilms(int count) {
        // Получаем список всех фильмов из хранилища
        List<Film> films = filmStorage.getAllFilms();

        // Сортируем фильмы по количеству лайков в порядке убывания
        return films.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size())) // Сортировка по количеству лайков
                .limit(count) // Ограничиваем количество фильмов до заданного параметра count
                .collect(Collectors.toList());
    }
}

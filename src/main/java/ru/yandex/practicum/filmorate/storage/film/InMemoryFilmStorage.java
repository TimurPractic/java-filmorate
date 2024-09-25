package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1; // Для генерации уникальных ID

    @Override
    public Film addFilm(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Фильм с ID " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new IllegalArgumentException("Фильм с ID " + filmId + " не найден.");
        }
        films.remove(filmId);
    }

    @Override
    public Optional<Film> getFilmById(int filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}

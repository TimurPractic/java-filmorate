package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class})
class FilmDbStorageTests {

    private final FilmDbStorage filmStorage;
//    private final UserDbStorage userStorage;

    @Test
    public void testCreateFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDurationMinutes(148);
        film.setGenre(Genre.ACTION);
        film.setRating(Rating.PG13);

        filmStorage.create(film);

        Optional<Film> createdFilm = Optional.ofNullable(filmStorage.getFilmById(film.getId()));
        assertThat(createdFilm)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("name", "Inception");
                    assertThat(f).hasFieldOrPropertyWithValue("description", "A mind-bending thriller");
                    assertThat(f).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 7, 16));
                    assertThat(f).hasFieldOrPropertyWithValue("durationMinutes", 148);
                    assertThat(f).hasFieldOrPropertyWithValue("genre", Genre.ACTION);
                    assertThat(f).hasFieldOrPropertyWithValue("rating", Rating.PG13);
                });
    }

    @Test
    public void testUpdateFilm() {
        // Сначала создаем фильм.
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDurationMinutes(148);
        film.setGenre(Genre.ACTION);
        film.setRating(Rating.PG13);

        filmStorage.create(film);

        // Обновляем фильм.
        film.setName("Inception (Updated)");
        film.setDescription("A new thrilling experience");
        filmStorage.update(film);

        // Проверяем, что фильм обновлен.
        Optional<Film> updatedFilm = Optional.ofNullable(filmStorage.getFilmById(film.getId()));
        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("name", "Inception (Updated)");
                    assertThat(f).hasFieldOrPropertyWithValue("description", "A new thrilling experience");
                });
    }

    @Test
    public void testDeleteFilm() {
        // Создаем фильм для удаления.
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDurationMinutes(148);
        film.setGenre(Genre.ACTION);
        film.setRating(Rating.PG13);

        filmStorage.create(film);
        int filmId = film.getId();

        // Удаляем фильм.
        filmStorage.deleteFilm(filmId);

        // Проверяем, что фильм был удален.
        Optional<Film> deletedFilm = Optional.ofNullable(filmStorage.getFilmById(filmId));
        assertThat(deletedFilm).isNotPresent();
    }

    @Test
    public void testGetFilmById() {
        // Создаем фильм.
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDurationMinutes(148);
        film.setGenre(Genre.ACTION);
        film.setRating(Rating.PG13);

        filmStorage.create(film);

        // Получаем фильм по ID.
        Optional<Film> retrievedFilm = Optional.ofNullable(filmStorage.getFilmById(film.getId()));
        assertThat(retrievedFilm)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("id", film.getId());
                    assertThat(f).hasFieldOrPropertyWithValue("name", "Inception");
                });
    }

    @Test
    public void testGetAllFilms() {
        // Создаем несколько фильмов.
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDurationMinutes(148);
        film1.setGenre(Genre.ACTION);
        film1.setRating(Rating.PG13);
        filmStorage.create(film1);

        Film film2 = new Film();
        film2.setName("The Dark Knight");
        film2.setDescription("Batman battles Joker");
        film2.setReleaseDate(LocalDate.of(2008, 7, 18));
        film2.setDurationMinutes(152);
        film2.setGenre(Genre.ACTION);
        film2.setRating(Rating.PG13);
        filmStorage.create(film2);

        // Получаем все фильмы.
        List<Film> allFilms = filmStorage.getAllFilms();
        assertThat(allFilms).hasSize(2);
    }

    @Test
    public void testAddLikeToFilm() {
        // Создаем фильм.
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDurationMinutes(148);
        film.setGenre(Genre.ACTION);
        film.setRating(Rating.PG13);
        filmStorage.create(film);

//        // Создаем пользователя.
//        User user = new User();
//        user.setName("Test User");
//        user.setEmail("user@example.com");
//        user.setBirthday(LocalDate.of(1990, 1, 1));
//        userStorage.create(user);

        // Добавляем лайк.
        filmStorage.addOrUpdateLike(film.getId(), 1, true); // Предположим, что юзер с ID 1 ставит лайк

        // Проверяем, что лайк добавлен.
        int likeCount = filmStorage.getFilmLikeCount(film.getId());
        assertThat(likeCount).isEqualTo(1);
    }

    @Test
    public void testRemoveLikeFromFilm() {
        // Создаем фильм и добавляем лайк.
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDurationMinutes(148);
        film.setGenre(Genre.ACTION);
        film.setRating(Rating.PG13);
        filmStorage.create(film);

//        // Создаем пользователя.
//        User user = new User();
//        user.setName("Test User");
//        user.setEmail("user@example.com");
//        user.setBirthday(LocalDate.of(1990, 1, 1));
//        userStorage.create(user);

        filmStorage.addOrUpdateLike(film.getId(), 1,true); // Предположим, что юзер с ID 1 ставит лайк

        // Удаляем лайк.
        filmStorage.removeLike(film.getId(), 1);

        // Проверяем, что лайк удален.
        int likeCount = filmStorage.getFilmLikeCount(film.getId());
        assertThat(likeCount).isEqualTo(0);
    }
}

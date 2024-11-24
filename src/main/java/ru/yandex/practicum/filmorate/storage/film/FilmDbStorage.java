package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDurationMinutes(rs.getInt("duration"));
        int genreId = rs.getInt("genre_id");
        Genre genre = Genre.values()[genreId - 1];
        film.setGenre(genre);
        String ratingName = rs.getString("rating_name");
        Rating rating = Rating.valueOf(ratingName.toUpperCase());
        film.setRating(rating);
        return film;
    };

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = Genre.values()[rs.getInt("genre_id") - 1]; // Поскольку genre_id начинается с 1
        return genre;
    };

    private final RowMapper<Rating> ratingRowMapper = (rs, rowNum) -> {
            String ratingName = rs.getString("rating_name");
        return Rating.valueOf(ratingName.toUpperCase());
    };

    @Override
    public void deleteFilm(int filmId) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM film";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO film (name, description, release_date, duration, genre_id, rating_id) " +
                "VALUES (?, ?, ?, ?, " +
                "(SELECT genre_id FROM genre WHERE genre_name = ?), " +
                "(SELECT rating_id FROM rating WHERE rating_name = ?))";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre().name(),
                film.getRating().name()
        );
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, genre_id = ?, rating_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre().name(),
                film.getRating().name(),
                film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM film WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, filmRowMapper);
    }

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Добавить или обновить лайк
    public void addOrUpdateLike(int filmId, int userId, boolean liked) {
        String sql = "MERGE INTO films_likes (film_id, user_id, liked) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, filmId, userId, liked);
    }

    public int getFilmLikeCount(int filmId) {
        String sql = "SELECT COUNT(*) FROM films_likes WHERE film_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, filmId);
    }

    // Удалить лайк
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    // Получить всех пользователей, которые поставили лайк фильму
    public Set<User> getUsersWhoLikedFilm(int filmId) {
        String sql = "SELECT u.user_id, u.email, u.login, u.user_name, u.birthday " +
                "FROM films_likes fl JOIN \"user\" u ON fl.user_id = u.user_id " +
                "WHERE fl.film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("user_name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }, filmId));
    }

    // Получить все фильмы, которые понравились пользователю
    public Set<Film> getFilmsLikedByUser(int userId) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.genre_id, f.rating_name " +
                "FROM films_likes fl JOIN film f ON fl.film_id = f.film_id " +
                "WHERE fl.user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, filmRowMapper, userId));
    }

    // Метод для получения всех жанров
    public List<Genre> getAllGenres() {
        String sql = "SELECT genre_id FROM genre";
        return jdbcTemplate.query(sql, genreRowMapper);
    }

    // Метод для получения жанра по ID
    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT genre_id FROM genre WHERE genre_id = ?";
        return jdbcTemplate.query(sql, genreRowMapper, id).stream().findFirst();
    }

    // Метод для получения всех рейтингов (MPA)
    public List<Rating> getAllRatings() {
        String sql = "SELECT rating_name FROM rating";
        return jdbcTemplate.query(sql, ratingRowMapper);
    }

    // Метод для получения рейтинга по ID
    public Optional<Rating> getRatingById(int id) {
        String sql = "SELECT rating_name FROM rating WHERE rating_id = ?";
        return jdbcTemplate.query(sql, ratingRowMapper, id).stream().findFirst();
    }

}

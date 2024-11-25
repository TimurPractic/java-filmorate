package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        String durationString = rs.getString("duration");
        film.setDuration(convertTimeStringToDuration(durationString));
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
        String sql = "DELETE FROM \"film\" WHERE \"film_id\" = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT " +
                "\"film\".\"film_id\", " +
                "\"film\".\"film_name\", " +
                "\"film\".\"description\", " +
                "\"film\".\"release_date\", " +
                "\"film\".\"duration\", " +
                "\"film\".\"genre_id\", " +
                "\"rating\".\"rating_name\" " +
                "FROM \"film\" " +
                "JOIN \"rating\" ON \"film\".\"rating_id\" = \"rating\".\"rating_id\"";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO \"film\" (\"film_name\", \"description\", \"release_date\", \"duration\", \"genre_id\", \"rating_id\") " +
                "VALUES (?, ?, ?, ?, " +
                "(SELECT \"genre_id\" FROM \"genre\" WHERE \"genre_name\" = ?), " +
                "(SELECT \"rating_id\" FROM \"rating\" WHERE \"rating_name\" = ?))";

        // Выполняем запрос с RETURN_GENERATED_KEYS, чтобы получить автоматически сгенерированный film_id
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setString(4, convertDurationToTimeString(film.getDuration()));
            ps.setString(5, film.getGenre().name());
            ps.setString(6, film.getRating().name());
            return ps;
        }, keyHolder);

        // Устанавливаем автоматически сгенерированный id в объект Film
        int generatedId = keyHolder.getKey().intValue();
        film.setId(generatedId);

        return film;
    }

    @Override
    public Film update(Film film) {
        String durationString = convertDurationToTimeString(film.getDuration());
        String sql = "UPDATE \"film\" SET \"film_name\" = ?, \"description\" = ?, \"release_date\" = ?, \"duration\" = ?, \"genre_id\" = ?, \"rating_id\" = ? WHERE \"film_id\" = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                durationString,
                film.getDuration(),
                film.getGenre().name(),
                film.getRating().name(),
                film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT " +
                "\"film\".\"film_id\", " +
                "\"film\".\"film_name\", " +
                "\"film\".\"description\", " +
                "\"film\".\"release_date\", " +
                "\"film\".\"duration\", " +
                "\"film\".\"genre_id\", " +
                "\"film\".\"rating_id\", " +
                "\"rating\".\"rating_name\" " +
                "FROM \"film\" " +
                "JOIN \"rating\" ON \"film\".\"rating_id\" = \"rating\".\"rating_id\" " +
                "WHERE \"film\".\"film_id\" = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, filmRowMapper);
    }

    // Добавить или обновить лайк
    public void addOrUpdateLike(int filmId, int userId, boolean liked) {
        String checkUserSql = "SELECT COUNT(*) FROM \"user\" WHERE \"user_id\" = ?";
        String checkFilmSql = "SELECT COUNT(*) FROM \"film\" WHERE \"film_id\" = ?";

        Integer userExists = jdbcTemplate.queryForObject(checkUserSql, Integer.class, userId);
        Integer filmExists = jdbcTemplate.queryForObject(checkFilmSql, Integer.class, filmId);

        if (userExists == null || userExists == 0) {
            throw new IllegalArgumentException("User with id " + userId + " does not exist.");
        }

        if (filmExists == null || filmExists == 0) {
            throw new IllegalArgumentException("Film with id " + filmId + " does not exist.");
        }

        String sql = "MERGE INTO \"films_likes\" (\"film_id\", \"user_id\", \"liked\") VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, filmId, userId, liked);
    }


    public int getFilmLikeCount(int filmId) {
        String sql = "SELECT COUNT(*) FROM \"films_likes\" WHERE \"film_id\" = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, filmId);
    }

    // Удалить лайк
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM \"films_likes\" WHERE \"film_id\" = ? AND \"user_id\" = ?";
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
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.genre_id, f.rating_name FROM films_likes fl JOIN film f ON fl.film_id = f.film_id WHERE fl.user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, filmRowMapper, userId));
    }

    // Метод для получения всех жанров
    public List<Genre> getAllGenres() {
        String sql = "SELECT \"genre_id\" FROM \"genre\"";
        return jdbcTemplate.query(sql, genreRowMapper);
    }

    // Метод для получения жанра по ID
    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT \"genre_id\" FROM \"genre\" WHERE \"genre_id\" = ?";
        return jdbcTemplate.query(sql, genreRowMapper, id).stream().findFirst();
    }

    // Метод для получения всех рейтингов (MPA)
    public List<Rating> getAllRatings() {
        String sql = "SELECT \"rating_name\" FROM \"rating\"";
        return jdbcTemplate.query(sql, ratingRowMapper);
    }

    // Метод для получения рейтинга по ID
    public Optional<Rating> getRatingById(int id) {
        String sql = "SELECT \"rating_name\" FROM \"rating\" WHERE \"rating_id\" = ?";
        return jdbcTemplate.query(sql, ratingRowMapper, id).stream().findFirst();
    }
    private String convertDurationToTimeString(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private Duration convertTimeStringToDuration(String timeString) {
        String[] parts = timeString.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }
}

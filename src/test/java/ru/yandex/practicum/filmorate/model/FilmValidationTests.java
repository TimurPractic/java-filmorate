package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FilmValidationTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidFilm_thenNoConstraintViolations() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Film");
        film.setDescription("A valid description that is within the 200 characters limit.");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(120));

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenEmptyName_thenConstraintViolation() {
        Film film = new Film();
        film.setId(1);
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(120));

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Название не может быть пустым"))).isTrue();
    }

    @Test
    void whenDescriptionTooLong_thenConstraintViolation() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Film");
        film.setDescription("A".repeat(201)); // Длинное описание
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(120));

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Максимальная длина описания — 200 символов"))).isTrue();
    }

    @Test
    void whenReleaseDateTooEarly_thenConstraintViolation() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // Слишком ранняя дата
        film.setDuration(Duration.ofMinutes(120));

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Дата релиза не может быть раньше 28 декабря 1895 года"))).isTrue();
    }

    @Test
    void whenNegativeDuration_thenConstraintViolation() {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(-120)); // Отрицательная продолжительность

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Продолжительность должна быть положительным числом"))).isTrue();
    }

    @Test
    void whenEmptyFilm_thenConstraintViolations() {
        Film film = new Film();

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Название не может быть пустым"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Дата релиза не может быть раньше 28 декабря 1895 года"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Продолжительность должна быть положительным числом"))).isTrue();
    }
}

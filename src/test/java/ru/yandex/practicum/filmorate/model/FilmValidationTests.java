//package ru.yandex.practicum.filmorate.model;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//class FilmValidationTests {
//
//    @Autowired
//    private Validator validator;
//
//    @BeforeEach
//    void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    void whenValidFilm_thenNoConstraintViolations() {
//        Film film = new Film();
//        film.setId(1);
//        film.setName("Valid Film");
//        film.setDescription("A valid description that is within the 200 characters limit.");
//        film.setReleaseDate(LocalDate.of(1895, 12, 28));
//        film.setDuration(Duration.ofMinutes(120));
//
//        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
//
//        assertThat(violations).isEmpty();
//    }
//
//    @Test
//    void whenEmptyName_thenConstraintViolation() {
//        Film film = new Film();
//        film.setId(1);
//        film.setName("");
//        film.setDescription("Valid description");
//        film.setReleaseDate(LocalDate.of(1895, 12, 28));
//        film.setDuration(Duration.ofMinutes(120));
//
//        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Название фильма не может быть пустым"))).isTrue();
//    }
//
//    @Test
//    void whenDescriptionTooLong_thenConstraintViolation() {
//        Film film = new Film();
//        film.setId(1);
//        film.setName("Valid Film");
//        film.setDescription("A".repeat(201));
//        film.setReleaseDate(LocalDate.of(1895, 12, 28));
//        film.setDuration(Duration.ofMinutes(120));
//
//        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Описание не может содержать более 200 символов"))).isTrue();
//    }
//
//    @Test
//    void whenReleaseDateTooEarly_thenConstraintViolation() {
//        Film film = new Film();
//        film.setId(1);
//        film.setName("Valid Film");
//        film.setDescription("Valid description");
//
//        ValidationException exception = assertThrows(ValidationException.class, () -> {
//            film.setReleaseDate(LocalDate.of(1800, 1, 1)); // Слишком ранняя дата
//        });
//        assertThat(exception.getMessage()).contains("Дата релиза не может быть раньше 28 декабря 1895 года");
//        film.setReleaseDate(LocalDate.of(1896, 1, 1));
//        film.setDuration(Duration.ofMinutes(120));
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        assertThat(violations).isEmpty();
//    }
//
//    @Test
//    void whenNegativeDuration_thenConstraintViolation() {
//        Film film = new Film();
//        film.setId(1);
//        film.setName("Valid Film");
//        film.setDescription("Valid description");
//        film.setReleaseDate(LocalDate.of(1895, 12, 28));
//        film.setDuration(Duration.ofMinutes(-120));
//
//        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Продолжительность должна быть положительным числом"))).isTrue();
//    }
//}

//package ru.yandex.practicum.filmorate.model;
//
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class UserValidationTests {
//
//    private Validator validator;
//
//    @BeforeEach
//    void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    void whenValidUser_thenNoConstraintViolations() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail("user@example.com");
//        user.setLogin("user123");
//        user.setName("User Name");
//        user.setBirthday(LocalDate.of(2000, 1, 1));
//
//        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
//
//        assertThat(violations).isEmpty();
//    }
//
//    @Test
//    void whenEmptyEmail_thenConstraintViolation() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail(""); // Пустой email
//        user.setLogin("user123");
//        user.setName("User Name");
//        user.setBirthday(LocalDate.of(2000, 1, 1));
//
//        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Email не может быть пустым"))).isTrue();
//    }
//
//    @Test
//    void whenEmailWithoutAt_thenConstraintViolation() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail("userexample.com"); // Email без @
//        user.setLogin("user123");
//        user.setName("User Name");
//        user.setBirthday(LocalDate.of(2000, 1, 1));
//
//        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Некорректный формат email"))).isTrue();
//    }
//
//    @Test
//    void whenEmptyLogin_thenConstraintViolation() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail("user@example.com");
//        user.setLogin(""); // Пустой login
//        user.setName("User Name");
//        user.setBirthday(LocalDate.of(2000, 1, 1));
//
//        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Логин не может быть пустым"))).isTrue();
//    }
//
//    @Test
//    void whenLoginWithSpaces_thenConstraintViolation() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail("user@example.com");
//        user.setLogin("user 123"); // Логин с пробелами
//        user.setName("User Name");
//        user.setBirthday(LocalDate.of(2000, 1, 1));
//
//        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Логин не может содержать пробелы"))).isTrue();
//    }
//
//    @Test
//    void whenFutureBirthday_thenConstraintViolation() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail("user@example.com");
//        user.setLogin("user123");
//        user.setName("User Name");
//        user.setBirthday(LocalDate.of(3000, 1, 1)); // Дата рождения в будущем
//
//        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
//
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Дата рождения не может быть в будущем"))).isTrue();
//    }
//}

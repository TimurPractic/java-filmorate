package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * Class for modeling User data model.
 */
@Entity
@Table(name = "\"user\"")
@Data
public class User {
    /**
     * USer identical number.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    /**
     * User e-mail.
     */
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * User login.
     */
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^\\s]*$", message = "Логин не может содержать пробелы")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * User name.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * User birthday date.
     */
    @Past(message = "Дата рождения не может быть в будущем")
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    /**
     * Method for proper getting user's name.
     */
    public String getName() {
        return (name != null && !name.isBlank()) ? name : login;
    }

}

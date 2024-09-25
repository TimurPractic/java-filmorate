package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for modeling User data model.
 */
@Data
public class User {
    /**
     * USer identical number.
     */
    private int id;

    /**
     * User e-mail.
     */
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    /**
     * User login.
     */
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^\\s]*$", message = "Логин не может содержать пробелы")
    private String login;

    /**
     * User name.
     */
    private String name;

    /**
     * User birthday date.
     */
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    /**
     * Method for proper getting user's name.
     */
    public String getName() {
        return (name != null && !name.isBlank()) ? name : login;
    }

}

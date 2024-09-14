package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

/**
 * User.
 */
@Data
public class User {
    /**
     * Java-doc filler.
     */
    private int id;

    /**
     * Java-doc filler.
     */
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    /**
     * Java-doc filler.
     */
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^\\s]*$", message = "Логин не может содержать пробелы")
    private String login;
    /**
     * Java-doc filler.
     */
    private String name;

    /**
     * Java-doc filler.
     */
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    /**
     * Java-doc filler.
     */
    public String getName() {
        return (name != null && !name.isBlank()) ? name : login;
    }
}

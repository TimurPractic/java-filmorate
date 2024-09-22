package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import ru.yandex.practicum.filmorate.validation.ValidDuration;
import jakarta.validation.constraints.PastOrPresent;

/**
 * Class for modeling Film data model.
 */
@Data
public class Film {
    /**
     * Film identical number.
     */
    private int id;

    /**
     * Film name.
     */
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    /**
     * Film description.
     */
    @Size(max = 200, message = "Описание не может содержать более 200 символов")
    private String description;

    /**
     * Date of film release.
     */
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    /**
     * Duration of the film.
     */
    @ValidDuration
    private Duration duration;

    /**
     * Method for proper setting of release date including validation of rule "not before cinema invention".
     */
    public void setReleaseDate(LocalDate releaseDate) {
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (releaseDate.isBefore(earliestReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        this.releaseDate = releaseDate;
    }

    /**
     * Method for proper getting of film duration.
     */
    @JsonGetter("duration")
    public int getDurationMinutes() {
        return (int) duration.toMinutes();
    }

    /**
     * Method for proper setting of film duration.
     */
    @JsonSetter("duration")
    public void setDurationMinutes(int minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

}

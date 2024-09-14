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
 * Java-doc filler.
 */
@Data
public class Film {
    /**
     * Java-doc filler.
     */
    private int id;

    /**
     * Java-doc filler.
     */
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    /**
     * Java-doc filler.
     */
    @Size(max = 200, message = "Описание не может содержать более 200 символов")
    private String description;

    /**
     * Java-doc filler.
     */
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    /**
     * Java-doc filler.
     */
    @ValidDuration
    private Duration duration;

    /**
     * Java-doc filler.
     */
    public void setReleaseDate(LocalDate releaseDate) {
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (releaseDate.isBefore(earliestReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        this.releaseDate = releaseDate;
    }

    /**
     * Java-doc filler.
     */
    @JsonGetter("duration")
    public int getDurationMinutes() {
        return (int) duration.toMinutes();
    }

    /**
     * Java-doc filler.
     */
    @JsonSetter("duration")
    public void setDurationMinutes(int minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

}

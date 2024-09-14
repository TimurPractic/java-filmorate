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

@Data
public class Film {
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может содержать более 200 символов")
    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    @ValidDuration
    private Duration duration;

    public void setReleaseDate(LocalDate releaseDate) {
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (releaseDate.isBefore(earliestReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        this.releaseDate = releaseDate;
    }

    @JsonGetter("duration")
    public int getDurationMinutes() {
        return (int) duration.toMinutes();
    }

    @JsonSetter("duration")
    public void setDurationMinutes(int minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

}

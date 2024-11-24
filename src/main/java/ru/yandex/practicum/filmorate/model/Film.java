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
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

/**
 * Class for modeling Film data model.
 */
@Data
@Entity
@Table(name = "film")
public class Film {
    /**
     * Film identical number.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private int id;

    /**
     * Film name.
     */
    @NotBlank(message = "Название фильма не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Film description.
     */
    @Size(max = 200, message = "Описание не может содержать более 200 символов")
    @Column(name = "description")
    private String description;

    /**
     * Date of film release.
     */
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    /**
     * Duration of the film.
     */
    @ValidDuration
    @Column(name = "duration", nullable = false)
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "rating_id", referencedColumnName = "rating_id")
    private Rating rating;

    private Set<Integer> likes = new HashSet<>();

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

package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.yandex.practicum.filmorate.service.GenreDeserializer;
import ru.yandex.practicum.filmorate.service.GenreSerializer;

@JsonSerialize(using = GenreSerializer.class)
@JsonDeserialize(using = GenreDeserializer.class)
public enum Genre {
    Комедия(1),
    Драма(2),
    Мультфильм(3),
    Триллер(4),
    Документальный(5),
    Боевик(6);

    private final int id;

    Genre(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Genre fromId(int id) {
        for (Genre genre : Genre.values()) {
            if (genre.id == id) {
                return genre;
            }
        }
        throw new IllegalArgumentException("No genre with id: " + id);
    }
}

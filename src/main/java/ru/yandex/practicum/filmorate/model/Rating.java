package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.yandex.practicum.filmorate.service.RatingDeserializer;
import ru.yandex.practicum.filmorate.service.RatingSerializer;

@JsonSerialize(using = RatingSerializer.class)
@JsonDeserialize(using = RatingDeserializer.class)
public enum Rating {
    G("G", 1),
    PG("PG", 2),
    PG13("PG-13", 3),
    R("R", 4),
    NC17("NC-17", 5);

    private final String displayName;
    private final int id;

    Rating(String displayName, int id) {
        this.displayName = displayName;
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public static Rating fromId(int id) {
        for (Rating rating : Rating.values()) {
            if (rating.getId() == id) {
                return rating;
            }
        }
        throw new IllegalArgumentException("No enum constant with id " + id);
    }

    public static Rating fromDisplayName(String displayName) {
        // Исправляем PG13 на PG-13
        if (displayName.equalsIgnoreCase("PG13")) {
            displayName = "PG-13";
        }
        if (displayName.equalsIgnoreCase("NC17")) {
            displayName = "NC-17";
        }
        for (Rating rating : Rating.values()) {
            if (rating.displayName.equalsIgnoreCase(displayName)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + displayName);
    }
}

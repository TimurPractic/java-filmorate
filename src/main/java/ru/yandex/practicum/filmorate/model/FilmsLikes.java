package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmsLikes {
    private int id;

    private Film film;

    private User user;

    private Boolean liked;

    private int filmId; // film_id в таблице
    private int userId; // user_id в таблице
}

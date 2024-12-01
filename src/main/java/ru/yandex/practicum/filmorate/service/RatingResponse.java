package ru.yandex.practicum.filmorate.service;

public class RatingResponse {
    private int id;         // ID рейтинга
    private String name;    // Имя рейтинга

    public RatingResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

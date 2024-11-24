package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UsersFriends {
    private int id;

    private User firstUser;

    private User secondUser;

    private Friendship friendshipStatus;
}

package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    /**
     * Create a new user.
     */
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        int id = users.size() + 1;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping
    /**
     * Update an existing user.
     */
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IllegalArgumentException("Пользователь с ID " + user.getId() + " не найден.");
        }
        return user;
    }

    @GetMapping
    /**
     * Get the list of all users.
     */
    public List<User> list() {
        return new ArrayList<>(users.values());
    }
}

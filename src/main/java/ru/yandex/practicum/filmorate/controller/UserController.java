package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.List;

import ru.yandex.practicum.filmorate.service.UserService;

/**
 * REST Controller for User class.
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user.
     */
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        return userService.create(user);
    }

    /**
     * Update an existing user.
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        return userService.update(user);
    }

    /**
     * Get the list of all users.
     */
    @GetMapping
    public List<User> list() {
        return userService.getAllUsers();
    }
}

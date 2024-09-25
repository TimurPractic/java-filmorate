package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    // Добавление нового пользователя
    User create(User user);

    // Обновление существующего пользователя
    User update(User user);

    // Удаление пользователя по ID
    void deleteUser(int userId);

    // Получение пользователя по ID
    Optional<User> getUserById(int userId);

    // Получение списка всех пользователей
    List<User> getAllUsers();

}
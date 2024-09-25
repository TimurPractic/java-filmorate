package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // Добавление в друзья
    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User friend = userStorage.getUserById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    // Удаление из друзей
    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User friend = userStorage.getUserById(friendId).orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    // Вывод списка общих друзей
    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User otherUser = userStorage.getUserById(otherUserId).orElseThrow(() -> new IllegalArgumentException("Other user not found"));

        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());

        return commonFriendsIds.stream()
                .map(id -> userStorage.getUserById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();

    }
    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }
}

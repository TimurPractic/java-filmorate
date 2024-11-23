package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final String unf = "User not found";

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Add to friends.
     */
    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);  // Просто получаем пользователя
        User friend = userStorage.getUserById(friendId);

        if (user == null) {
            throw new IllegalArgumentException(unf);
        }
        if (friend == null) {
            throw new IllegalArgumentException("Friend not found");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    /**
     * Remove from friends.
     */
    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null) {
            throw new IllegalArgumentException(unf);
        }
        if (friend == null) {
            throw new IllegalArgumentException("Friend not found");
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    /**
     * Show mutual friends.
     */
    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);
        if (user == null) {
            throw new IllegalArgumentException(unf);
        }
        if (otherUser == null) {
            throw new IllegalArgumentException("Other user not found");
        }
        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());
        return commonFriendsIds.stream()
                .map(id -> userStorage.getUserById(id))
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
        if (userStorage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден.");
        }
        return userStorage.update(user);
    }


    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден.");
        }
        return user;
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден.");
        }
        Set<Integer> friendIds = user.getFriends();
        if (friendIds == null || friendIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendIds) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public void deleteUser(int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        userStorage.deleteUser(userId);
    }
}

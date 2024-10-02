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
        List<User> allUsers = userStorage.getAllUsers();
        List<Integer> idsOfUsers = new ArrayList<>();
        for(User i : allUsers) {
            idsOfUsers.add(i.getId());
        }
         if (!idsOfUsers.contains(user.getId())){
            throw new UserNotFoundException("User with id " + user.getId() + " not found.");
        }
        return userStorage.update(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
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
}

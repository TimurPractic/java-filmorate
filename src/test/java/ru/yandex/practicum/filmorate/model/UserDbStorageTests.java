package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbStorageTests {

    private final UserDbStorage userStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        if (jdbcTemplate == null) {
            throw new IllegalStateException("JdbcTemplate is not properly initialized.");
        }
    }

    @BeforeEach
    void setupTestData() {
        // Удаляем данные перед тестами
        jdbcTemplate.update("DELETE FROM \"users_friends\""); // Удаляем связи дружбы
        jdbcTemplate.update("DELETE FROM \"user\"");         // Удаляем пользователей

        // Создаем пользователя 1
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setLogin("boobies");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        userStorage.create(user1);

        // Создаем пользователя 2
        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setLogin("boobies2");
        user2.setBirthday(LocalDate.of(1995, 5, 15));
        userStorage.create(user2);
    }



    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testProposeFriendship() {
        // Предположим, что пользователи с ID 1 и 2 существуют.
        userStorage.proposeFriendship(1, 2);

        // Проверим, что запрос в базу данных прошел успешно, и дружба предложена.
        Friendship status = userStorage.getFriendshipStatus(1, 2);
        assertThat(status).isEqualTo(Friendship.NOT_CONFIRMED);
    }

    @Test
    public void testConfirmFriendship() {

        // Предположим, что дружба между пользователями с ID 1 и 2 предложена.
        userStorage.proposeFriendship(1, 2);

        // Теперь подтверждаем дружбу.
        userStorage.confirmFriendship(1, 2);

        // Проверим, что статус дружбы обновился.
        Friendship status = userStorage.getFriendshipStatus(1, 2);
        assertThat(status).isEqualTo(Friendship.CONFIRMED);
    }

    @Test
    public void testGetFriends() {
        // Добавляем двух пользователей в дружбу.
        userStorage.proposeFriendship(1, 2);
        userStorage.proposeFriendship(1, 3);
        userStorage.confirmFriendship(1, 2);
        userStorage.confirmFriendship(1, 3);

        // Получаем список друзей для пользователя с ID 1.
        List<User> friends = userStorage.getFriends(1);

        assertThat(friends)
                .hasSize(2)  // Ожидаем 2 друга
                .extracting("id")
                .containsExactlyInAnyOrder(2, 3);  // ID друзей 2 и 3
    }

    @Test
    public void testGetFriendshipStatus() {
        // Добавляем пользователей 1 и 2 в список друзей.
        userStorage.proposeFriendship(1, 2);
        Friendship statusBefore = userStorage.getFriendshipStatus(1, 2);
        assertThat(statusBefore).isEqualTo(Friendship.NOT_CONFIRMED);

        // Подтверждаем дружбу.
        userStorage.confirmFriendship(1, 2);
        Friendship statusAfter = userStorage.getFriendshipStatus(1, 2);
        assertThat(statusAfter).isEqualTo(Friendship.CONFIRMED);
    }
}
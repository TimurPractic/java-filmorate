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

        // Создаем пользователей, если они не существуют
        createUserIfNotExists(1, "John Doe", "john.doe@example.com", "boobies", LocalDate.of(1990, 1, 1));
        createUserIfNotExists(2, "Jane Smith", "jane.smith@example.com", "boobies2", LocalDate.of(1995, 5, 15));
        createUserIfNotExists(3, "Aaron Smith", "aaron.smith@example.com", "boobies3", LocalDate.of(1996, 5, 25));

    }

    // Метод для создания пользователя, если он не существует
    private void createUserIfNotExists(int userId, String name, String email, String login, LocalDate birthday) {
        String checkQuery = "SELECT COUNT(*) FROM \"user\" WHERE \"user_id\" = ?";
        int count = jdbcTemplate.queryForObject(checkQuery, new Object[]{userId}, Integer.class);

        if (count == 0) {
            String insertQuery = "INSERT INTO \"user\" (\"user_id\", \"user_name\", \"email\", \"login\", \"birthday\") VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertQuery, userId, name, email, login, birthday);
        }
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
        List<User> friendslist = userStorage.getFriends(1);

        assertThat(friendslist)
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
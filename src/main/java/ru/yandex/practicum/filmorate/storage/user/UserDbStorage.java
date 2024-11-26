package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
public class UserDbStorage implements UserStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setName(rs.getString("user_name"));
            user.setEmail(rs.getString("email"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }
    };

    @Override
    public User create(User user) {
        String sql = "INSERT INTO \"user\" (\"user_name\", \"email\", \"birthday\", \"login\") VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getLogin());  // Добавляем login
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE \"user\" SET \"user_name\" = ?, \"email\" = ?, \"birthday\" = ?, \"login\" = ? WHERE \"user_id\" = ?";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getLogin(),  // Добавляем login
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        String sql = "DELETE FROM \"user\" WHERE \"user_id\" = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM \"user\"";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM \"user\" WHERE \"user_id\" = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{id+1}, userRowMapper).stream()
                    .findFirst()
                    .orElseThrow(() -> new EmptyResultDataAccessException("No user found with id: " + id, 1));
        } catch (DataAccessException e) {
            throw new RuntimeException("Error retrieving user with id: " + id, e);
        }
    }

    // Метод для предложения дружбы
    public void proposeFriendship(int firstUserId, int secondUserId) {
        // Проверим, существуют ли оба пользователя
        String checkUserQuery = "SELECT COUNT(*) FROM \"user\" WHERE \"user_id\" = ?";
        int firstUserCount = jdbcTemplate.queryForObject(checkUserQuery, new Object[]{firstUserId}, Integer.class);
        int secondUserCount = jdbcTemplate.queryForObject(checkUserQuery, new Object[]{secondUserId}, Integer.class);

        if (firstUserCount > 0 && secondUserCount > 0) {
            String query = "INSERT INTO \"users_friends\" (\"first_user_id\", \"second_user_id\", \"friendship_status\") " +
                    "VALUES (?, ?, ?)";
            jdbcTemplate.update(query, firstUserId, secondUserId, Friendship.NOT_CONFIRMED.name());
            String query2 = "SELECT COUNT (*) FROM \"users_friends\" ";
            Integer query33 = jdbcTemplate.queryForObject(query2, Integer.class);
            System.out.println(query33);
        } else {
            throw new IllegalArgumentException("One or both users do not exist.");
        }
    }

    // Метод для подтверждения дружбы
    public void confirmFriendship(int firstUserId, int secondUserId) {
        String query = "UPDATE \"users_friends\" SET \"friendship_status\" = ? " +
                "WHERE \"first_user_id\" = ? AND \"second_user_id\" = ?";
        jdbcTemplate.update(query, Friendship.CONFIRMED.name(), firstUserId, secondUserId);
    }

    // Метод для получения списка друзей пользователя (с подтвержденным статусом)
    public List<User> getFriends(int userId) {
        String query = "SELECT DISTINCT \"user\".\"user_id\", \"user\".\"email\", \"user\".\"login\", \"user\".\"user_name\", \"user\".\"birthday\" " +
                "FROM \"users_friends\" " +
                "JOIN \"user\" ON (" +
                "   (\"users_friends\".\"second_user_id\" = \"user\".\"user_id\" AND \"users_friends\".\"first_user_id\" = ?) " +
                "   OR " +
                "   (\"users_friends\".\"first_user_id\" = \"user\".\"user_id\" AND \"users_friends\".\"second_user_id\" = ?) " +
                ") " +
                "WHERE \"users_friends\".\"friendship_status\" = ?";
        return jdbcTemplate.query(query, new Object[]{userId, userId, Friendship.CONFIRMED.name()}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User friend = new User();
                friend.setId(rs.getInt("user_id"));
                friend.setEmail(rs.getString("email"));
                friend.setLogin(rs.getString("login"));
                friend.setName(rs.getString("user_name"));
                friend.setBirthday(rs.getDate("birthday").toLocalDate());
                return friend;
            }
        });
    }

    // Метод для получения статуса дружбы между двумя пользователями
    public Friendship getFriendshipStatus(int firstUserId, int secondUserId) {
        String query = "SELECT \"friendship_status\" FROM \"users_friends\" " +
                "WHERE (\"first_user_id\" = ? AND \"second_user_id\" = ?) OR (\"first_user_id\" = ? AND \"second_user_id\" = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{firstUserId, secondUserId, secondUserId, firstUserId},
                (rs, rowNum) -> Friendship.valueOf(rs.getString("friendship_status")));
    }
}

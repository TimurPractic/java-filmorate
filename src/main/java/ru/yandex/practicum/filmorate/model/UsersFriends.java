package ru.yandex.practicum.filmorate.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Table(name = "users_friends")
@Data
public class UsersFriends {
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @MapsId("firstUserId")
    @JoinColumn(name = "first_user_id")
    private User firstUser;

    @ManyToOne
    @MapsId("secondUserId")
    @JoinColumn(name = "second_user_id")
    private User secondUser;

    @Column(name = "friendship_status")
    private String friendshipStatus;
}

CREATE TABLE IF NOT EXISTS genre(
    genre_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE rating(
    rating_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS film(
    film_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE NOT NULL,
    duration TIME NOT NULL,
    genre_id INTEGER REFERENCES genre(genre_id),
    rating_id INTEGER,
    FOREIGN KEY (rating_id) REFERENCES rating(rating_id)
);

CREATE TABLE IF NOT EXISTS user(
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100),
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS films_likes(
    film_id INTEGER REFERENCES film(film_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES "user"(user_id) ON DELETE CASCADE,
    liked BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS users_friends(
    first_user_id INTEGER REFERENCES "user"(user_id) ON DELETE CASCADE,
    second_user_id INTEGER REFERENCES "user"(user_id) ON DELETE CASCADE,
    friendship_status VARCHAR(50),
    PRIMARY KEY (first_user_id, second_user_id)
);




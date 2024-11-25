
CREATE TABLE IF NOT EXISTS "genre" (
    "genre_id" INT PRIMARY KEY,
    "genre_name" VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "rating" (
    "rating_id" INT PRIMARY KEY,
    "rating_name" VARCHAR(10) NOT NULL
);


CREATE TABLE IF NOT EXISTS "film"(
    "film_id" SERIAL PRIMARY KEY,
    "film_name" VARCHAR(255) NOT NULL,
    "description" TEXT,
    "release_date" DATE NOT NULL,
    "duration" TIME NOT NULL,
    "genre_id" INTEGER REFERENCES "genre"("genre_id"),
    "rating_id" INTEGER,
    FOREIGN KEY ("rating_id") REFERENCES "rating"("rating_id")
);

CREATE TABLE IF NOT EXISTS "user"(
    "user_id" SERIAL PRIMARY KEY,
    "email" VARCHAR(255) UNIQUE NOT NULL,
    "login" VARCHAR(50) UNIQUE NOT NULL,
    "user_name" VARCHAR(100),
    "birthday" DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS "films_likes"(
    "film_id" INTEGER REFERENCES "film"("film_id") ON DELETE CASCADE,
    "user_id" INTEGER REFERENCES "user"("user_id") ON DELETE CASCADE,
    "liked" BOOLEAN DEFAULT TRUE,
    PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE IF NOT EXISTS "users_friends"(
    "first_user_id" INTEGER REFERENCES "user"("user_id") ON DELETE CASCADE,
    "second_user_id" INTEGER REFERENCES "user"("user_id") ON DELETE CASCADE,
    "friendship_status" VARCHAR(50),
    PRIMARY KEY ("first_user_id", "second_user_id")
);


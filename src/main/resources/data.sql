-- Вставка данных в таблицу genre
INSERT INTO "genre" ("genre_id", "genre_name")
VALUES
(1, 'ACTION'),
(2, 'DRAMA'),
(3, 'COMEDY'),
(4, 'HORROR'),
(5, 'ROMANCE'),
(6, 'CARTOON'),
(7, 'THRILLER'),
(8, 'DOCUMENTARY');

-- Вставка данных в таблицу rating
INSERT INTO "rating" ("rating_id", "rating_name")
VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG13'),
(4, 'R'),
(5, 'NC17');

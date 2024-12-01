-- Вставка данных в таблицу genre
DELETE FROM "genre";
INSERT INTO "genre"
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

-- Вставка данных в таблицу rating
DELETE FROM "rating";
INSERT INTO "rating"
VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG13'),
(4, 'R'),
(5, 'NC17');

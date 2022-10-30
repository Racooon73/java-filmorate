DELETE from FRIENDS;
DELETE  FROM USERS ;

DELETE FROM likes;

DELETE FROM FILM_GENRE;
DELETE FROM FILMS;
DELETE FROM RATINGS;

ALTER TABLE FILMS ALTER COLUMN id RESTART WITH 1;

ALTER TABLE USERS ALTER COLUMN id RESTART WITH 1;
merge into RATINGS (RATING_ID, RATING_NAME) values (1,'G' ),(2,'PG'),(3,'PG-13'),(4,'M'),(5, 'NC-17');
merge into GENRE (GENRE_ID,GENRE_NAME) values ( 1,'Комедия'), (2,'Драма'),(3,'Мультфильм'),( 4,'Триллер'),
                                              (5,'Ужасы'),(6,'Документальный');
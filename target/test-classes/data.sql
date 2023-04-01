INSERT INTO USERS (email, login, name, birthday) 
	VALUES ('user1@yandex.com','user1','user1','2005-05-10');
INSERT INTO USERS (email, login, name, birthday) 
	VALUES ('user2@yandex.com','user2','user2','2005-07-14');
INSERT INTO USERS (email, login, name, birthday) 
	VALUES ('user3@yandex.com','user3','user3','2005-09-18');

INSERT INTO FRIENDS (userId, friendId) VALUES (1,2);
INSERT INTO FRIENDS (userId, friendId) VALUES (1,3);
INSERT INTO FRIENDS (userId, friendId) VALUES (2,3);

INSERT INTO FILMS (name, description, releaseDate, duration) VALUES 
	('film 1','description 1','2022-01-21',90);
INSERT INTO FILMS (name, description, releaseDate, duration) VALUES 
	('film 2','description 2','2022-02-11',110);

INSERT INTO LIKES (filmId, userId) VALUES (1,2);
INSERT INTO LIKES (filmId, userId) VALUES (2,1);
INSERT INTO LIKES (filmId, userId) VALUES (2,3);

INSERT INTO FILMGENRES (filmId, genreId) VALUES (1, 3);
INSERT INTO FILMGENRES (filmId, genreId) VALUES (1, 1);

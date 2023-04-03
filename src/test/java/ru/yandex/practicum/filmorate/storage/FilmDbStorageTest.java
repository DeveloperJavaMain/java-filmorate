package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage storage;

    @Test
    void getAll() {
        List<Film> list = storage.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void getById() {
        Film film = storage.getById(1);
        assertNotNull(film);
        assertEquals(1, film.getId());
        assertThrows(NotFoundException.class, () -> {
            storage.getById(100);
        });
    }

    @Test
    void insert() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("new film");
        film.setReleaseDate(LocalDate.of(2023, 3, 20));
        film.setDuration(95);

        Film newFilm = storage.insert(film);

        film = storage.getById(newFilm.getId());
        assertEquals(newFilm, film);
        storage.delete(film);
    }

    @Test
    void update() {
        Film film = storage.getById(2);
        String name = film.getName();
        film.setName("updated film");
        film.getLikes().add(1);
        storage.update(film);

        Film updFilm = storage.getById(film.getId());
        assertEquals(film, updFilm);
        film.setName(name);
        storage.update(film);
    }

    @Test
    void delete() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("new film");
        film.setReleaseDate(LocalDate.of(2023, 3, 20));
        film.setDuration(95);

        Film newFilm = storage.insert(film);

        film = storage.getById(newFilm.getId());
        assertEquals(newFilm, film);

        storage.delete(film);
        int id = film.getId();
        assertThrows(NotFoundException.class, () -> {
            storage.getById(id);
        });
    }
}
package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class GenreDbStorageTest {

    @Autowired
    private GenreStorage storage;

    @Test
    void getAll() {
        List<Genre> list = storage.getAll();
        assertNotNull(list);
        assertEquals(6, list.size());
    }

    @Test
    void getByFilm() {
        List<Genre> list = storage.getByFilm(1);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void getById() {
        Genre genre = storage.getById(3);
        assertNotNull(genre);
        assertEquals("Мультфильм", genre.getName());

        assertThrows(NotFoundException.class, ()-> {storage.getById(100);});
    }

    @Test
    void insert() {
        Genre genre = new Genre();
        genre.setName("Новинки");
        storage.insert(genre);
        Genre newGenre = storage.getById(genre.getId());
        assertEquals(genre, newGenre);
        storage.delete(newGenre);
    }

    @Test
    void update() {
        Genre genre = storage.getById(2);
        String name = genre.getName();
        genre.setName("Новинки");
        storage.update(genre);
        Genre updGenre = storage.getById(genre.getId());
        assertEquals(genre, updGenre);
        genre.setName(name);
        storage.update(genre);
    }

    @Test
    void delete() {
        Genre genre = new Genre();
        genre.setName("Новинки");
        storage.insert(genre);
        Genre newGenre = storage.getById(genre.getId());
        assertEquals(genre, newGenre);
        newGenre = storage.delete(newGenre);
        assertEquals(genre, newGenre);
        int id = newGenre.getId();
        assertThrows(NotFoundException.class, ()->{storage.getById(id);});
    }
}
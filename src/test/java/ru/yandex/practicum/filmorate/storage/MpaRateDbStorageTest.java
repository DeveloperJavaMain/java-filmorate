package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class MpaRateDbStorageTest {

    @Autowired
    private MpaRateStorage storage;

    @Test
    void getAll() {
        List<MpaRate> list = storage.getAll();
        assertNotNull(list);
        assertEquals(5, list.size());
    }

    @Test
    void getById() {
        MpaRate mpaRate = storage.getById(3);
        assertNotNull(mpaRate);
        assertEquals("PG-13", mpaRate.getName());

        assertThrows(NotFoundException.class, () -> {
            storage.getById(100);
        });
    }

    @Test
    void insert() {
        MpaRate mpaRate = new MpaRate();
        mpaRate.setName("New");
        storage.insert(mpaRate);
        MpaRate newMpa = storage.getById(mpaRate.getId());
        assertEquals(mpaRate, newMpa);
        storage.delete(newMpa);
    }

    @Test
    void update() {
        MpaRate mpaRate = storage.getById(3);
        String name = mpaRate.getName();
        mpaRate.setName("Upd");
        storage.update(mpaRate);
        MpaRate updMpa = storage.getById(mpaRate.getId());
        assertEquals(mpaRate, updMpa);
        mpaRate.setName(name);
        storage.update(mpaRate);
    }

    @Test
    void delete() {
        MpaRate newMpa = new MpaRate();
        newMpa.setName("NewRate");
        newMpa = storage.insert(newMpa);
        MpaRate mpaRate = storage.getById(newMpa.getId());
        assertEquals(newMpa, mpaRate);
        mpaRate = storage.delete(mpaRate);
        assertEquals(newMpa, mpaRate);
        int id = newMpa.getId();
        assertThrows(NotFoundException.class, () -> {
            storage.getById(id);
        });
    }
}
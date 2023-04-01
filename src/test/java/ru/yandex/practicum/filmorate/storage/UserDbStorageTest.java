package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {

    @Autowired
    private UserDbStorage storage;

    @Test
    void getAll() {
        List<User> list = storage.getAll();
        assertNotNull(list);
        assertEquals(3, list.size());
    }

    @Test
    void getById() {
        User user = storage.getById(2);
        assertNotNull(user);
        assertEquals(2, user.getId());
        assertThrows(NotFoundException.class, ()->{storage.getById(100);});
    }

    @Test
    void insert() {
        User user = new User();
        user.setEmail("user@yandex.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,3,7));

        User newUser = storage.insert(user);
        assertNotNull(newUser);

        user = storage.getById(newUser.getId());
        assertEquals(user, newUser);
        storage.delete(user);
    }

    @Test
    void update() {
        User user = storage.getById(3);
        user.setEmail("mail@mail.ru");
        user.getFriends().add(1);
        storage.update(user);
        User updUser = storage.getById(user.getId());
        assertEquals(user, updUser);
        user.setId(100);
        assertThrows(NotFoundException.class, ()->{storage.update(user);});
    }

    @Test
    void delete() {
        User user = new User();
        user.setEmail("user@yandex.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,3,7));
        storage.insert(user);
        User newUser = storage.getById(user.getId());
        assertEquals(user, newUser);
        storage.delete(newUser);
        int id = newUser.getId();
        assertThrows(NotFoundException.class, ()->{storage.getById(id);});
    }
}
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    public void validate() {
        UserController controller = new UserController();
        LocalDate birthday = LocalDate.of(2000,6,1);
        User user = new User(1,"login@gmail.com","login","name", birthday);

        // all correct
        assertDoesNotThrow(()->controller.validate(user));

        // e-mail
        user.setEmail(null);
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setEmail("");
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setEmail("login_gmail.com");
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setEmail("login@gmail.com");

        // login not empty
        user.setLogin(null);
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setLogin("");
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setLogin("log in");
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setLogin("login");

        // empty name
        user.setName(null);
        controller.validate(user);
        assertEquals(user.getLogin(),user.getName());

        // future birthday
        LocalDate date = LocalDate.now().plusDays(1);
        user.setBirthday(date);
        assertThrows(ValidationException.class, ()->controller.validate(user));
        user.setBirthday(birthday);
    }
}
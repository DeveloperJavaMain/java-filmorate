package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    public void validate() {
        UserValidator validator = new UserValidator();
        LocalDate birthday = LocalDate.of(2000, 6, 1);
        User user = User.builder()
                .id(1)
                .email("login@gmail.com")
                .login("login")
                .name("name")
                .birthday(birthday)
                .build();

        // all correct
        assertDoesNotThrow(() -> validator.validate(user));

        // e-mail
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setEmail("");
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setEmail("login_gmail.com");
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setEmail("login@gmail.com");

        // login not empty
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setLogin("");
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setLogin("log in");
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setLogin("login");

        // empty name
        user.setName(null);
        validator.validate(user);
        assertEquals(user.getLogin(), user.getName());

        // future birthday
        LocalDate date = LocalDate.now().plusDays(1);
        user.setBirthday(date);
        assertThrows(ValidationException.class, () -> validator.validate(user));
        user.setBirthday(birthday);
    }
}
package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.utils.Common.check;

@Slf4j
public class UserValidator {
    private final LocalDate maxDate = LocalDate.now();

    public void validate(User user) {
        try {
            String email = user.getEmail();
            String login = user.getLogin();
            LocalDate birthday = user.getBirthday();
            check(email == null || email.isEmpty() || !email.contains("@"), "incorrect email: " + email);
            check(login == null || login.isEmpty() || login.contains(" "), "incorrect login: " + login);
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(login);
            }
            check(birthday != null && birthday.isAfter(maxDate), "incorrect birthday: " + birthday);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}

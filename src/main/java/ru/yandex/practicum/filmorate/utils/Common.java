package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public class Common {
    public static void check(boolean result, String message) {
        if(result) {
            throw new ValidationException(message);
        }
    }
}

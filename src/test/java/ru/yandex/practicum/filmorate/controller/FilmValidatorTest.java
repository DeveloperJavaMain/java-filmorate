package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    public void validate() {
        FilmValidator validator = new FilmValidator();
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2020, 6, 1))
                .duration(90)
                .build();

        // all correct
        assertDoesNotThrow(() -> validator.validate(film));
        // empty name
        film.setName(null);
        assertThrows(ValidationException.class, () -> validator.validate(film));
        film.setName("");
        assertThrows(ValidationException.class, () -> validator.validate(film));
        film.setName("name");

        // long description
        film.setDescription(String.format("%0200d", 0));
        assertDoesNotThrow(() -> validator.validate(film));
        film.setDescription(String.format("%0201d", 0));
        assertThrows(ValidationException.class, () -> validator.validate(film));
        film.setDescription("description");

        // release date
        LocalDate date = LocalDate.of(1895, 12, 28);
        film.setReleaseDate(date);
        assertDoesNotThrow(() -> validator.validate(film));
        film.setReleaseDate(date.minusDays(1));
        assertThrows(ValidationException.class, () -> validator.validate(film));
        film.setReleaseDate(LocalDate.of(2020, 6, 1));

        //positive duration
        film.setDuration(1);
        assertDoesNotThrow(() -> validator.validate(film));
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> validator.validate(film));
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> validator.validate(film));
        film.setDuration(90);

    }

}
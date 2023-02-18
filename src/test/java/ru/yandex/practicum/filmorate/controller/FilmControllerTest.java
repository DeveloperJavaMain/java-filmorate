package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    public void validate() {
        FilmController controller = new FilmController();
        Film film = new Film(1,"name","description", LocalDate.of(2020,6,1),90);

        // all correct
        assertDoesNotThrow(()->controller.validate(film));
        // empty name
        film.setName(null);
        assertThrows(ValidationException.class, ()->controller.validate(film));
        film.setName("");
        assertThrows(ValidationException.class, ()->controller.validate(film));
        film.setName("name");

        // long description
        film.setDescription(String.format("%0200d",0));
        assertDoesNotThrow(()->controller.validate(film));
        film.setDescription(String.format("%0201d",0));
        assertThrows(ValidationException.class, ()->controller.validate(film));
        film.setDescription("description");

        // release date
        LocalDate date = LocalDate.of(1895,12,28);
        film.setReleaseDate(date);
        assertDoesNotThrow(()->controller.validate(film));
        film.setReleaseDate(date.minusDays(1));
        assertThrows(ValidationException.class, ()->controller.validate(film));
        film.setReleaseDate(LocalDate.of(2020,6,1));

        //positive duration
        film.setDuration(1);
        assertDoesNotThrow(()->controller.validate(film));
        film.setDuration(0);
        assertThrows(ValidationException.class, ()->controller.validate(film));
        film.setDuration(-1);
        assertThrows(ValidationException.class, ()->controller.validate(film));
        film.setDuration(90);

    }

}
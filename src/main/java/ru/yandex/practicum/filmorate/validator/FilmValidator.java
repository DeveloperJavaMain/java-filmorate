package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.utils.Common.check;

@Slf4j
public class FilmValidator {

    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    public void validate(Film film) {
        try {
            check(film.getName() == null || film.getName().isEmpty(), "name must not be null or empty");
            check(film.getDescription() != null && film.getDescription().length() > 200, "max description length 200");
            check(film.getReleaseDate() != null && film.getReleaseDate().isBefore(minDate), "release date must not be before 28-12-1895");
            check(film.getDuration() <= 0, "duration must be more then 0");
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}

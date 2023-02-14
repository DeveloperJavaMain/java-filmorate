package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static ru.yandex.practicum.filmorate.utils.Common.*;

@RestController
@RequestMapping("films")
@Slf4j
public class FilmController {

    private static int counter = 0;
    private TreeMap<Integer,Film> films = new TreeMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if(film!=null) {
            validate(film);
            film.setId(counter++);
            films.put(film.getId(), film);
            log.info("film created: " + film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if(film!=null) {
            validate(film);
            films.put(film.getId(), film);
            log.info("film updated: " + film);
        }
        if(counter<=film.getId()) {
            counter=film.getId()+1;
        }
        return film;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return films.get(id);
    }

    private final LocalDate minDate = LocalDate.of(1895,12,28);
    public void validate(Film film){
        try {
            check(film.getName() == null || film.getName().isEmpty(), "name must not be null or empty");
            check(film.getDescription() != null && film.getDescription().length() > 200, "max description length 200");
            check(film.getReleaseDate() != null && film.getReleaseDate().isBefore(minDate), "release date must not be before 28-12-1895");
            check(film.getDuration() <= 0, "duration must be more then 0");
        }catch(ValidationException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}

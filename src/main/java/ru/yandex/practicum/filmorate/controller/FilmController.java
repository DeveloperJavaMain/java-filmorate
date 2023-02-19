package ru.yandex.practicum.filmorate.controller;

//import jakarta.validation.Valid;
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
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static ru.yandex.practicum.filmorate.utils.Common.*;

@RestController
@RequestMapping("films")
@Slf4j
public class FilmController {

    private static int counter = 1;
    private TreeMap<Integer,Film> films = new TreeMap<>();
    private FilmValidator validator = new FilmValidator();

    @GetMapping
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if(film!=null) {
            validator.validate(film);
            film.setId(counter++);
            films.put(film.getId(), film);
            log.info("film created: " + film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if(film!=null && films.containsKey(film.getId())) {
            validator.validate(film);
            films.put(film.getId(), film);
            log.info("film updated: " + film);
            if(counter<=film.getId()) {
                counter=film.getId()+1;
            }
            return film;
        }
        throw new ValidationException("unknown film: "+film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return films.get(id);
    }

}

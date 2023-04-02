package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("/films getAll");
        return filmService.getAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("/films add " + film);
        return filmService.insert(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("/films update " + film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("/films get " + id);
        return filmService.getById(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("/films addLike " + filmId + " " + userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void remLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("/films remLike " + filmId + " " + userId);
        filmService.remLike(filmId, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTop(@RequestParam(defaultValue = "10") int count) {
        log.info("/films top10");
        return filmService.top(count);
    }

}

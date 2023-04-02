package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @ResponseBody
    public List<Genre> getAll() {
        log.info("/genres getAll");
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Genre get(@PathVariable int id) {
        log.info("/genres getById");
        return genreService.getById(id);
    }
}

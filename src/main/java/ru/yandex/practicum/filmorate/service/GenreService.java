package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {

    private final GenreStorage storage;

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getById(int id) {
        return storage.getById(id);
    }
}

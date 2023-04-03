package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();

    List<Genre> getByFilm(int filmId);

    Genre getById(int id);

    Genre insert(Genre genre);

    Genre update(Genre genre);

    Genre delete(Genre genre);
}

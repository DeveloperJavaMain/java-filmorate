package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film getById(int id);

    Film insert(Film film);

    Film update(Film film);

    Film delete(Film film);
}

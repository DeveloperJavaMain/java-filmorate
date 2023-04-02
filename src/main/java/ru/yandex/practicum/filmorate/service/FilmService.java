package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmValidator validator;
    private final FilmStorage storage;
    private final UserStorage userStorage;

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film getById(int id) {
        return storage.getById(id);
    }

    public Film insert(Film film) {
        validator.validate(film);
        return storage.insert(film);
    }

    public Film update(Film film) {
        validator.validate(film);
        return storage.update(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = getById(filmId);
        User user = userStorage.getById(userId);
        film.getLikes().add(userId);
        update(film);
    }

    public void remLike(int filmId, int userId) {
        Film film = getById(filmId);
        User user = userStorage.getById(userId);
        film.getLikes().remove(userId);
        update(film);
    }

    public List<Film> top(int count) {
        List<Film> list = getAll();
        return list.stream()
                .sorted(Comparator.comparingInt(film->-film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}

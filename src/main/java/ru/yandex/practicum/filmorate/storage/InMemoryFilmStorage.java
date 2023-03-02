package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.TreeMap;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private static int counter = 1;
    private TreeMap<Integer,Film> films = new TreeMap<>();

    @Override
    public List<Film> getAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Film getById(int id) {
        if(!films.containsKey(id)) {
            throw new NotFoundException("Film #"+id+" not found");
        }
        return films.get(id);
    }

    @Override
    public Film insert(Film film) {
        film.setId(counter++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if(film==null) {
            return null;
        }
        getById(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film delete(Film film) {
        if(film==null) {
            return null;
        }
        getById(film.getId());
        return films.remove(film.getId());
    }
}

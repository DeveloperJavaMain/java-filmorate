package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.TreeMap;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static int counter = 1;
    private TreeMap<Integer, User> users = new TreeMap<>();

    @Override
    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User getById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User #" + id + " not found");
        }
        return users.get(id);
    }

    @Override
    public User insert(User user) {
        user.setId(counter++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null) {
            return null;
        }
        getById(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(User user) {
        if (user == null) {
            return null;
        }
        getById(user.getId());
        return users.remove(user.getId());
    }
}

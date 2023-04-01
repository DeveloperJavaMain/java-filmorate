package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator validator;
    private final UserStorage storage;

    public List<User> getAll() {
        return storage.getAll();
    }

    public User getById(int id) {
        return storage.getById(id);
    }

    public User insert(User user) {
        validator.validate(user);
        return storage.insert(user);
    }

    public User update(User user) {
        validator.validate(user);
        return storage.update(user);
    }

    public List<User> getFriends(int userId) {
        User user = getById(userId);
        return user.getFriends().stream().map(id->getById(id)).collect(Collectors.toList());
    }

    public void addFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        update(user);
    }

    public void remFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        update(user);
    }

    public List<User> commonFriends(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        List<Integer> list = new ArrayList<>(user.getFriends());
        Set<Integer> frndLst = friend.getFriends();
        list.retainAll(frndLst);
        return list.stream().map(id->getById(id)).collect(Collectors.toList());
    }

}

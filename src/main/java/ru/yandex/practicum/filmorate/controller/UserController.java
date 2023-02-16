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
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static ru.yandex.practicum.filmorate.utils.Common.*;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private static int counter = 1;
    private TreeMap<Integer, User> users = new TreeMap<>();

    @GetMapping
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        if(user!=null) {
            validate(user);
            user.setId(counter++);
            users.put(user.getId(), user);
            log.info("user created: " + user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if(user!=null && users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
            log.info("user updated: " + user);
            if(counter<=user.getId()) {
                counter=user.getId()+1;
            }
            return user;
        }
        throw new ValidationException("unknown user: "+user);
    }

    private final LocalDate maxDate = LocalDate.now();
    public void validate(User user) {
        try {
            String email = user.getEmail();
            String login = user.getLogin();
            LocalDate birthday = user.getBirthday();
            check(email == null || email.isEmpty() || !email.contains("@"), "incorrect email: " + email);
            check(login == null || login.isEmpty() || login.contains(" "), "incorrect login: " + login);
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(login);
            } else {
                //check(user.getName().contains(" "), "incorrect name: "+user.getName());
            }
            check(birthday != null && birthday.isAfter(maxDate), "incorrect birthday: " + birthday);
        }catch(ValidationException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @NotNull
    @NotEmpty
    @Email(message = "Введенное значение не является адресом электронной почты.")
    private String email;
    @NotNull
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем. ")
    private LocalDate birthday;
    private Set<Integer> friends = new TreeSet<>();
}

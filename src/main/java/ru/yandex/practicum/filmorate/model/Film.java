package ru.yandex.practicum.filmorate.model;

//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    private @NotNull @NotEmpty String name;
    @Length(min = 1, max = 200, message = "Описание фильма не должно превышать 200 символов.")
    private String description;
    @NotNull
    @PastOrPresent(message = "Дата релиза не может быть в будущем. ")
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма не может быть отрицательной.")
    private int duration;
    private Set<Integer> likes = new TreeSet<>();
}


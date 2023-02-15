package ru.yandex.practicum.filmorate.model;

//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    private @NotNull @NotEmpty String name;
    private String description;
    private @NotNull LocalDate releaseDate;
    private int duration;

}


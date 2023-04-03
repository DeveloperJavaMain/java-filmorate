package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Genre implements Comparable<Genre> {
    private int id;
    @NotBlank
    private String name;

    @Override
    public int compareTo(Genre genre) {
        return (int) (id - genre.getId());
    }
}

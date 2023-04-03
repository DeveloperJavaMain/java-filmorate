package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MpaRate {
    private int id;
    @NotBlank
    private String name;
}

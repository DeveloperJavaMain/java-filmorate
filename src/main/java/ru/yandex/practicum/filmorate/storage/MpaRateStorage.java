package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.List;

public interface MpaRateStorage {
    List<MpaRate> getAll();
    MpaRate getById(int id);

    MpaRate insert(MpaRate mpaRate);
    MpaRate update(MpaRate mpaRate);
    MpaRate delete(MpaRate mpaRate);
}

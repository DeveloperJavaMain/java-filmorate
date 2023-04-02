package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.storage.MpaRateStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaRateService {

    private final MpaRateStorage storage;

    public List<MpaRate> getAll() {
        return storage.getAll();
    }

    public MpaRate getById(int id) {
        return storage.getById(id);
    }
}

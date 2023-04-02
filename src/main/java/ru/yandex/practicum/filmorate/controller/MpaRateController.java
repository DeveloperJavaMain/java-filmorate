package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.service.MpaRateService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaRateController {

    private final MpaRateService mpaRateService;

    @GetMapping
    @ResponseBody
    public List<MpaRate> getAll() {
        log.info("/mpa getAll");
        return mpaRateService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MpaRate get(@PathVariable int id) {
        log.info("/mpa getById");
        return mpaRateService.getById(id);
    }
}

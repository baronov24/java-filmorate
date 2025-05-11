package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        // проверяем выполнение необходимых условий
        checkFields(film);

        // формируем дополнительные данные
        film.setId(getNextId());

        // сохраняем новый фильм в памяти приложения
        films.put(film.getId(), film);

        log.info("Добавлен новый фильм с id = {}", film.getId());
        return film;
    }

    // вспомогательный метод для проверки полей объекта
    private void checkFields(Film film) {
        StringBuilder validation = new StringBuilder();

        if (film.getName() == null || film.getName().isEmpty()) {
            validation.append("Название не может быть пустым. ");
        }

        if (film.getDescription().length() > 200) {
            validation.append("Максимальная длина описания — 200 символов. ");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            validation.append("Дата релиза — не раньше 28 декабря 1895 года. ");
        }

        if (film.getDuration() <= 0) {
            validation.append("Продолжительность фильма должна быть положительным числом.");
        }

        if (!validation.isEmpty()) {
            log.info("Фильм не прошел проверку. {}", validation);
            throw new ValidationException(validation.toString());
        }
    }

    // вспомогательный метод для генерации нового идентификатора
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null || newFilm.getId() == 0) {
            log.info("Запрос на изменение фильма без указания id");
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(newFilm.getId())) {
            log.info("Фильм с id = {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        // проверяем выполнение необходимых условий
        checkFields(newFilm);

        films.put(newFilm.getId(), newFilm);

        log.info("Фильм с id = {} обновлен", newFilm.getId());
        return newFilm;
    }
}

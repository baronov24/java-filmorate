package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        film = new Film();
        film.setName("Поймай меня, если сможешь");
        film.setDescription("Агент ФБР Карл Хэнрэтти отдал бы все," +
                " чтобы схватить Фрэнка и привлечь к ответственности за свои деяния," +
                " но Фрэнк всегда опережает его на шаг, заставляя продолжать погоню.");
        film.setReleaseDate(LocalDate.of(2002, 12, 16));
        film.setDuration(141);
    }

    @Test
    public void testAddNewFilm() {
        assertDoesNotThrow(() -> filmController.createFilm(film));
        Collection<Film> films = filmController.getAllFilms();
        Assertions.assertEquals(1, films.size(), "Некорректное количество фильмов");
    }
}

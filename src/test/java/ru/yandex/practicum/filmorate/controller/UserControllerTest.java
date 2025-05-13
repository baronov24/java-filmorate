package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        user = new User();
        user.setLogin("debaronov");
        user.setEmail("baronov24@gmail.com");
        user.setBirthday(LocalDate.of(1990, 11, 6));
    }

    @Test
    public void testAddNewUser() {
        assertDoesNotThrow(() -> userController.createUser(user));
        Collection<User> users = userController.getAllUsers();
        Assertions.assertEquals(1, users.size(), "Некорректное количество фильмов");
        Assertions.assertEquals("debaronov", user.getName(), "Не изменилось имя пользователя");
    }
}

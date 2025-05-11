package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        // проверяем выполнение необходимых условий
        checkFields(user);

        // формируем дополнительные данные
        user.setId(getNextId());

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь с id = {}", user.getId());
        return user;
    }

    // вспомогательный метод для проверки полей объекта
    private void checkFields(User user) {
        StringBuilder validation = new StringBuilder();

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            validation.append("Логин не может быть пустым или содержать пробелы. ");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            validation.append("Дата рождения не может быть в будущем.");
        }

        if (!validation.isEmpty()) {
            log.info("Пользователь не прошел проверку. {}", validation);
            throw new ValidationException(validation.toString());
        }
    }

    // вспомогательный метод для генерации нового идентификатора
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null || newUser.getId() == 0) {
            log.info("Запрос на изменение пользователя без указания id");
            throw new ValidationException("Id должен быть указан");
        }

        if (!users.containsKey(newUser.getId())) {
            log.info("Пользователь с id = {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        // проверяем выполнение необходимых условий
        checkFields(newUser);

        User oldUser = users.get(newUser.getId());

        users.put(newUser.getId(), newUser);

        log.info("Пользователь с id = {} обновлен", newUser.getId());
        return oldUser;
    }
}

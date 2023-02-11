package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> getById(Long id);

    Optional<User> getByEmail(String email);

    User add(User user);

    User update(User user);

    void remove(Long id);
}

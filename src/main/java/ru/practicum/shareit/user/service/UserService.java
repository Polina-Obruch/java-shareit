package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User update(Long id, User user);

    void remove(Long id);

    List<User> getAll();

    User getById(Long id);

}

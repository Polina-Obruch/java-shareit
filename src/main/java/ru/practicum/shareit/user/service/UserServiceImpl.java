package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.core.exception.DuplicateEmailException;
import ru.practicum.shareit.core.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        log.info("Добавление пользователя");
        checkEmail(user.getEmail());
        return userRepository.add(user);
    }

    @Override
    public User update(Long id, User user) {
        log.info(String.format("Обновление пользователя c id = %d", id));
        User updateUser = userRepository.getById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Пользователь с id = %d не найден в базе", id)));

        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            updateUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }

        return userRepository.update(updateUser);
    }

    @Override
    public void remove(Long id) {
        log.info(String.format("Удаление пользователя c id = %d", id));
        userRepository.remove(id);
    }

    @Override
    public List<User> getAll() {
        log.info("Выдача всех пользователей");
        return userRepository.getAll();
    }

    @Override
    public User getById(Long id) {
        log.info(String.format("Выдача пользователя c id = %d", id));
        return userRepository.getById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("Пользователь с id = %d не найден в базе", id)));
    }

    private void checkEmail(String email) {
        userRepository.getByEmail(email).ifPresent(user -> {
            throw new DuplicateEmailException(String.format("Пользователь с email = %s уже зарегистрирован", email));
        });
    }
}

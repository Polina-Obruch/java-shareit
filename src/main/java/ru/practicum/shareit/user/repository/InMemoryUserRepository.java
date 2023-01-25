package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private long count = 0;

    @Override
    public User add(User user) {
        log.info("Запрос к БД на добавление пользователя");
        long id = getId();
        user.setId(id);
        userMap.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void remove(Long id) {
        userMap.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userMap.values().stream().filter(user ->
                user.getEmail().equals(email)).findFirst();
    }

    private long getId() {
        return ++count;
    }
}

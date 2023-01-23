package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> getByUserId(long userId);

    List<Item> getByText(String text);

    Optional<Item> getByItemId(long id);

    Item add(Item item);

    Item update(Item item);

    void remove(long id);
}

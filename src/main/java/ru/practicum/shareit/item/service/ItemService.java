package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemService {
    Item add(Long userId, Item item);

    Item update(Long itemId, Long userId, Item item);

    void remove(Long itemId);

    Item getByItemId(Long itemId);

    List<Item> getByUserId(Long userId);

    List<Item> search(String text);
}

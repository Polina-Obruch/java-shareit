package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> itemMap = new HashMap<>();
    long count = 0;


    @Override
    public Item add(Item item) {
        long id = getId();
        item.setId(id);
        itemMap.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public void remove(long id) {
        itemMap.remove(id);
    }

    @Override
    public Optional<Item> getByItemId(long id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public List<Item> getByUserId(long userId) {
        return itemMap.values().stream().filter(item ->
                item.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> getByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        String lowerText = text.toLowerCase();
        return itemMap.values().stream().filter(item ->
                        (item.getName().toLowerCase().contains(lowerText)
                                || item.getDescription().toLowerCase().contains(lowerText))
                                && item.getAvailable())
                .collect(Collectors.toList());
    }

    private long getId() {
        return ++count;
    }
}

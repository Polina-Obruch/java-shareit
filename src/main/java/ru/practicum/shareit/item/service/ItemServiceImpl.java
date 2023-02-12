package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.core.exception.EntityNotFoundException;
import ru.practicum.shareit.core.exception.FailUserIdForItemException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Item add(Long userId, Item item) {
        log.info("Добавление вещи");
        item.setOwner(userService.getByUserId(userId));
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long itemId, Long userId, Item item) {
        log.info(String.format("Обновление вещи c id = %d", itemId));
        //Если пользователя или вещи нет в базе - ошибка NotFound
        User user = userService.getByUserId(userId);
        Item updateItem = this.getByItemId(itemId);
        User owner = updateItem.getOwner();

        if (!user.equals(owner)) {
            throw new FailUserIdForItemException(
                    String.format("Обновлять информацию по предмету с id = %d может только пользователь с id = %d",
                            itemId, owner.getId()));
        }

        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }

        return itemRepository.save(updateItem);
    }

    @Override
    public void remove(Long itemId) {
        log.info(String.format("Удаление вещи с id = %d", itemId));
        itemRepository.deleteById(itemId);
    }

    @Override
    public Item getByItemId(Long itemId) {
        log.info(String.format("Выдача вещи с id = %d", itemId));
        return itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Предмет с id = %d не найден в базе", itemId)));
    }

    @Override
    public List<Item> getByUserId(Long userId) {
        log.info(String.format("Выдача вещей пользователя с id = %d",userId));
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public List<Item> search(String text) {
        log.info(String.format("Выдача вещи по поиску строки = %s", text.toLowerCase()));
         if ( text.isBlank()) {
             return Collections.emptyList();
         }
        return itemRepository.findByText("%"+text.toLowerCase()+"%");
    }
}

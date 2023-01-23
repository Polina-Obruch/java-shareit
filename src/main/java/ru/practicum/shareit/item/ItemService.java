package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.core.exception.model.EntityNotFoundException;
import ru.practicum.shareit.core.exception.model.FailUserIdForItemException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public Item add(long userId, Item item) {
        log.info("Добавление вещи");
        item.setOwner(userService.getById(userId));
        return itemRepository.add(item);
    }

    public Item update(long itemId, long userId, Item item) {
        log.info(String.format("Обновление вещи c id = %d", itemId));
        //Если пользователя или вещи нет в базе - ошибка NotFound
        User user = userService.getById(userId);
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

        return itemRepository.update(updateItem);
    }

    public void remove(long itemId) {
        log.info(String.format("Удаление вещи с id = %d", itemId));
        itemRepository.remove(itemId);
    }

    public Item getByItemId(long itemId) {
        log.info(String.format("Выдача вещи с id = %d", itemId));
        return itemRepository.getByItemId(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Предмет с id = %d не найден в базе", itemId)));
    }

    public List<Item> getByUserId(long userId) {
        log.info(String.format("Выдача вещей пользователя с id = %d",userId));
        return itemRepository.getByUserId(userId);
    }

    public List<Item> search(String text) {
        log.info(String.format("Выдача вещи по поиску строки = %s", text));
        return itemRepository.getByText(text);
    }
}

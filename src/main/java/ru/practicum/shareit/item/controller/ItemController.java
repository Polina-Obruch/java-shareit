package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemMapper.itemToItemDto(itemService.add(userId, itemMapper.itemDtoToItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestHeader(name = "X-Sharer-User-Id") long userId,
                          @Valid @RequestBody UpdateItemDto itemDto) {
        return itemMapper.itemToItemDto(itemService.update(itemId, userId, itemMapper.updateItemDtoToItem(itemDto)));
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId) {
        itemService.remove(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getByItemId(@PathVariable long itemId) {
        return itemMapper.itemToItemDto(itemService.getByItemId(itemId));
    }

    @GetMapping
    public List<ItemDto> getByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemMapper.itemListToItemDtoList(itemService.getByUserId(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(required = false) String text) {
        return itemMapper.itemListToItemDtoList(itemService.search(text));
    }
}

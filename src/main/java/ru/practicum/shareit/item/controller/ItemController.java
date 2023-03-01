package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public ItemDto add(@RequestHeader(name = USER_ID_HEADER) long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemMapper.itemToItemDto(itemService.add(userId, itemMapper.itemDtoToItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestHeader(name = USER_ID_HEADER) long userId,
                          @Valid @RequestBody UpdateItemDto itemDto) {
        return itemMapper.itemToItemDto(itemService.update(itemId, userId, itemMapper.updateItemDtoToItem(itemDto)));
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId) {
        itemService.remove(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemGetDto getByItemId(@PathVariable long itemId, @RequestHeader(name = USER_ID_HEADER) long userId) {
        Item item = itemService.getByItemId(itemId, userId);

        if (Objects.equals(item.getOwner().getId(), userId)) {
            item.setNextBooking(bookingMapper.bookingToBookingShortDto(bookingService.getNextBookingByItemId(itemId)));
            item.setLastBooking(bookingMapper.bookingToBookingShortDto(bookingService.getLastBookingByItemId(itemId)));
        }
        return itemMapper.itemToItemGetDto(item);
    }

    @GetMapping
    public List<ItemGetDto> getByOwnerId(@RequestHeader(name = USER_ID_HEADER) long ownerId) {
        List<Item> items = itemService.getByOwnerId(ownerId);

       List<Item> itemListWithBooking = items.stream().peek(item -> {
            item.setNextBooking(bookingMapper.bookingToBookingShortDto(bookingService.getNextBookingByItemId(item.getId())));
            item.setLastBooking(bookingMapper.bookingToBookingShortDto(bookingService.getLastBookingByItemId(item.getId())));
       }).collect(Collectors.toList());

        return itemMapper.itemListToItemGetDtoList(itemListWithBooking);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(required = false) String text) {
        return itemMapper.itemListToItemDtoList(itemService.search(text));
    }
}

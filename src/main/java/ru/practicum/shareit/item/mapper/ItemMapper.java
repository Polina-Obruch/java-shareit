package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    Item updateItemDtoToItem(UpdateItemDto updateItemDto);

    UpdateItemDto itemToUpdateItemDto(Item item);

    List<ItemDto> itemListToItemDtoList(List<Item> itemList);
}

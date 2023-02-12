package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Value
public class BookingAnswerDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    Item item;
    User booker;
}

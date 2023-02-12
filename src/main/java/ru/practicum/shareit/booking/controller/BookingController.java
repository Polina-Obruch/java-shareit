package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;


    @PostMapping
    public BookingAnswerDto add(@RequestHeader(name = USER_ID_HEADER) long bookerId, @Valid @RequestBody BookingRequestDto dto) {
        return bookingMapper.bookingToBookingAnswerDto(
                bookingService.add(bookerId, dto.getItemId(), bookingMapper.bookingRequestDtoToBooking(dto)));
    }
}

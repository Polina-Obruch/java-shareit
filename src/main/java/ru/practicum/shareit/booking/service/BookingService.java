package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;


public interface BookingService {
    Booking add(Long userId, Long itemId, Booking booking);

    Booking approved(Long bookingId, Long ownerId, boolean isApprove);

    Booking getByBookingId(Long bookingId, Long userId);

    List<Booking> getAllBookingByOwnerId(Long ownerId, State state);

    List<Booking> getAllBookingByBookerId(Long bookerId, State state);

    Booking getNextBookingByItemId(Long itemId);

    Booking getLastBookingByItemId(Long itemId);
}

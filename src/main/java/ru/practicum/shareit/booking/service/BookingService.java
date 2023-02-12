package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;


public interface BookingService {
    Booking add(Long userId, Long itemId, Booking booking);
    Booking approved(Long bookingId, Long ownerId, boolean isApprove);
    Booking getById(Long bookingId, Long userId);
    List<Booking> getAllBookingByUserId(Long userId, State state);
    List<Booking> getAllBookingByOwnerId(Long userId, State state);
}

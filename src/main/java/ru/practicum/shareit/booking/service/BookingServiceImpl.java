package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking add(Long bookerId, Long itemId, Booking booking) {
        User booker = userService.getByUserId(bookerId);
        Item item = itemService.getByItemId(itemId);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approved(Long bookingId, Long ownerId, boolean isApprove) {
        return null;
    }

    @Override
    public Booking getById(Long bookingId, Long userId) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingByUserId(Long userId, State state) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingByOwnerId(Long userId, State state) {
        return null;
    }
}

package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.core.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking add(Long bookerId, Long itemId, Booking booking) {
        User booker = userService.getByUserId(bookerId);
        Item item = itemService.getByItemId(itemId, bookerId);

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(String.format("Предмет с id = %d не доступен", itemId));
        }

        if (Objects.equals(bookerId, item.getOwner().getId())) {
            throw new FailIdException(String.format(
                    "Делать броннирование вещи с id = %d  владелец не может", itemId));
        }

        checkTimeValidation(booking);

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getByBookingId(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Броннирование с id = %d не найдено в базе", bookingId)));

        boolean isOwner = Objects.equals(booking.getItem().getOwner().getId(), userId);
        boolean isBooker = Objects.equals(booking.getBooker().getId(), userId);

        if (!(isOwner || isBooker)) {
            throw new FailIdException(String.format(
                    "Просматривать броннирование с id = %d может только владелец броннирования/вещи", bookingId));
        }

        return booking;
    }

    @Override
    public Booking approved(Long bookingId, Long ownerId, boolean isApprove) {
        Booking booking = this.getByBookingId(bookingId, ownerId);
        BookingStatus status = booking.getStatus();

        if (!status.equals(BookingStatus.WAITING)) {
            throw new ValidationException(String.format(
                    "Статус бронирования с id = %d уже был изменен ", bookingId));
        }

        //Владелец вещи отвечает на запрос
        if (Objects.equals(booking.getItem().getOwner().getId(), ownerId)) {
            booking.setStatus(isApprove ? BookingStatus.APPROVED : BookingStatus.REJECTED);
            return bookingRepository.save(booking);
        }

        throw new FailIdException(String.format(
                "Подтверждать броннирование с id = %d может только владелец вещи", bookingId));
    }

    @Override
    public List<Booking> getAllBookingByOwnerId(Long ownerId, State state) {
        List<Item> items = itemService.getByOwnerId(ownerId);
        // Если нет вещей этого пользователя в базе - ошибка
        if (items.isEmpty()) {
            throw new FailIdException(String.format(
                    "Владелец вещей с id = %d отсутствует в базе", ownerId));
        }

        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            default:
                throw new StatusException();
        }
    }

    @Override
    public List<Booking> getAllBookingByBookerId(Long bookerId, State state) {
        //Проверка наличия такого пользователя в системе
        userService.getByUserId(bookerId);

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            default:
                throw new StatusException();
        }
    }

    @Override
    public Booking getNextBookingByItemId(Long itemId) {
        return bookingRepository.findFirstByItemIdLikeAndStartAfterOrderByStartDesc(itemId, LocalDateTime.now());
    }

    @Override
    public Booking getLastBookingByItemId(Long itemId) {
        return bookingRepository.findFirstByItemIdLikeAndEndBeforeOrderByEnd(itemId, LocalDateTime.now());
    }

    private void checkTimeValidation(Booking booking) {
        boolean isStartInPast = booking.getStart().isBefore(LocalDateTime.now());
        boolean isEndInPast = booking.getEnd().isBefore(LocalDateTime.now());
        boolean isEndBeforeStart = booking.getEnd().isBefore(booking.getStart());

        if (isStartInPast || isEndInPast || isEndBeforeStart) {
            throw new ValidationException("Время броннирование указано неверно");
        }
    }
}

package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingMapperTest {
    @Spy
    private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    private Long userId;
    private Long itemId;
    private Long bookingId;

    private User user;
    private User user1;
    private Item item;
    private Item item1;
    private Booking booking;
    private Booking newBooking;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;
        bookingId = 1L;

        user = new User(
                userId,
                "John",
                "john.doe@mail.com");

        user1 = new User(
                2L,
                "Jo",
                "jo.do@mail.com");

        item = new Item(
                itemId,
                "name",
                "description",
                true,
                user,
                new BookingShortDto(2L, 2L),
                new BookingShortDto(3L, 2L),
                null,
                null);

        item1 = new Item(
                itemId,
                "name",
                "description",
                true,
                user1,
                new BookingShortDto(2L, 2L),
                new BookingShortDto(3L, 2L),
                null,
                null);

        booking = new Booking(
                bookingId,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                user,
                BookingStatus.WAITING);

        newBooking = new Booking(
                bookingId,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                null,
                null,
                null);
    }

    @Test
    void bookingToBookingShortDto_shouldReturnBookingShortDto() {
        BookingShortDto dto = bookingMapper.bookingToBookingShortDto(booking);

        assertThat(dto.getId()).isEqualTo(booking.getId());
        assertThat(dto.getBookerId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void bookingToBookingShortDto_shouldReturnNull() {
        BookingShortDto dto = bookingMapper.bookingToBookingShortDto(null);
        assertThat(dto).isEqualTo(null);
    }

    @Test
    void bookingToBookingAnswerDto() {
        BookingAnswerDto dto = bookingMapper.bookingToBookingAnswerDto(booking);
        assertThat(dto.getId()).isEqualTo(booking.getId());
        assertThat(dto.getBooker().getId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void bookingToBookingAnswerDto_shouldReturnNull() {
        BookingAnswerDto dto = bookingMapper.bookingToBookingAnswerDto(null);
        assertThat(dto).isEqualTo(null);
    }
}

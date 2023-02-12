package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking bookingRequestDtoToBooking(BookingRequestDto bookingRequestDto);

    BookingAnswerDto bookingToBookingAnswerDto(Booking booking);
}

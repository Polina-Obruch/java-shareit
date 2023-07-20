package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRequestDto {
    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}

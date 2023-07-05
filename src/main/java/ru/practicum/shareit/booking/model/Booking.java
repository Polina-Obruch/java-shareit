package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "START_TIME")
    private LocalDateTime start;

    @Column(name = "END_TIME")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "BOOKER_ID")
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}

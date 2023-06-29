package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "ITEMS")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Transient
    private BookingShortDto lastBooking;

    @Transient
    private BookingShortDto nextBooking;

    @Transient
    private List<CommentDto> comments;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    private Request request;
}

package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.core.exception.EntityNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServerTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private RequestService requestService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final Long userId = 1L;
    private final Long itemId = 1L;
    private final Long requestId = 1L;

    private User user = new User(
            userId,
            "John",
            "john.doe@mail.com");

    private Item item = new Item(
            itemId,
            "name",
            "description",
            true,
            null,
            new BookingShortDto(2L, 2L),
            new BookingShortDto(3L, 2L),
            null,
            null);

    private Request request = new Request(
            requestId,
            "description",
            LocalDateTime.now(),
            user);

    @Test
    void add_shouldThrowNotFoundExceptionIfUserIsNotExists() {
        when(userService.getByUserId(userId)).thenThrow(EntityNotFoundException.class);
        assertThatThrownBy(() -> itemService.add(userId, null, item)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_shouldCreateItemWithRequest() {
        when(userService.getByUserId(userId)).thenReturn(user);

        when(userService.getByUserId(userId)).thenReturn(user);
        when(requestService.getByRequestId(requestId)).thenReturn(request);
        when(itemRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Item newItem = itemService.add(userId, requestId, item);

        assertThat(newItem.getRequest()).isEqualTo(request);
        assertThat(newItem.getOwner()).isEqualTo(user);
    }

    @Test
    void removeTest() {
        itemRepository.deleteById(any());
        itemService.remove(userId);
    }

    /*@Test
    void updateTest() {
        when(userService.getByUserId(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
    }*/

}

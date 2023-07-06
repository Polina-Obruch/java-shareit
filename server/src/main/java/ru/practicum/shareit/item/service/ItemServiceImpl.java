package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.core.exception.EntityNotFoundException;
import ru.practicum.shareit.core.exception.FailIdException;
import ru.practicum.shareit.core.exception.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final RequestRepository requestRepository;

    //В методе вызывается другой @Transactional-метод.
    // Все будет в одной (существующей) транзакции так как по умолчанию : Propagation.REQUIRED
    @Transactional
    @Override
    public Item add(Long userId, Long requestId, Item item) {
        log.info("Добавление вещи");
        item.setOwner(userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Пользователь с id = %d не найден в базе", userId))));
        if (requestId != null) {
            Request request = requestRepository.findById(requestId).orElseThrow(()
                    -> new EntityNotFoundException(String.format("Запрос с id = %d не найден в базе", requestId)));
            item.setRequest(request);
        }
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item update(Long itemId, Long userId, Item item) {
        log.info(String.format("Обновление вещи c id = %d", itemId));
        //Если пользователя или вещи нет в базе - ошибка NotFound
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Пользователь с id = %d не найден в базе", userId)));
        Item updateItem = this.getByItemId(itemId, userId);
        User owner = updateItem.getOwner();

        if (!user.equals(owner)) {
            throw new FailIdException(
                    String.format("Вы не можете обновлять информацию по предмету с id = %d", itemId));
        }

        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }

        return itemRepository.save(updateItem);
    }

    @Transactional
    @Override
    public void remove(Long itemId) {
        log.info(String.format("Удаление вещи с id = %d", itemId));
        itemRepository.deleteById(itemId);
    }

    @Override
    public Item getByItemId(Long itemId, Long userId) {
        log.info(String.format("Выдача вещи с id = %d", itemId));

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Предмет с id = %d не найден в базе", itemId)));

        //Если выдачу вещи запросил владелец - нужно добавить даты ближайших броннирований
        if (Objects.equals(item.getOwner().getId(), userId)) {
            List<Booking> bookings = bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(itemId, BookingStatus.APPROVED);

            item.setNextBooking(bookingMapper.bookingToBookingShortDto(getNextBooking(bookings)));
            item.setLastBooking(bookingMapper.bookingToBookingShortDto(getLastBooking(bookings)));
        }

        item.setComments(commentMapper.commentListToCommentDtoList(commentRepository.findAllByItemId(itemId)));

        return item;
    }

    @Override
    public List<Item> getByOwnerId(Long ownerId, Pageable pageable) {
        log.info(String.format("Выдача вещей владельца с id = %d", ownerId));
        List<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId, pageable);

        return items.stream().map(item -> {
            List<Booking> bookings = bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(item.getId(), BookingStatus.APPROVED);

            item.setNextBooking(bookingMapper.bookingToBookingShortDto(getNextBooking(bookings)));
            item.setLastBooking(bookingMapper.bookingToBookingShortDto(getLastBooking(bookings)));
            item.setComments(commentMapper.commentListToCommentDtoList(commentRepository.findAllByItemId(item.getId())));

            return item;

        }).collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text, Pageable pageable) {
        log.info(String.format("Выдача вещи по поиску строки = %s", text.toLowerCase()));
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findByText("%" + text.toLowerCase() + "%", pageable);
    }

    @Transactional
    @Override
    public Comment addComment(Long itemId, Long userId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Предмет с id = %d не найден в базе", itemId)));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Пользователь с id = %d не найден в базе", userId)));
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());

        //Проверка на наличие аренды этой вещи этим пользователем
        List<Booking> bookingsForItem = bookings.stream().map(booking -> {
            boolean isBooking = Objects.equals(booking.getItem().getId(), item.getId());
            if (isBooking) {
                return booking;
            }
            return null;
        }).collect(Collectors.toList());

        if (bookingsForItem.isEmpty()) {
            throw new ValidationException(
                    String.format("Оставлять комментарий по предмету с id = %d можно только после взятия в аренду", itemId));
        }

        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    private Booking getNextBooking(List<Booking> bookings) {
        List<Booking> filteredBookings = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        return filteredBookings.isEmpty() ? null : filteredBookings.get(0);
    }

    private Booking getLastBooking(List<Booking> bookings) {
        List<Booking> filteredBookings = bookings.stream()
                .filter(booking -> (booking.getEnd().isAfter(LocalDateTime.now())
                        && booking.getStart().isBefore(LocalDateTime.now())) || booking.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        return filteredBookings.isEmpty() ? null : filteredBookings.get(filteredBookings.size() - 1);
    }
}

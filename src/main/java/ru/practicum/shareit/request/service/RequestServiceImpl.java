package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.core.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestWithItemDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;


    @Transactional
    @Override
    public Request add(Long userId, Request request) {
        log.info("Создание запроса");
        User user = userService.getByUserId(userId);
        request.setCreated(LocalDateTime.now());
        request.setUser(user);
        return requestRepository.save(request);
    }

    @Override
    public List<RequestWithItemDto> getAllRequestsByOwnerId(Long ownerId) {
        log.info(String.format("Выдача запросов пользователя c id = %d", ownerId));
        //проверка наличия пользователя отправившего запрос
        userService.getByUserId(ownerId);
        return requestRepository.findAllByUserIdOrderByCreatedDesc(ownerId).
                stream().map(this::addItemsForRequest).collect(Collectors.toList());
    }

    @Override
    public List<RequestWithItemDto> getAllRequest(Long userId, Pageable pageable) {
        log.info("Выдача всех запросов");
        //проверка наличия пользователя отправившего запрос
        userService.getByUserId(userId);
        return requestRepository.findAllByUserIdNotOrderByCreatedDesc(userId,pageable).
                stream().map(this:: addItemsForRequest).collect(Collectors.toList());
    }

    @Override
    public Request getByRequestId(Long requestId) {
        log.info(String.format("Выдача запроса c id = %d", requestId));
        return requestRepository.findById(requestId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Запрос с id = %d не найден в базе", requestId)));
    }

    @Override
    public RequestWithItemDto getByRequestIdWithItem(Long requestId, Long userId) {
        log.info(String.format("Выдача запроса вместе с ответами id = %d", requestId));
        // проверка наличия пользователя отправившего запрос
        userService.getByUserId(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(()
                -> new EntityNotFoundException(String.format("Запрос с id = %d не найден в базе", requestId)));

        return addItemsForRequest(request);
    }

    private RequestWithItemDto addItemsForRequest(Request request) {
        List<ItemDto> items = itemMapper.itemListToItemDtoList(itemRepository.findAllByRequestId(request.getId()));
        RequestWithItemDto requestWithItemDto = requestMapper.requestToRequestWithItemDto(request);
        requestWithItemDto.setItems(items);

        return requestWithItemDto;
    }
}

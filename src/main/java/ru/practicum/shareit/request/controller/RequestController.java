package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.request.dto.RequestAnswerDto;
import ru.practicum.shareit.request.dto.RequestWithItemDto;
import ru.practicum.shareit.core.mapper.PaginationMapper;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class RequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestMapper requestMapper;
    private final RequestService requestService;

    @PostMapping
    public RequestAnswerDto add(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                @Valid @RequestBody RequestAddDto requestAddDto) {
        return requestMapper.requestToRequestAnswerDto(requestService.add(userId, requestMapper.requestAddDtoToRequest(requestAddDto)));
    }

    @GetMapping
    public List<RequestWithItemDto> getAllRequestByOwnerId(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        return requestService.getAllRequestsByOwnerId(userId);
    }

    @GetMapping("/all")
    public List<RequestWithItemDto> getAllRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                  @RequestParam(required = false) Integer from,
                                                  @RequestParam(required = false) Integer size) {
        return requestService.getAllRequest(userId, PaginationMapper.toMakePage(from, size));
    }

    @GetMapping("/{requestId}")
    public RequestWithItemDto getByRequestIdWithItem(@PathVariable Long requestId, @RequestHeader(name = USER_ID_HEADER) Long userId) {
        return requestService.getByRequestIdWithItem(requestId, userId);
    }

}

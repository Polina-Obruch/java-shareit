package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestAnswerDto {
    private Long id;

    private String description;

    private LocalDateTime created;
}

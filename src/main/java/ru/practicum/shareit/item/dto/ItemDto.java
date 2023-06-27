package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;

    @NotBlank(message = "Название предмета не может быть пустым")
    String name;

    @NotBlank(message = "Описание предмета не может быть пустым")
    String description;

    @NotNull(message = "Параметр available не может быть пустым")
    Boolean available;

    Long requestId;
}

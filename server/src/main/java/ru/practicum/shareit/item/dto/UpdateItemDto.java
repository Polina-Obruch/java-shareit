package ru.practicum.shareit.item.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}

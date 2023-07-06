package ru.practicum.shareit.user.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDto {
    private String name;
    private String email;
}

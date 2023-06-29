package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDto {
    private String name;

    @Email(message = "Введите правильный email")
    private String email;
}

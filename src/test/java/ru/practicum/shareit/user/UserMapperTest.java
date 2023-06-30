package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {
    @Spy
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private Long userId;
    private Long itemId;

    private User user;
    private Item item;
    private RequestUserDto requestUserDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;

        user = new User(
                userId,
                "John",
                "john.doe@mail.com");

        item = new Item(
                itemId,
                "name",
                "description",
                true,
                user,
                new BookingShortDto(2L, 2L),
                new BookingShortDto(3L, 2L),
                null,
                null);

        requestUserDto = new RequestUserDto("name", "description");
    }

    @Test
    void userToUserDto() {
        UserDto dto = userMapper.userToUserDto(user);

        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getName()).isEqualTo(user.getName());
    }

    @Test
    void userToUserDto_shouldReturnNull() {
        UserDto dto = userMapper.userToUserDto(null);
        assertThat(dto).isEqualTo(null);
    }

    @Test
    void requestUserDtoToUser() {
        User user1 = userMapper.requestUserDtoToUser(requestUserDto);
        assertThat(user1.getName()).isEqualTo(requestUserDto.getName());
    }

    @Test
    void userListToUserDtoList() {
        List<UserDto> list = userMapper.userListToUserDtoList(List.of(user));
        assertThat(list.get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void updateUserDtoToUser() {
        UpdateUserDto dto = new UpdateUserDto("newName", "new.doe@mail.com");
        User user1 = userMapper.updateUserDtoToUser(dto);
        assertThat(user1.getName()).isEqualTo(dto.getName());
    }
}

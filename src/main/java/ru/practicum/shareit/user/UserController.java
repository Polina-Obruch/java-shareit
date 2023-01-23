package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public User add(@Valid @RequestBody UserDto userDto) {
        //Обычно mapper используется на уровне контроллеров
        return userService.add(userMapper.userDtoToUser(userDto));
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable long id, @Valid @RequestBody UpdateUserDto userDto) {
        return userService.update(id, userMapper.updateUserDtoToUser(userDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.remove(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        return userService.getById(id);
    }
}

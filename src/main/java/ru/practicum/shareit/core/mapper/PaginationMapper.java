package ru.practicum.shareit.core.mapper;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.core.exception.ValidationException;

@NoArgsConstructor
public class PaginationMapper {
    public static Pageable toMakePage(Integer from, Integer size) {
        if (from == null || size == null) {
            return null;
        }

        if (size <= 0 || from < 0) {
            throw new ValidationException("Уточнчите правильность параметров отображения");
        }

        int page = from / size;
        return PageRequest.of(page, size);
    }
}

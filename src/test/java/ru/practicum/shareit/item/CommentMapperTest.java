package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {
    @Spy
    CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private Long commentId;
    private Long itemId;
    private Long userId;

    private User user;
    private Item item;
    private Request request;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentId = 1L;
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

        comment = Comment.builder()
                .id(commentId)
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void commentToCommentDto() {
        CommentDto dto = commentMapper.commentToCommentDto(comment);
        assertThat(dto.getId()).isEqualTo(comment.getId());
        assertThat(dto.getText()).isEqualTo(comment.getText());
        assertThat(dto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
    }

}

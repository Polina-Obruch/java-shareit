package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.dto.AddCommentDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment addCommentDtoToComment(AddCommentDto addCommentDto);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> commentListToCommentDtoList(List<Comment> comments);
}

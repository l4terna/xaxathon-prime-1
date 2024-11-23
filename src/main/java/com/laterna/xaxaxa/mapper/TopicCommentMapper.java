package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.TopicCommentCreateDto;
import com.laterna.xaxaxa.dto.TopicCommentResponseDto;
import com.laterna.xaxaxa.entity.TopicComment;
import com.laterna.xaxaxa.service.TopicService;
import com.laterna.xaxaxa.service.UserService;
import com.laterna.xaxaxa.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicCommentMapper {
    private final TopicService topicService;
    private final SecurityUtil userService;
    private final UserMapper userMapper;

    public TopicComment toEntity(TopicCommentCreateDto dto) {
        TopicComment comment = new TopicComment();
        comment.setContent(dto.getContent());
        comment.setTopic(topicService.getTopic(dto.getTopicId()));
        comment.setAuthor(userService.getCurrentUser());
        return comment;
    }

    public TopicCommentResponseDto toDto(TopicComment comment) {
        TopicCommentResponseDto dto = new TopicCommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUserDto(userMapper.toDto(comment.getAuthor()));
        return dto;
    }
}
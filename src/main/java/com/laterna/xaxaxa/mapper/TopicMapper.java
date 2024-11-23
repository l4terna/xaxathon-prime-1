package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.TopicCreateDto;
import com.laterna.xaxaxa.dto.TopicResponseDto;
import com.laterna.xaxaxa.entity.Topic;
import com.laterna.xaxaxa.service.SportEventService;
import com.laterna.xaxaxa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicMapper {
    private final SportEventService eventService;
    private final UserService userService;
    private final UserMapper userMapper;

    public Topic toEntity(TopicCreateDto dto) {
        Topic topic = new Topic();
        topic.setTitle(dto.getTitle());
        topic.setContent(dto.getContent());
        topic.setSportEvent(eventService.getEvent(dto.getSportEventId()));
        topic.setAuthor(userService.getCurrentUser());
        return topic;
    }

    public TopicResponseDto toDto(Topic topic) {
        TopicResponseDto dto = new TopicResponseDto();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setCreatedAt(topic.getCreatedAt());
        dto.setUserDto(userMapper.toDto(topic.getAuthor()));
        dto.setEventId(topic.getSportEvent().getId());
        dto.setCommentsCount(topic.getComments().size());
        return dto;
    }
}

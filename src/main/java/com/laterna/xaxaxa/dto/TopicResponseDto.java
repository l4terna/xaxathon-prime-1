package com.laterna.xaxaxa.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TopicResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private UserDto userDto;
    private Long eventId;
    private int commentsCount;
}

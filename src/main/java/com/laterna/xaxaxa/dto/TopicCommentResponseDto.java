package com.laterna.xaxaxa.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TopicCommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDto userDto;
}
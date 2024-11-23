package com.laterna.xaxaxa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicCommentCreateDto {
    @NotBlank(message = "Content is required")
    @Size(min = 2, message = "Comment must be at least 2 characters")
    private String content;

    @NotNull(message = "Topic ID is required")
    private Long topicId;
}

package com.laterna.xaxaxa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TopicCommentUpdateDto {
    @NotBlank(message = "Comment content cannot be empty")
    private String content;
}
package com.laterna.xaxaxa.dto;

import com.laterna.xaxaxa.entity.SportEvent;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private SportEventDto sportEvent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

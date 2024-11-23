package com.laterna.xaxaxa.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDetailDto {

    private Long id;
    private String name;
    private String description;
    private SportEventDto sportEvent; // Изменено на SportEventDto
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TeamMemberDto> members;
}
    
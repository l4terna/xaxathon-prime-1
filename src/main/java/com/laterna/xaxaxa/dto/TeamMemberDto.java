package com.laterna.xaxaxa.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberDto {
    
    private Long id;
    private Long teamId;
    private Long userId;
    private String role;
    private UserBasicInfoDto user;
    private LocalDateTime joinedAt;
}

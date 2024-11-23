package com.laterna.xaxaxa.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamInvitationDto {
    
    private Long id;
    private Long teamId;
    private Long inviterId;
    private Long inviteeId;
    private String status; // "PENDING", "ACCEPTED", "DECLINED"
    private String teamName;
    private String inviterName;
    private String inviteeName;
    private LocalDateTime invitedAt;
}

package com.laterna.xaxaxa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamInvitationCreateDto {
    
    @NotNull(message = "Идентификатор команды обязателен")
    private Long teamId;

    @NotNull(message = "Идентификатор приглашенного пользователя обязателен")
    private Long inviteeId;
}

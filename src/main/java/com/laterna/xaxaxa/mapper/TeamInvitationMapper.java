package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.TeamInvitationDto;
import com.laterna.xaxaxa.entity.TeamInvitation;
import org.springframework.stereotype.Component;

@Component
public class TeamInvitationMapper {

    /**
     * Преобразование сущности TeamInvitation в DTO.
     *
     * @param invitation Сущность TeamInvitation.
     * @return DTO для ответа.
     */
    public TeamInvitationDto toDto(TeamInvitation invitation) {
        if (invitation == null) {
            return null;
        }

        return TeamInvitationDto.builder()
                .id(invitation.getId())
                .teamId(invitation.getTeam().getId())
                .inviterId(invitation.getInviter().getId())
                .inviteeId(invitation.getInvitee().getId())
                .status(invitation.getStatus())
                .teamName(invitation.getTeam().getName())
                .inviterName(invitation.getInviter().getFirstName()) // Предполагается, что у User есть getFirstName()
                .inviteeName(invitation.getInvitee().getFirstName()) // Предполагается, что у User есть getFirstName()
                .invitedAt(invitation.getInvitedAt()) // Установка нового поля
                .build();
    }
}

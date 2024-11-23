package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.TeamMemberDto;
import com.laterna.xaxaxa.dto.UserBasicInfoDto;
import com.laterna.xaxaxa.entity.TeamMember;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper {
    
    /**
     * Преобразование сущности TeamMember в DTO.
     *
     * @param teamMember Сущность TeamMember.
     * @return DTO для ответа.
     */
    public TeamMemberDto toDto(TeamMember teamMember) {
        if (teamMember == null) {
            return null;
        }
        
        UserBasicInfoDto userDto = UserBasicInfoDto.builder()
                .id(teamMember.getUser().getId())
                .firstName(teamMember.getUser().getFirstName())
                .lastName(teamMember.getUser().getLastName())
                .email(teamMember.getUser().getEmail())
                .build();
        
        return TeamMemberDto.builder()
                .id(teamMember.getId())
                .teamId(teamMember.getTeam().getId())
                .userId(teamMember.getUser().getId())
                .role(teamMember.getRole())
                .user(userDto)
                .joinedAt(teamMember.getJoinedAt())
                .build();
    }
}

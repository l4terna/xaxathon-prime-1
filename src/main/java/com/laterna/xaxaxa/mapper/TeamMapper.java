package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.TeamCreateDto;
import com.laterna.xaxaxa.dto.TeamDetailDto;
import com.laterna.xaxaxa.dto.TeamResponseDto;
import com.laterna.xaxaxa.entity.Team;
import com.laterna.xaxaxa.service.SportEventService;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    private final SportEventService sportEventService;
    private final TeamMemberMapper teamMemberMapper;
    private final SportEventMapper sportEventMapper;

    public TeamMapper(SportEventService sportEventService, TeamMemberMapper teamMemberMapper, SportEventMapper sportEventMapper) {
        this.sportEventService = sportEventService;
        this.teamMemberMapper = teamMemberMapper;
        this.sportEventMapper = sportEventMapper;
    }

    /**
     * Преобразование DTO для создания команды в сущность Team.
     *
     * @param dto DTO для создания команды.
     * @return Сущность Team.
     */
    public Team toEntity(TeamCreateDto dto) {
        Team team = new Team();
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());
        team.setSportEvent(sportEventService.getEvent(dto.getSportEventId()));
        team.setCreatedAt(java.time.LocalDateTime.now());
        return team;
    }

    /**
     * Преобразование сущности Team в DTO для ответа.
     *
     * @param team Сущность Team.
     * @return DTO для ответа.
     */
    public TeamResponseDto toDto(Team team) {
        TeamResponseDto dto = new TeamResponseDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        dto.setSportEvent(sportEventMapper.toDto(team.getSportEvent()));
        dto.setCreatedAt(team.getCreatedAt());
        dto.setUpdatedAt(team.getUpdatedAt());
        return dto;
    }

    /**
     * Преобразование сущности Team в детализированный DTO.
     *
     * @param team Сущность Team.
     * @return Детализированный DTO для ответа.
     */
    public TeamDetailDto toDetailDto(Team team) {
        TeamDetailDto dto = new TeamDetailDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        dto.setSportEvent(sportEventMapper.toDto(team.getSportEvent()));
        dto.setCreatedAt(team.getCreatedAt());
        dto.setUpdatedAt(team.getUpdatedAt());
        dto.setMembers(team.getTeamMembers().stream()
                .map(teamMemberMapper::toDto)
                .collect(java.util.stream.Collectors.toList()));
        return dto;
    }
}

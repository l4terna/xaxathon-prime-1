package com.laterna.xaxaxa.controller;

import com.laterna.xaxaxa.dto.*;
import com.laterna.xaxaxa.entity.Team;
import com.laterna.xaxaxa.entity.TeamInvitation;
import com.laterna.xaxaxa.entity.User;
import com.laterna.xaxaxa.exception.ResourceNotFoundException;
import com.laterna.xaxaxa.mapper.TeamInvitationMapper;
import com.laterna.xaxaxa.mapper.TeamMapper;
import com.laterna.xaxaxa.mapper.TeamMemberMapper;
import com.laterna.xaxaxa.service.TeamService;
import com.laterna.xaxaxa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final TeamInvitationMapper teamInvitationMapper;
    private final TeamMemberMapper teamMemberMapper;
    private final UserService userService;

    /**
     * Создание новой команды.
     *
     * @param dto DTO для создания команды.
     * @return DTO созданной команды.
     */
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@Valid @RequestBody TeamCreateDto dto) {
        Team team = teamService.createTeam(dto);
        TeamResponseDto responseDto = teamMapper.toDto(team);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Отправка приглашения в команду.
     *
     * @param dto DTO для создания приглашения.
     * @return DTO созданного приглашения.
     */
    @PostMapping("/invitations")
    public ResponseEntity<TeamInvitationDto> sendInvitation(@Valid @RequestBody TeamInvitationCreateDto dto) {
        TeamInvitation invitation = teamService.sendInvitation(dto);
        TeamInvitationDto responseDto = teamInvitationMapper.toDto(invitation);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/invitations/{id}/accept")
    public ResponseEntity<Void> acceptInvitation(@PathVariable Long id) {
        teamService.acceptInvitation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invitations/{id}/decline")
    public ResponseEntity<Void> declineInvitation(@PathVariable Long id) {
        teamService.declineInvitation(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение всех команд для определенного спортивного мероприятия.
     *
     * @param sportEventId Идентификатор спортивного мероприятия.
     * @return Список DTO команд.
     */
    @GetMapping("/sport-event/{sportEventId}")
    public ResponseEntity<List<TeamResponseDto>> getTeamsBySportEvent(@PathVariable Long sportEventId) {
        List<Team> teams = teamService.getTeamsBySportEvent(sportEventId);
        List<TeamResponseDto> dtoList = teams.stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Получение членов команды.
     *
     * @param teamId Идентификатор команды.
     * @return Список DTO членов команды.
     */
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberDto>> getTeamMembers(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        List<TeamMemberDto> members = team.getTeamMembers().stream()
                .map(teamMemberMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    /**
     * Получение всех команд текущего пользователя.
     *
     * @return Список DTO команд.
     */
    @GetMapping("/my")
    public ResponseEntity<List<TeamResponseDto>> getMyTeams() {
        List<Team> teams = teamService.getTeamsForCurrentUser();
        List<TeamResponseDto> dtoList = teams.stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Получение всех приглашений в команды, которые пришли текущему пользователю.
     *
     * @return Список DTO приглашений.
     */
    @GetMapping("/invitations")
    public ResponseEntity<List<TeamInvitationDto>> getMyInvitations() {
        List<TeamInvitation> invitations = teamService.getInvitationsForCurrentUser();
        List<TeamInvitationDto> dtoList = invitations.stream()
                .map(teamInvitationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Получение детальной информации о команде.
     *
     * @param teamId Идентификатор команды.
     * @return DTO с детальной информацией о команде.
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailDto> getTeamDetail(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);

        // Проверяем, что текущий пользователь является членом команды
        User currentUser = userService.getCurrentUser();
        if (currentUser == null || !team.getTeamMembers().stream()
                .anyMatch(member -> member.getUser().equals(currentUser))) {
            throw new ResourceNotFoundException("Команда не найдена или доступ запрещен");
        }

        TeamDetailDto detailDto = teamMapper.toDetailDto(team);
        return ResponseEntity.ok(detailDto);
    }
}

package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.dto.*;
import com.laterna.xaxaxa.entity.*;
import com.laterna.xaxaxa.exception.ResourceNotFoundException;
import com.laterna.xaxaxa.repository.TeamInvitationRepository;
import com.laterna.xaxaxa.repository.TeamMemberRepository;
import com.laterna.xaxaxa.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final UserService userService;
    private final SportEventService sportEventService;

    /**
     * Создание новой команды.
     *
     * @param dto DTO для создания команды.
     * @return Созданная команда.
     */
    @Transactional
    public Team createTeam(TeamCreateDto dto) {
        if (teamRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Команда с таким названием уже существует");
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ResourceNotFoundException("Пользователь не найден или не аутентифицирован");
        }

        Team team = new Team();
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());
        team.setSportEvent(sportEventService.getEvent(dto.getSportEventId()));
        team.setCreatedAt(LocalDateTime.now());

        Team savedTeam = teamRepository.save(team);

        // Добавление текущего пользователя как лидера команды
        TeamMember leader = TeamMember.builder()
                .team(savedTeam)
                .user(currentUser)
                .role("LEADER")
                .joinedAt(LocalDateTime.now())
                .build();
        teamMemberRepository.save(leader);

        return savedTeam;
    }

    /**
     * Отправка приглашения в команду.
     *
     * @param dto DTO для создания приглашения.
     * @return Созданное приглашение.
     */
    @Transactional
    public TeamInvitation sendInvitation(TeamInvitationCreateDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Команда не найдена с id: " + dto.getTeamId()));

        User inviter = userService.getCurrentUser();
        if (inviter == null) {
            throw new ResourceNotFoundException("Пользователь не найден или не аутентифицирован");
        }

        if (!isTeamLeader(team, inviter)) {
            throw new IllegalArgumentException("Только лидеры команды могут отправлять приглашения");
        }

        // Извлечение пользователя из Optional или выброс исключения, если не найден
        User invitee = userService.getUserById(dto.getInviteeId())
                .orElseThrow(() -> new ResourceNotFoundException("Приглашенный пользователь не найден с id: " + dto.getInviteeId()));

        // Проверка, является ли пользователь уже членом команды
        if (teamMemberRepository.existsByTeamAndUser(team, invitee)) {
            throw new IllegalArgumentException("Пользователь уже является членом команды");
        }

        // Проверка, было ли уже отправлено приглашение этому пользователю
        if (teamInvitationRepository.findByTeamAndInvitee(team, invitee).isPresent()) {
            throw new IllegalArgumentException("Приглашение уже отправлено этому пользователю");
        }

        // Создание и сохранение приглашения
        TeamInvitation invitation = TeamInvitation.builder()
                .team(team)
                .inviter(inviter)
                .invitee(invitee)
                .status("PENDING")
                .invitedAt(LocalDateTime.now())
                .build();

        return teamInvitationRepository.save(invitation);
    }

    /**
     * Принятие приглашения в команду.
     *
     * @param invitationId Идентификатор приглашения.
     */
    @Transactional
    public void acceptInvitation(Long invitationId) {
        TeamInvitation invitation = teamInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Приглашение не найдено с id: " + invitationId));

        User currentUser = userService.getCurrentUser();
        if (currentUser == null || !invitation.getInvitee().equals(currentUser)) {
            throw new IllegalArgumentException("Вы не можете принять это приглашение");
        }

        if (!"PENDING".equals(invitation.getStatus())) {
            throw new IllegalArgumentException("Приглашение уже обработано");
        }

        TeamMember member = TeamMember.builder()
                .team(invitation.getTeam())
                .user(currentUser)
                .role("MEMBER")
                .joinedAt(LocalDateTime.now())
                .build();
        teamMemberRepository.save(member);

        invitation.setStatus("ACCEPTED");
        teamInvitationRepository.save(invitation);
    }

    /**
     * Отклонение приглашения в команду.
     *
     * @param invitationId Идентификатор приглашения.
     */
    @Transactional
    public void declineInvitation(Long invitationId) {
        TeamInvitation invitation = teamInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Приглашение не найдено с id: " + invitationId));

        User currentUser = userService.getCurrentUser();
        if (currentUser == null || !invitation.getInvitee().equals(currentUser)) {
            throw new IllegalArgumentException("Вы не можете отклонить это приглашение");
        }

        if (!"PENDING".equals(invitation.getStatus())) {
            throw new IllegalArgumentException("Приглашение уже обработано");
        }

        invitation.setStatus("DECLINED");
        teamInvitationRepository.save(invitation);
    }

    /**
     * Получение всех команд для определенного мероприятия.
     *
     * @param sportEventId Идентификатор спортивного мероприятия.
     * @return Список команд.
     */
    public List<Team> getTeamsBySportEvent(Long sportEventId) {
        SportEvent sportEvent = sportEventService.getEvent(sportEventId);
        return teamRepository.findBySportEvent(sportEvent);
    }

    /**
     * Получение команды по ее идентификатору.
     *
     * @param teamId Идентификатор команды.
     * @return Команда.
     */
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Команда не найдена с id: " + teamId));
    }

    /**
     * Проверка, является ли пользователь лидером команды.
     *
     * @param team Команда.
     * @param user Пользователь.
     * @return true, если пользователь является лидером, иначе false.
     */
    private boolean isTeamLeader(Team team, User user) {
        return teamMemberRepository.findByTeamAndUser(team, user)
                .map(member -> "LEADER".equalsIgnoreCase(member.getRole()))
                .orElse(false);
    }

    /**
     * Получение всех команд текущего пользователя.
     *
     * @return Список команд.
     */
    public List<Team> getTeamsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ResourceNotFoundException("Пользователь не найден или не аутентифицирован");
        }
        return teamMemberRepository.findByUser(currentUser)
                .stream()
                .map(TeamMember::getTeam)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех приглашений, которые пришли текущему пользователю.
     *
     * @return Список приглашений.
     */
    public List<TeamInvitation> getInvitationsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ResourceNotFoundException("Пользователь не найден или не аутентифицирован");
        }
        return teamInvitationRepository.findByInviteeAndStatus(currentUser, "PENDING");
    }
}

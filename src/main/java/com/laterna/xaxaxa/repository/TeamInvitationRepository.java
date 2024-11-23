package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.TeamInvitation;
import com.laterna.xaxaxa.entity.Team;
import com.laterna.xaxaxa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
    Optional<TeamInvitation> findByTeamAndInvitee(Team team, User invitee);
    List<TeamInvitation> findByInviteeAndStatus(User invitee, String status);
}

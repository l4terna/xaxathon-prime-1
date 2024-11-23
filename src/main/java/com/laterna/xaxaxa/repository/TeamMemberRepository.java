package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.TeamMember;
import com.laterna.xaxaxa.entity.Team;
import com.laterna.xaxaxa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    
    Optional<TeamMember> findByTeamAndUser(Team team, User user);
    
    List<TeamMember> findByTeam(Team team);
    
    boolean existsByTeamAndUser(Team team, User user);
    
    /**
     * Найти все записи TeamMember для данного пользователя.
     *
     * @param user Пользователь.
     * @return Список TeamMember.
     */
    List<TeamMember> findByUser(User user);
}

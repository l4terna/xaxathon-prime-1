package com.laterna.xaxaxa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_invitations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team_id", "invitee_id"}, name = "uq_team_invitations_team_id_invitee_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamInvitation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private User invitee;

    @Column(nullable = false, length = 20)
    private String status; // Например, "PENDING", "ACCEPTED", "DECLINED"

    @Column(name = "invited_at", nullable = false)
    private LocalDateTime invitedAt;
}

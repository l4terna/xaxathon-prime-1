-- changeset laterna:13
CREATE TABLE teams (
       id BIGSERIAL PRIMARY KEY,
       name VARCHAR(255) NOT NULL UNIQUE,
       description TEXT,
       sport_event_id BIGINT NOT NULL,
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NULL,
       CONSTRAINT fk_teams_sport_event_id FOREIGN KEY (sport_event_id)
           REFERENCES sport_events(id)
           ON DELETE CASCADE
);

-- changeset laterna:14
CREATE TABLE team_members (
          id BIGSERIAL PRIMARY KEY,
          team_id BIGINT NOT NULL,
          user_id BIGINT NOT NULL,
          role VARCHAR(50) NOT NULL,
          joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
          CONSTRAINT fk_team_members_team_id FOREIGN KEY (team_id)
              REFERENCES teams(id)
              ON DELETE CASCADE,
          CONSTRAINT fk_team_members_user_id FOREIGN KEY (user_id)
              REFERENCES users(id)
              ON DELETE CASCADE,
          CONSTRAINT uq_team_members_team_id_user_id UNIQUE (team_id, user_id)
);

-- changeset laterna:15
CREATE TABLE team_invitations (
              id BIGSERIAL PRIMARY KEY,
              team_id BIGINT NOT NULL,
              inviter_id BIGINT NOT NULL,
              invitee_id BIGINT NOT NULL,
              status VARCHAR(20) NOT NULL,
              invited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
              CONSTRAINT fk_team_invitations_team_id FOREIGN KEY (team_id)
                  REFERENCES teams(id)
                  ON DELETE CASCADE,
              CONSTRAINT fk_team_invitations_inviter_id FOREIGN KEY (inviter_id)
                  REFERENCES users(id)
                  ON DELETE CASCADE,
              CONSTRAINT fk_team_invitations_invitee_id FOREIGN KEY (invitee_id)
                  REFERENCES users(id)
                  ON DELETE CASCADE,
              CONSTRAINT uq_team_invitations_team_id_invitee_id UNIQUE (team_id, invitee_id)
);

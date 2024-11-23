
-- changeset laterna:6
CREATE TABLE notifications (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       notification_type VARCHAR(50) NOT NULL,
       sent BOOLEAN NOT NULL DEFAULT FALSE,
       sent_at TIMESTAMP,
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id)
);
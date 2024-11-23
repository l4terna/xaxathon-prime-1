
-- changeset laterna:5
CREATE TABLE user_settings (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
       theme VARCHAR(50) NOT NULL DEFAULT 'LIGHT',
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id)
);

-- changeset laterna:8
CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    sport_event_id BIGINT REFERENCES sport_events(id),
    user_id BIGINT REFERENCES users(id)
);

-- changeset laterna:9
CREATE TABLE topic_comments (
      id BIGSERIAL PRIMARY KEY,
      content TEXT NOT NULL,
      created_at TIMESTAMP NOT NULL,
      updated_at TIMESTAMP NOT NULL,
      topic_id BIGINT REFERENCES topics(id),
      user_id BIGINT REFERENCES users(id)
);

-- changeset laterna: 10
CREATE INDEX idx_topics_event_id ON topics(sport_event_id);
CREATE INDEX idx_comments_topic_id ON topic_comments(topic_id);
CREATE INDEX idx_topics_user_id ON topics(user_id);
CREATE INDEX idx_comments_user_id ON topic_comments(user_id);
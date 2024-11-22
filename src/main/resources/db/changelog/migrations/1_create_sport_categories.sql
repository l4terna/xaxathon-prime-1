-- changeset laterna:1
CREATE TABLE sport_categories (
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(255) NOT NULL UNIQUE
);
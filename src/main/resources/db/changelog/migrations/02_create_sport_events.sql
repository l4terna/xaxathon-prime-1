-- changeset laterna    :2
CREATE TABLE sport_events (
      id BIGSERIAL PRIMARY KEY,
      event_id VARCHAR(255),
      name VARCHAR(255),
      date_start DATE,
      date_end DATE,
      location VARCHAR(255),
      participants INT,
      description TEXT,
      category_id BIGINT,
      FOREIGN KEY (category_id) REFERENCES sport_categories(id) ON DELETE SET NULL
);

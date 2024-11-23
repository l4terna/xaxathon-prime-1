
-- changeset laterna:7
ALTER TABLE notifications
    ADD COLUMN title VARCHAR(255),
    ADD COLUMN message TEXT,
    ADD COLUMN additional_data TEXT;
ALTER TABLE tag_access
    ADD latitude DECIMAL(10, 8);

ALTER TABLE tag_access
    ADD longitude DECIMAL(11, 8);

ALTER TABLE tag_access
    ALTER COLUMN latitude SET NOT NULL;

ALTER TABLE tag_access
    ALTER COLUMN longitude SET NOT NULL;
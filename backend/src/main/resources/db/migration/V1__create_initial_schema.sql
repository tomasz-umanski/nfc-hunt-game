CREATE TABLE tag_access
(
    id                 UUID                        NOT NULL,
    creation_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_timestamp   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id            UUID                        NOT NULL,
    tag_location_id    UUID                        NOT NULL,
    CONSTRAINT pk_tag_access PRIMARY KEY (id)
);

CREATE TABLE tag_location_unlock_images
(
    id                 UUID                        NOT NULL,
    creation_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_timestamp   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    tag_location_id    UUID                        NOT NULL,
    image_filename     VARCHAR(64)                 NOT NULL,
    display_order      INTEGER                     NOT NULL,
    CONSTRAINT pk_tag_location_unlock_images PRIMARY KEY (id)
);

CREATE TABLE tag_locations
(
    id                      UUID                        NOT NULL,
    creation_timestamp      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_timestamp        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name                    VARCHAR(255)                NOT NULL,
    description             TEXT,
    nfc_tag_uuid            VARCHAR(512)                NOT NULL,
    latitude                DECIMAL(10, 8)              NOT NULL,
    longitude               DECIMAL(11, 8)              NOT NULL,
    locked_image_filename   VARCHAR(64)                 NOT NULL,
    unlocked_image_filename VARCHAR(64)                 NOT NULL,
    CONSTRAINT pk_tag_locations PRIMARY KEY (id)
);

CREATE TABLE tokens
(
    id                 UUID                        NOT NULL,
    creation_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_timestamp   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    token              VARCHAR(512)                NOT NULL,
    token_scheme       VARCHAR(20)                 NOT NULL,
    expiration_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revoked            BOOLEAN                     NOT NULL,
    device_id          VARCHAR(512)                NOT NULL,
    user_id            UUID                        NOT NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                 UUID                        NOT NULL,
    creation_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_timestamp   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    email              VARCHAR(100)                NOT NULL,
    password           VARCHAR(1024)               NOT NULL,
    role               VARCHAR(20)                 NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE tag_locations
    ADD CONSTRAINT uc_tag_locations_nfc_tag_uuid UNIQUE (nfc_tag_uuid);

ALTER TABLE tokens
    ADD CONSTRAINT uc_tokens_token UNIQUE (token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE tag_access
    ADD CONSTRAINT FK_TAG_ACCESS_ON_TAG_LOCATION FOREIGN KEY (tag_location_id) REFERENCES tag_locations (id);

ALTER TABLE tag_access
    ADD CONSTRAINT FK_TAG_ACCESS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE tag_location_unlock_images
    ADD CONSTRAINT FK_TAG_LOCATION_UNLOCK_IMAGES_ON_TAG_LOCATION FOREIGN KEY (tag_location_id) REFERENCES tag_locations (id);

ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
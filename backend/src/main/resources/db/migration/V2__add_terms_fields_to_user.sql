ALTER TABLE users
    ADD age_confirmed_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE users
    ADD game_rules_accepted_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE users
    ADD privacy_policy_accepted_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE users
    ALTER COLUMN age_confirmed_at SET NOT NULL;

ALTER TABLE users
    ALTER COLUMN game_rules_accepted_at SET NOT NULL;

ALTER TABLE users
    ALTER COLUMN privacy_policy_accepted_at SET NOT NULL;
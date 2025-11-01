CREATE TABLE account
(
    user_id         UUID                        DEFAULT gen_random_uuid() NOT NULL,
    email_address   TEXT                                                  NOT NULL,
    full_name       TEXT                                                  NOT NULL,
    password_hash   TEXT                                                  NOT NULL,
    registered_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()             NOT NULL,
    CONSTRAINT account_pkey PRIMARY KEY (user_id)
);

CREATE TABLE auth2fa
(
    user_id      UUID NOT NULL,
    secret       TEXT NOT NULL,
    backup_codes JSON NOT NULL,
    CONSTRAINT auth2fa_pkey PRIMARY KEY (user_id)
);

CREATE TABLE session
(
    session_code    UUID                        DEFAULT gen_random_uuid() NOT NULL,
    user_id         UUID                                                  NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE,
    created_date    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()             NOT NULL,
    CONSTRAINT session_pkey PRIMARY KEY (session_code)
);

ALTER TABLE account
    ADD CONSTRAINT account_email_address_key UNIQUE (email_address);
ALTER TABLE auth2fa
    ADD CONSTRAINT auth2fa_user_id_fkey FOREIGN KEY (user_id) REFERENCES account (user_id) ON DELETE CASCADE;
ALTER TABLE session
    ADD CONSTRAINT session_user_id_fkey FOREIGN KEY (user_id) REFERENCES account (user_id) ON DELETE CASCADE;
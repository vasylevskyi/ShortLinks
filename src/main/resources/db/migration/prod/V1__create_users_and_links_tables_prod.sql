CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(100) PRIMARY KEY,
    password_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS links (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    short_link VARCHAR(50) NOT NULL UNIQUE,
    original_link VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    valid_to TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL '30' DAY) NOT NULL,
    counter INTEGER DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

INSERT INTO users (user_id, password_hash) VALUES ('john.doe@mail.com', '$2a$12$abMl2.dUx57LDUqhbAh4aeAcsz/iNoX1jebmfSa40AJeC5pbR545i');

INSERT INTO links (user_id, short_link, original_link) VALUES
    ('john.doe@mail.com', 'ABCD1234', 'https://www.md5hashgenerator.com/');
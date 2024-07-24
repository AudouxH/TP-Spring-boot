CREATE TABLE IF NOT EXISTS sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_name VARCHAR(255) NOT NULL,
    session_desc TEXT,
    CONSTRAINT session_pk PRIMARY KEY (session_id)
);

CREATE TABLE IF NOT EXISTS speakers (
    speaker_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    company VARCHAR(255),
    speaker_bio TEXT,
    CONSTRAINT speaker_pk PRIMARY KEY (speaker_id)
);

CREATE TABLE IF NOT EXISTS session_speakers (
    session_id BIGINT NOT NULL,
    speaker_id BIGINT NOT NULL,
    PRIMARY KEY (session_id, speaker_id),
    CONSTRAINT fk_session FOREIGN KEY (session_id) REFERENCES sessions(session_id),
    CONSTRAINT fk_speaker FOREIGN KEY (speaker_id) REFERENCES speakers(speaker_id)
);

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
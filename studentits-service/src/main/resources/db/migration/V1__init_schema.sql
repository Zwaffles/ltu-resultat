CREATE TABLE student_account (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL UNIQUE,
    ltu_id VARCHAR(20) NOT NULL UNIQUE,
    personal_identity_number VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    university_email VARCHAR(150) NOT NULL UNIQUE,
    account_status VARCHAR(30) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
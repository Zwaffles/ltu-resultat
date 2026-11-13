CREATE TABLE assignment (
    id SERIAL PRIMARY KEY,
    course_code VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date TIMESTAMP NOT NULL
);

CREATE TABLE submission (
    id SERIAL PRIMARY KEY,
    assignment_id INT REFERENCES assignment(id) ON DELETE CASCADE,
    student_id INT NOT NULL,
    submission_text TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'SUBMITTED'
);
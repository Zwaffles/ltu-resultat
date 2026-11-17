-- Students as Canvas knows them. username is the link to StudentITS.
CREATE TABLE students (
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,   -- same format as StudentITS, e.g. "annand-1"
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL
);

-- A student's submission on an assignment, with the Canvas grade (omdöme).
CREATE TABLE submissions (
    id            BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT NOT NULL REFERENCES assignments(id),
    student_id    BIGINT NOT NULL REFERENCES students(id),
    grade         VARCHAR(10),                 -- Canvas grade: U/G/VG, NULL = not graded yet
    UNIQUE (assignment_id, student_id)         -- one submission per student and assignment
);
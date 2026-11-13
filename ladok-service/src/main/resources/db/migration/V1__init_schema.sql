CREATE TABLE registration (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    course_code VARCHAR(50) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grade_record (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    course_code VARCHAR(50) NOT NULL,
    module_code VARCHAR(50) NOT NULL,
    grade VARCHAR(10) NOT NULL,
    credits DECIMAL(4,1) NOT NULL,
    certified_date DATE NOT NULL,
    certified_by VARCHAR(100) NOT NULL
);
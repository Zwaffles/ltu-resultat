CREATE TABLE grades (
    id BIGSERIAL PRIMARY KEY,
    personnummer VARCHAR(20) NOT NULL,
    kurskod VARCHAR(20) NOT NULL,
    grade VARCHAR(10) NOT NULL,
    examination_date DATE NOT NULL
);
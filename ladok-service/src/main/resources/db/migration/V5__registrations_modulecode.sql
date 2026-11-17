-- 1. Lägg till modulkod i grades och sätt standard till '0001' (Tentamen)
ALTER TABLE grades ADD COLUMN modulkod VARCHAR(20);
UPDATE grades SET modulkod = '0001';
ALTER TABLE grades ALTER COLUMN modulkod SET NOT NULL;
ALTER TABLE grades ADD CONSTRAINT uq_grades_student_course_module
    UNIQUE (personnummer, kurskod, modulkod);

-- 2. Ny tabell för kursregistreringar
CREATE TABLE course_registrations (
    id BIGSERIAL PRIMARY KEY,
    personnummer VARCHAR(20) NOT NULL,
    kurskod VARCHAR(20) NOT NULL,
    UNIQUE (personnummer, kurskod)
);

-- 3. Ny tabell för Ladoks egna kopia av modulerna
CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    kurskod VARCHAR(20) NOT NULL,
    modulkod VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL,
    UNIQUE (kurskod, modulkod)
);

-- ==================== TESTDATA ====================

-- a) Modules (exakt samma som Epok, med D0031N 0003 satt till active = FALSE)
INSERT INTO modules (kurskod, modulkod, active) VALUES
    ('D0031N', '0001', true),
    ('D0031N', '0002', true),
    ('D0031N', '0003', false),
    ('D0031N', '0005', true),
    ('D0024E', '0001', true),
    ('D0024E', '0002', true),
    ('D0024E', '0003', true),
    ('I0015N', '0001', true),
    ('I0015N', '0002', true),
    ('I0015N', '0003', true),
    ('D0010E', '0001', true),
    ('D0010E', '0002', true),
    ('D0012E', '0001', true),
    ('D0012E', '0002', true),
    ('M0047M', '0001', true);

-- b) Course registrations: alla med befintliga resultat + 20020105-7890 på D0031N
INSERT INTO course_registrations (personnummer, kurskod)
SELECT DISTINCT personnummer, kurskod FROM grades;

INSERT INTO course_registrations (personnummer, kurskod) 
VALUES ('20020105-7890', 'D0031N')
ON CONFLICT (personnummer, kurskod) DO NOTHING;
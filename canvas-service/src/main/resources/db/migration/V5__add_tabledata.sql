-- 1. Studenter i Canvas (synkade med usernames i StudentITS)
INSERT INTO students (username, first_name, last_name) VALUES
    ('annand-1', 'Anna', 'Andersson'),
    ('johjoh-2', 'Johan', 'Johansson'),
    ('elikar-3', 'Elin', 'Karlsson'),
    ('marber-4', 'Marcus', 'Bergström'),
    ('sarlin-5', 'Sara', 'Lindqvist'),
    ('oscnil-6', 'Oscar', 'Nilsson'),
    ('majols-7', 'Maja', 'Olsson'),
    ('vikpet-8', 'Viktor', 'Pettersson'),
    ('emmsve-9', 'Emma', 'Svensson');

-- 2. Inlämningar och omdömen i Canvas
-- Täcker in: VG, G, U, samt NULL (inlämnad men ännu ej rättad)
INSERT INTO submissions (assignment_id, student_id, grade)
SELECT a.id, s.id, v.grade
FROM (VALUES
    -- D0031N: Inlämningsuppgift 1: Mikrotjänster
    ('Inlämningsuppgift 1: Mikrotjänster', 'annand-1', 'VG'),
    ('Inlämningsuppgift 1: Mikrotjänster', 'johjoh-2', 'G'),
    ('Inlämningsuppgift 1: Mikrotjänster', 'elikar-3', 'VG'),
    ('Inlämningsuppgift 1: Mikrotjänster', 'marber-4', 'U'),
    ('Inlämningsuppgift 1: Mikrotjänster', 'oscnil-6', NULL),

    -- D0031N: Inlämningsuppgift 2: API-integration & Säkerhet
    ('Inlämningsuppgift 2: API-integration & Säkerhet', 'annand-1', 'G'),
    ('Inlämningsuppgift 2: API-integration & Säkerhet', 'johjoh-2', NULL),
    ('Inlämningsuppgift 2: API-integration & Säkerhet', 'elikar-3', 'VG'),

    -- D0024E: Laboration 2: Docker & Flyway
    ('Laboration 2: Docker & Flyway', 'annand-1', 'G'),
    ('Laboration 2: Docker & Flyway', 'johjoh-2', 'U'),
    ('Laboration 2: Docker & Flyway', 'elikar-3', 'VG'),
    ('Laboration 2: Docker & Flyway', 'majols-7', NULL),

    -- I0015N: Projektrapport
    ('Projektrapport', 'annand-1', 'VG'),
    ('Projektrapport', 'marber-4', 'G'),
    ('Projektrapport', 'sarlin-5', NULL)
) AS v(assignment_title, username, grade)
JOIN assignments a ON a.title = v.assignment_title
JOIN students s ON s.username = v.username;
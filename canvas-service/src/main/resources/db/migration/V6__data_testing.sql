-- Canvas student with no account in StudentITS (demo: unknown student)
INSERT INTO students (username, first_name, last_name) VALUES
    ('okand-10', 'Kalle', 'Okänd');

-- oscnil-6 gets a grade: normal case, can be registered
UPDATE submissions SET grade = 'G'
WHERE assignment_id = (SELECT id FROM assignments WHERE title = 'Inlämningsuppgift 1: Mikrotjänster')
  AND student_id    = (SELECT id FROM students    WHERE username = 'oscnil-6');

-- More submissions on the demo assignment, one per demo case
INSERT INTO submissions (assignment_id, student_id, grade)
SELECT a.id, s.id, v.grade
FROM (VALUES
    ('majols-7', 'VG'),   -- registrerad
    ('vikpet-8', 'G'),    -- hinder: inactive account
    ('sarlin-5', 'G'),    -- hinder: not registered on D0031N
    ('okand-10', 'G'),    -- hinder: unknown in StudentITS
    ('emmsve-9', NULL)    -- not graded
) AS v(username, grade)
JOIN assignments a ON a.title = 'Inlämningsuppgift 1: Mikrotjänster'
JOIN students    s ON s.username = v.username;
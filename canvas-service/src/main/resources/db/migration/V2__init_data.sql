-- Lägg till exempel-inlämningsuppgifter
INSERT INTO assignment (course_code, title, description, due_date) VALUES 
('M7011E', 'Inlämningsuppgift 1: ER-modell', 'Skapa en komplett ER-modell för databasarkitektur.', '2026-06-01 23:59:00'),
('D0012E', 'Laboration 1: Java OOP', 'Implementera arv och polymorfism i Java.', '2026-06-05 23:59:00');

-- Lägg till exempel-inlämningar från studenter
INSERT INTO submission (assignment_id, student_id, submission_text, status) VALUES 
(1, 1, 'Här är länk till min ER-modell på GitHub.', 'GRADED'),
(1, 2, 'Bifogar PDF med ER-diagram.', 'SUBMITTED'),
(2, 2, 'Kod finns i repot.', 'SUBMITTED');
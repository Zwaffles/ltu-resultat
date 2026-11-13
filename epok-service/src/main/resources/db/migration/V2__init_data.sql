-- Lägg till grundläggande kurser
INSERT INTO course (course_code, name, credits) VALUES 
('M7011E', 'Databasarkitektur', 7.5),
('D0012E', 'Objektorienterad programmering', 7.5),
('I0007N', 'Programvaruteknik', 7.5);

-- Lägg till några studenter
INSERT INTO student (first_name, last_name, email) VALUES 
('Anna', 'Andersson', 'anna.andersson@ltu.se'),
('Johan', 'Johansson', 'johan.johansson@ltu.se'),
('Elin', 'Karlsson', 'elin.karlsson@ltu.se');

-- Koppla ihop studenter och kurser med resultat/betyg
INSERT INTO result (student_id, course_id, grade, examination_date) VALUES 
(1, 1, 'VG', '2026-06-01'),
(2, 2, 'G', '2026-06-01'),
(3, 3, '5', '2026-06-02');
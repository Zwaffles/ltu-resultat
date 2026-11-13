-- Lägg till kursregistreringar
INSERT INTO registration (student_id, course_code, semester) VALUES 
(1, 'M7011E', 'VT26'),
(2, 'M7011E', 'VT26'),
(2, 'D0012E', 'VT26'),
(3, 'I0007N', 'VT26');

-- Lägg till officiellt attesterade betyg
INSERT INTO grade_record (student_id, course_code, module_code, grade, credits, certified_date, certified_by) VALUES 
(1, 'M7011E', '0001', 'VG', 7.5, '2026-06-05', 'Examinator Exempelsson'),
(2, 'D0012E', '0001', 'G', 7.5, '2026-06-06', 'Examinator Exempelsson'),
(3, 'I0007N', '0001', '5', 7.5, '2026-06-07', 'Lärare Lärarsson');
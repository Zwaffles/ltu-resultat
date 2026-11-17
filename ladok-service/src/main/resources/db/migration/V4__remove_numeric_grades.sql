DELETE FROM grades
WHERE grade NOT IN ('U', 'G', 'VG');

ALTER TABLE grades
ADD CONSTRAINT chk_valid_grade CHECK (grade IN ('U', 'G', 'VG'));
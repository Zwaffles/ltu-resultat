-- Ta bort registrering för sarlin-5 (20020105-7890) på D0031N så det ger hinder
DELETE FROM course_registrations 
WHERE personnummer = '20020105-7890' AND kurskod = 'D0031N';

-- Lägg till registrering för oscnil-6 (20030414-2345) på D0031N så registrering lyckas
INSERT INTO course_registrations (personnummer, kurskod) 
VALUES ('20030414-2345', 'D0031N')
ON CONFLICT (personnummer, kurskod) DO NOTHING;
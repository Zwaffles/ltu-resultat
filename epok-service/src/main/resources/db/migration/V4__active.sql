ALTER TABLE modules ADD COLUMN aktiv BOOLEAN NOT NULL DEFAULT TRUE;
UPDATE modules SET aktiv = FALSE
    WHERE kurskod = 'D0031N' AND modulkod = '0003';
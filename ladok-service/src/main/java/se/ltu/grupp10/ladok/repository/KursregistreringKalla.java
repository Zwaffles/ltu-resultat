package se.ltu.grupp10.ladok.repository;

public interface KursregistreringKalla {
    boolean arRegistrerad(String personnummer, String kurskod);
}

/*
KursregistreringKalla är ett gränssnitt med en enda fråga: är en student med ett visst personnummer registrerad på en viss kurs?
Reglerna i ResultatService använder det för att avgöra om ett resultat får registreras.
Eftersom tjänsten bara känner till gränssnittet och inte hur svaret tas fram kan implementationen bytas ut, från minnesdata nu till en databasfråga senare, utan att reglerna behöver ändras.
 */

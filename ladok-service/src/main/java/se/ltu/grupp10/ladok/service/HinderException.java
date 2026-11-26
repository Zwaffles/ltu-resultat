package se.ltu.grupp10.ladok.service;

public class HinderException extends RuntimeException {
    public HinderException(String orsak) {
        super(orsak);
    }
}

/*
HinderException är ett eget undantag som representerar att Ladok vägrar registrera ett resultat på grund av en affärsregel, till exempel att studenten inte är registrerad eller att datumet ligger i framtiden.
Orsakstexten skickas med som meddelande.
Genom att skilja detta från andra fel kan FelHanterare svara med status “hinder” och HTTP 422 i stället för ett allmänt serverfel.
 */

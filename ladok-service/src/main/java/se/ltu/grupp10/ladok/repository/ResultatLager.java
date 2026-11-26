package se.ltu.grupp10.ladok.repository;

import java.util.List;
import se.ltu.grupp10.ladok.web.ResultatDto;

public interface ResultatLager {
    boolean finns(String personnummer, String kurskod, String modul);
    void spara(ResultatDto resultat);
    List<ResultatDto> hamta(String kurskod, String modul);
}

/*
ResultatLager är ett gränssnitt för det som rör lagrade resultat.
Det har tre metoder: finns kontrollerar om ett resultat redan är registrerat (för att upptäcka dubbletter), spara lagrar ett nytt resultat och hamta returnerar resultaten för en kurs och modul.
Det är den del av Ladok som i slutändan ska ligga mot databasen, men tjänstelagret är oberoende av hur lagringen sker.
 */


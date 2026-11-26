package se.ltu.grupp10.ladok.web;

import java.time.LocalDate;

public record ResultatDto(String personnummer, String kurskod,
                          String modul, LocalDate datum, String betyg) {}

/*
ResultatDto är en record som representerar ett studieresultat i den form som skickas mellan tjänsterna.
Den innehåller personnummer, kurskod, modulkod, examinationsdatum och betyg.
Samma klass används för det som Canvas skickar in vid registrering (POST) och för de resultat som returneras när någon listar redan registrerade resultat (GET).
Jackson omvandlar automatiskt mellan JSON och objektet, och datumet läses som ÅÅÅÅ-MM-DD. Som record är klassen oföränderlig, vilket passar en DTO som bara ska bära data.
 */

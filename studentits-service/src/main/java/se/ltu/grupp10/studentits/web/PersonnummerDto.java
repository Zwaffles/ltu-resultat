package se.ltu.grupp10.studentits.web;

public record PersonnummerDto(String anvandarnamn, String personnummer, boolean aktiv) {}

/*
PersonnummerDto är en record som representerar svaret från get_Persnummer.
Den innehåller studentens användarnamn, personnummer och om studenten är aktiv, vilket följer API-kontraktet.
Jackson omvandlar objektet automatiskt till JSON, och som record är klassen oföränderlig, vilket passar en DTO som bara bär data.
 */

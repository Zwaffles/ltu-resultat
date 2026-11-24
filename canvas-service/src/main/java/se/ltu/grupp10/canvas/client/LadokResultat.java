package se.ltu.grupp10.canvas.client;

import java.time.LocalDate;

/** The body sent to POST /ladok/resultat, following Ladok's contract. */
public record LadokResultat(String personnummer, String kurskod, String modul,
                            LocalDate datum, String betyg) {
}
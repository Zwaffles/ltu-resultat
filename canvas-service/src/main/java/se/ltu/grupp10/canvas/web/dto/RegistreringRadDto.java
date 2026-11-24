package se.ltu.grupp10.canvas.web.dto;

import java.time.LocalDate;

/** One student row from the GUI: who, which date, which grade. */
public record RegistreringRadDto(String anvandarnamn, LocalDate datum, String betyg) {
}
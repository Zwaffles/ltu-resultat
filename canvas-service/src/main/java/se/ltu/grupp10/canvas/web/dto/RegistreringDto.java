package se.ltu.grupp10.canvas.web.dto;

import java.util.List;

/** The request to reg_Student_Resultat: one module and the rows to register. */
public record RegistreringDto(String modul, List<RegistreringRadDto> rader) {
}
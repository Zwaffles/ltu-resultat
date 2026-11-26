package se.ltu.grupp10.ladok.web;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatusDto(String status, String orsak) {}

/*
StatusDto är en record som utgör Ladoks svar på en registrering.
Den har ett fält status (“registrerad” eller “hinder”) och ett valfritt fält orsak som förklarar varför ett resultat nekades.
Annoteringen @JsonInclude(NON_NULL) gör att orsak utelämnas ur JSON när den saknar värde, så ett lyckat svar blir bara {"status":"registrerad"}.
Klassen används både för lyckade svar och för felsvar, vilket ger alla tjänster ett gemensamt felformat.
 */

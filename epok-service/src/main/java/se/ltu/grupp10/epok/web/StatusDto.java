package se.ltu.grupp10.epok.web;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatusDto(String status, String orsak) {}

/*
StatusDto är en record som utgör tjänstens felformat.
Den har ett fält status och ett valfritt fält orsak som förklarar vad som gick fel.
Annoteringen @JsonInclude(NON_NULL) gör att orsak utelämnas ur JSON när den saknar värde.
Klassen finns som en egen kopia i varje tjänst, eftersom tjänsterna är fristående och inte delar kod, men formatet är detsamma i alla fyra systemen.
 */

package se.ltu.grupp10.canvas.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/** The outcome for one row: "registrerad", "hinder" or "fel", with a reason when relevant. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RadStatusDto(String anvandarnamn, String status, String orsak) {

    public static RadStatusDto registered(String anvandarnamn) {
        return new RadStatusDto(anvandarnamn, "registrerad", null);
    }

    public static RadStatusDto hinder(String anvandarnamn, String orsak) {
        return new RadStatusDto(anvandarnamn, "hinder", orsak);
    }

    public static RadStatusDto error(String anvandarnamn, String orsak) {
        return new RadStatusDto(anvandarnamn, "fel", orsak);
    }
}
package se.ltu.grupp10.gui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Modul(String modulkod, String benamning) {

    @Override
    public String toString() {
        return modulkod + "-" + benamning;
    }
}


package se.ltu.grupp10.gui.model;

public record Modul(String modulkod, String benamning) {

    @Override
    public String toString() {
        return modulkod + "-" + benamning;
    }
}


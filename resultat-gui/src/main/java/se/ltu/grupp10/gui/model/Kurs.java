package se.ltu.grupp10.gui.model;

public record Kurs(String kurskod) {

    @Override
    public String toString() {
        return kurskod;
    }
}

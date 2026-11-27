package se.ltu.grupp10.gui.model;

public record Uppgift(Long id, String namn) {

    @Override
    public String toString() {
        return namn;
    }
}

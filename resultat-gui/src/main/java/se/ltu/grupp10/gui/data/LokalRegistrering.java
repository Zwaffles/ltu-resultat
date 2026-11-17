package se.ltu.grupp10.gui.data;

import java.time.LocalDate;

public class LokalRegistrering {

    public String provregistrera(String betyg, LocalDate datum) {
        if (betyg == null || betyg.isBlank()) {
            return "Betyg saknas";
        }

        if (datum == null) {
            return "Datum saknas";
        }

        return "Registrerad";
    }
}
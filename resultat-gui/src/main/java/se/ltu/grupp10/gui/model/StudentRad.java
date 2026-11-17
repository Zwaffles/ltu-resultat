package se.ltu.grupp10.gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class StudentRad {

    private final String namn;
    private final String canvasOmdome;
    private final StringProperty ladokBetyg = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> examinationsdatum = new SimpleObjectProperty<>();
    private final BooleanProperty markerad = new SimpleBooleanProperty(false);
    private final StringProperty registreringsstatus = new SimpleStringProperty();

    public StudentRad(String namn, String canvasOmdome) {
        this.namn = namn;
        this.canvasOmdome = canvasOmdome;

        ladokBetyg.addListener((egenskap, gammaltBetyg, nyttBetyg) ->
                registreringsstatus.set(null));
        examinationsdatum.addListener((egenskap, gammaltDatum, nyttDatum) ->
                registreringsstatus.set(null));
    }

    public String namn() {
        return namn;
    }

    public String canvasOmdome() {
        return canvasOmdome;
    }



    public StringProperty ladokBetygProperty() {
        return ladokBetyg;
    }

    public ObjectProperty<LocalDate> examinationsdatumProperty() {
        return examinationsdatum;
    }
    public BooleanProperty markeradProperty() {
        return markerad;
    }
    public StringProperty registreringsstatusProperty() {
        return registreringsstatus;
    }
}

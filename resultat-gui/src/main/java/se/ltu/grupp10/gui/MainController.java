package se.ltu.grupp10.gui;

import javafx.fxml.FXML;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import se.ltu.grupp10.gui.model.Modul;
import se.ltu.grupp10.gui.model.StudentRad;
import java.util.List;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import se.ltu.grupp10.gui.data.LokalaUppgifter;
import se.ltu.grupp10.gui.data.LokalaStudenter;
import se.ltu.grupp10.gui.data.LokalaModuler;
import javafx.collections.FXCollections;
import se.ltu.grupp10.gui.data.LokalaBetyg;
import java.time.LocalDate;
import se.ltu.grupp10.gui.cell.ExaminationsdatumCell;
import se.ltu.grupp10.gui.cell.LadokBetygCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import se.ltu.grupp10.gui.data.LokalRegistrering;

/** Controller for main-view.fxml. Role D builds the GUI logic here. */
public class MainController {

    private String hamtadKurskod;
    private final LokalaUppgifter lokalaUppgifter = new LokalaUppgifter();
    private final LokalaStudenter lokalaStudenter = new LokalaStudenter();
    private final LokalaModuler lokalaModuler = new LokalaModuler();
    private final LokalRegistrering lokalRegistrering = new LokalRegistrering();

    @FXML
    private TextField kurskodFalt;
    @FXML
    private ComboBox<String> uppgiftVal;
    @FXML
    private ComboBox<Modul> modulVal;
    @FXML
    private TableView<StudentRad> studentTabell;
    @FXML
    private TableColumn<StudentRad, String> studentKolumn;
    @FXML
    private TableColumn<StudentRad, String> canvasOmdomeKolumn;
    @FXML
    private TableColumn<StudentRad, String> ladokBetygKolumn;
    @FXML
    private TableColumn<StudentRad, LocalDate> examinationsdatumKolumn;
    @FXML
    private TableColumn<StudentRad, Boolean> markeradKolumn;
    @FXML
    private TableColumn<StudentRad, String> statusKolumn;
    @FXML
    private DatePicker gemensamtDatum;

    @FXML
    private void initialize() {
        studentKolumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().namn()));
        canvasOmdomeKolumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().canvasOmdome()));
        ladokBetygKolumn.setCellValueFactory(cell ->
                cell.getValue().ladokBetygProperty());
        examinationsdatumKolumn.setCellValueFactory(cell ->
                cell.getValue().examinationsdatumProperty());
        examinationsdatumKolumn.setCellFactory(kolumn -> new ExaminationsdatumCell());
        markeradKolumn.setCellValueFactory(cell -> cell.getValue().markeradProperty());
        markeradKolumn.setCellFactory(CheckBoxTableCell.forTableColumn(markeradKolumn));
        statusKolumn.setCellValueFactory(cell ->
                cell.getValue().registreringsstatusProperty());
        statusKolumn.setEditable(false);
        modulVal.valueProperty().addListener((egenskap, tidigareModul, nyModul) -> {
            for (StudentRad rad : studentTabell.getItems()) {
                rad.registreringsstatusProperty().set(null);
            }
        });

        try {
            List<String> betyg = new LokalaBetyg().hamtaAlla();

            var betygAlternativ = FXCollections.observableArrayList(betyg);
            ladokBetygKolumn.setCellFactory(kolumn -> new LadokBetygCell(betygAlternativ));
            ladokBetygKolumn.setEditable(false);

            studentKolumn.setEditable(false);
            canvasOmdomeKolumn.setEditable(false);
            studentTabell.setEditable(true);
        } catch (IOException ex) {
            System.err.println("Kunde inte läsa betyg: " + ex.getMessage());
        }
    }

    @FXML
    private void hamtaUppgifter() {
        String kurskod = kurskodFalt.getText().trim();

        uppgiftVal.setValue(null);
        studentTabell.getItems().clear();
        hamtadKurskod = kurskod;
        hamtaModuler(kurskod);

        try {
            List<String> uppgifter = lokalaUppgifter.forKurs(kurskod);
            uppgiftVal.getItems().setAll(uppgifter);

            if (uppgifter.isEmpty()) {
                uppgiftVal.setPromptText("Inga träffar");
            } else {
                uppgiftVal.setPromptText("Välj uppgift");
            }
        } catch (IOException ex) {
            uppgiftVal.getItems().clear();
            uppgiftVal.setPromptText("Kunde inte läsa uppgifterna");
        }
    }

    @FXML
    private void visaStudenter() {
        String uppgift = uppgiftVal.getValue();
        studentTabell.getItems().clear();

        if (uppgift == null) {
            return;
        }

        try {
            List<StudentRad> studenter = lokalaStudenter.forUppgift(hamtadKurskod, uppgift);
            studentTabell.getItems().setAll(studenter);
        } catch (IOException ex) {
            System.err.println("Kunde inte läsa studenter: " + ex.getMessage());
        }
    }

    private void hamtaModuler(String kurskod) {
        modulVal.setValue(null);

        try {
            List<Modul> moduler = lokalaModuler.forKurs(kurskod);
            modulVal.getItems().setAll(moduler);

            if (moduler.isEmpty()) {
                modulVal.setPromptText("Inga moduler");
            } else {
                modulVal.setPromptText("Välj modul");
            }
        } catch (IOException ex) {
            modulVal.getItems().clear();
            modulVal.setPromptText("Läsfel");
        }
    }
    @FXML
    private void markeraAlla() {
        for (StudentRad rad : studentTabell.getItems()) {
            rad.markeradProperty().set(true);
        }
    }
    @FXML
    private void sattDatum() {
        LocalDate datum = gemensamtDatum.getValue();

        if (datum == null) {
            new Alert(Alert.AlertType.INFORMATION, "Välj ett datum först.").showAndWait();
            return;
        }

        boolean datumSatt = false;

        for (StudentRad rad : studentTabell.getItems()) {
            if (rad.markeradProperty().get()) {
                rad.examinationsdatumProperty().set(datum);
                datumSatt = true;
            }
        }

        if (!datumSatt) {
            new Alert(Alert.AlertType.INFORMATION, "Markera minst en studentrad.").showAndWait();
        }
    }
    @FXML
    private void skickaMarkerade() {
        String kurskod = kurskodFalt.getText().trim();

        if (!kurskod.equalsIgnoreCase(hamtadKurskod)) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Klicka på Hämta uppgifter för den aktuella kurskoden.").showAndWait();
            return;
        }

        if (modulVal.getValue() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Välj en modul först.").showAndWait();
            return;
        }

        boolean harMarkeradeRader = false;

        for (StudentRad rad : studentTabell.getItems()) {
            if (rad.markeradProperty().get()) {
                harMarkeradeRader = true;

                String status = lokalRegistrering.provregistrera(
                        rad.ladokBetygProperty().get(),
                        rad.examinationsdatumProperty().get());

                rad.registreringsstatusProperty().set(status);
            }
        }

        if (!harMarkeradeRader) {
            new Alert(Alert.AlertType.INFORMATION, "Markera minst en studentrad.").showAndWait();
        }
    }
}

package se.ltu.grupp10.gui;

import javafx.fxml.FXML;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import se.ltu.grupp10.gui.model.Modul;
import se.ltu.grupp10.gui.model.StudentRad;
import java.util.List;
import java.io.IOException;
import javafx.scene.control.ComboBox;
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
import se.ltu.grupp10.gui.model.Kurs;
import javafx.concurrent.Task;
import se.ltu.grupp10.gui.client.CanvasKlient;
import java.net.URI;
import javafx.scene.control.Button;
import se.ltu.grupp10.gui.model.Uppgift;

/** Controller for main-view.fxml. Role D builds the GUI logic here. */
public class MainController {

    private String hamtadKurskod;
    private final CanvasKlient canvasKlient =
            new CanvasKlient(URI.create("http://localhost:8084"));
    private final LokalaStudenter lokalaStudenter = new LokalaStudenter();
    private final LokalaModuler lokalaModuler = new LokalaModuler();
    private final LokalRegistrering lokalRegistrering = new LokalRegistrering();

    @FXML
    private ComboBox<Kurs> kursVal;
    @FXML
    private Button hamtaUppgifterKnapp;
    @FXML
    private ComboBox<Uppgift> uppgiftVal;
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

        hamtaUppgifterKnapp.disableProperty().bind(
                kursVal.valueProperty().isNull().or(kursVal.disabledProperty())
        );

        kursVal.valueProperty().addListener(
                (egenskap, tidigareKurs, nyKurs) -> rensaKursdata()
        );

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

        hamtaKurser();
    }

    private void rensaKursdata() {
        hamtadKurskod = null;

        uppgiftVal.setValue(null);
        uppgiftVal.getItems().clear();
        uppgiftVal.setPromptText("Välj uppgift");

        studentTabell.getItems().clear();

        modulVal.setValue(null);
        modulVal.getItems().clear();
        modulVal.setPromptText("Välj modul");
    }

    @FXML
    private void visaStudenter() {
        Uppgift uppgift = uppgiftVal.getValue();
        studentTabell.getItems().clear();

        if (uppgift == null) {
            return;
        }

        try {
            List<StudentRad> studenter = lokalaStudenter.forUppgift(
                    hamtadKurskod, uppgift.namn());
            studentTabell.getItems().setAll(studenter);
        } catch (IOException ex) {
            System.err.println("Kunde inte läsa studenter: " + ex.getMessage());
        }
    }

    private void hamtaKurser() {
        kursVal.setDisable(true);
        kursVal.setPromptText("Hämtar kurser");

        Task<List<Kurs>> hamtning = new Task<>() {
            @Override
            protected List<Kurs> call() throws IOException, InterruptedException {
                return canvasKlient.hamtaKurser();
            }
        };

        hamtning.setOnSucceeded(event -> {
            List<Kurs> kurser = hamtning.getValue();
            kursVal.getItems().setAll(kurser);
            kursVal.setDisable(false);

            if (kurser.isEmpty()) {
                kursVal.setPromptText("Inga kurser");
            } else {
                kursVal.setPromptText("Välj kurs");
            }
        });

        hamtning.setOnFailed(event -> {
            kursVal.setDisable(false);
            kursVal.setPromptText("Läsfel");
            hamtning.getException().printStackTrace();

            new Alert(Alert.AlertType.ERROR,
                    "Kunde inte hämta kurser. Kontrollera att Canvas körs.")
                    .showAndWait();
        });

        Thread trad = new Thread(hamtning, "canvas-kurser");
        trad.setDaemon(true);
        trad.start();
    }

    @FXML
    private void hamtaUppgifter() {
        Kurs kurs = kursVal.getValue();

        if (kurs == null) {
            new Alert(Alert.AlertType.INFORMATION, "Välj en kurs först.").showAndWait();
            return;
        }

        rensaKursdata();
        kursVal.setDisable(true);
        uppgiftVal.setDisable(true);
        uppgiftVal.setPromptText("Hämtar...");

        Task<List<Uppgift>> hamtning = new Task<>() {
            @Override
            protected List<Uppgift> call() throws IOException, InterruptedException {
                return canvasKlient.hamtaUppgifter(kurs.kurskod());
            }
        };

        hamtning.setOnSucceeded(event -> {
            List<Uppgift> uppgifter = hamtning.getValue();
            hamtadKurskod = kurs.kurskod();
            uppgiftVal.getItems().setAll(uppgifter);
            uppgiftVal.setDisable(false);
            kursVal.setDisable(false);

            if (uppgifter.isEmpty()) {
                uppgiftVal.setPromptText("Inga träffar");
            } else {
                uppgiftVal.setPromptText("Välj uppgift");
            }

            hamtaModuler(kurs.kurskod());
        });

        hamtning.setOnFailed(event -> {
            kursVal.setDisable(false);
            uppgiftVal.setDisable(false);
            uppgiftVal.setPromptText("Läsfel");
            hamtning.getException().printStackTrace();

            new Alert(Alert.AlertType.ERROR,
                    "Kunde inte hämta uppgifter från Canvas.").showAndWait();
        });

        Thread trad = new Thread(hamtning, "canvas-uppgifter");
        trad.setDaemon(true);
        trad.start();
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
        Kurs kurs = kursVal.getValue();

        if (kurs == null || !kurs.kurskod().equalsIgnoreCase(hamtadKurskod)) {
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

package se.ltu.grupp10.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import se.ltu.grupp10.gui.cell.ExaminationsdatumCell;
import se.ltu.grupp10.gui.cell.LadokBetygCell;
import se.ltu.grupp10.gui.cell.StatusCell;
import se.ltu.grupp10.gui.client.CanvasKlient;
import se.ltu.grupp10.gui.client.EpokKlient;
import se.ltu.grupp10.gui.data.LokalaBetyg;
import se.ltu.grupp10.gui.model.Kurs;
import se.ltu.grupp10.gui.model.Modul;
import se.ltu.grupp10.gui.model.RadStatus;
import se.ltu.grupp10.gui.model.Registrering;
import se.ltu.grupp10.gui.model.RegistreringRad;
import se.ltu.grupp10.gui.model.Student;
import se.ltu.grupp10.gui.model.StudentRad;
import se.ltu.grupp10.gui.model.Uppgift;

/** Kopplar gränssnittet till Canvas och Epok via HTTP. */
public class MainController {

    private final CanvasKlient canvasKlient;
    private final EpokKlient epokKlient;
    private final BooleanProperty betygInlasta = new SimpleBooleanProperty(false);
    private String hamtadKurskod;

    @FXML
    private ComboBox<Kurs> kursVal;
    @FXML
    private Button hamtaUppgifterKnapp;
    @FXML
    private Button skickaMarkeradeKnapp;
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

    public MainController(CanvasKlient canvasKlient, EpokKlient epokKlient) {
        this.canvasKlient = canvasKlient;
        this.epokKlient = epokKlient;
    }

    @FXML
    private void initialize() {
        konfigureraKnappar();
        konfigureraTabell();
        konfigureraVal();
        laddaBetygsalternativ();
        hamtaKurser();
    }

    private void konfigureraKnappar() {
        hamtaUppgifterKnapp.disableProperty().bind(
                kursVal.valueProperty().isNull().or(kursVal.disabledProperty())
        );

        skickaMarkeradeKnapp.disableProperty().bind(
                kursVal.disabledProperty()
                        .or(uppgiftVal.valueProperty().isNull())
                        .or(modulVal.valueProperty().isNull())
                        .or(Bindings.isEmpty(studentTabell.getItems()))
                        .or(betygInlasta.not())
        );
    }

    private void konfigureraTabell() {
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
        statusKolumn.setCellFactory(kolumn -> new StatusCell());

        studentKolumn.setEditable(false);
        canvasOmdomeKolumn.setEditable(false);
        ladokBetygKolumn.setEditable(false);
        studentTabell.setEditable(true);
    }

    private void konfigureraVal() {
        kursVal.valueProperty().addListener(
                (egenskap, tidigareKurs, nyKurs) -> rensaKursdata());
        modulVal.valueProperty().addListener(
                (egenskap, tidigareModul, nyModul) -> rensaRegistreringsstatus());
    }

    private void laddaBetygsalternativ() {
        betygInlasta.set(false);
        try {
            List<String> betyg = new LokalaBetyg().hamtaAlla();
            if (betyg.isEmpty()) {
                throw new IOException("Betygsfilen innehåller inga betygsalternativ.");
            }

            var betygAlternativ = FXCollections.observableArrayList(betyg);
            ladokBetygKolumn.setCellFactory(kolumn -> new LadokBetygCell(betygAlternativ));
            betygInlasta.set(true);
        } catch (IOException ex) {
            Platform.runLater(() -> visaFel(
                    "Kunde inte läsa betygsalternativen. Kontrollera betyg.csv och starta om "
                            + "programmet. Registrering är avstängd tills felet är åtgärdat.", ex));
        }
    }

    private void rensaRegistreringsstatus() {
        for (StudentRad rad : studentTabell.getItems()) {
            rad.registreringsstatusProperty().set(null);
        }
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

        if (uppgift == null || hamtadKurskod == null) {
            studentTabell.setPlaceholder(new Label("Välj en uppgift"));
            return;
        }

        String kurskod = hamtadKurskod;
        kursVal.setDisable(true);
        uppgiftVal.setDisable(true);
        studentTabell.setPlaceholder(new Label("Hämtar studenter..."));

        Task<List<Student>> hamtning = new Task<>() {
            @Override
            protected List<Student> call() throws IOException, InterruptedException {
                return canvasKlient.hamtaStudenter(kurskod, uppgift.id());
            }
        };

        hamtning.setOnSucceeded(event -> {
            List<StudentRad> rader = hamtning.getValue().stream()
                    .map(student -> new StudentRad(
                            student.anvandarnamn(), student.namn(), student.omdome()))
                    .toList();

            studentTabell.getItems().setAll(rader);
            studentTabell.setPlaceholder(new Label("Inga studenter att visa"));
            kursVal.setDisable(false);
            uppgiftVal.setDisable(false);
        });

        hamtning.setOnFailed(event -> {
            kursVal.setDisable(false);
            uppgiftVal.setDisable(false);
            studentTabell.setPlaceholder(new Label("Kunde inte hämta studenter"));
            visaFel("Kunde inte hämta studenter från Canvas.", hamtning.getException());
        });

        startaTask(hamtning, "canvas-studenter");
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
            visaFel("Kunde inte hämta kurser. Kontrollera att Canvas körs.",
                    hamtning.getException());
        });

        startaTask(hamtning, "canvas-kurser");
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
            visaFel("Kunde inte hämta uppgifter från Canvas.", hamtning.getException());
        });

        startaTask(hamtning, "canvas-uppgifter");
    }

    private void hamtaModuler(String kurskod) {
        modulVal.setValue(null);
        modulVal.getItems().clear();
        modulVal.setDisable(true);
        modulVal.setPromptText("Hämtar...");

        Task<List<Modul>> hamtning = new Task<>() {
            @Override
            protected List<Modul> call() throws IOException, InterruptedException {
                return epokKlient.hamtaModuler(kurskod);
            }
        };

        hamtning.setOnSucceeded(event -> {
            List<Modul> moduler = hamtning.getValue();
            modulVal.getItems().setAll(moduler);
            modulVal.setDisable(moduler.isEmpty());
            kursVal.setDisable(false);
            uppgiftVal.setDisable(false);

            if (moduler.isEmpty()) {
                modulVal.setPromptText("Inga moduler");
            } else {
                modulVal.setPromptText("Välj modul");
            }
        });

        hamtning.setOnFailed(event -> {
            modulVal.setPromptText("Läsfel");
            kursVal.setDisable(false);
            uppgiftVal.setDisable(false);
            visaFel("Kunde inte hämta moduler från Epok.", hamtning.getException());
        });

        startaTask(hamtning, "epok-moduler");
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

        Modul modul = modulVal.getValue();
        if (modul == null) {
            new Alert(Alert.AlertType.INFORMATION, "Välj en modul först.").showAndWait();
            return;
        }

        List<StudentRad> markerade = studentTabell.getItems().stream()
                .filter(rad -> rad.markeradProperty().get())
                .toList();
        if (markerade.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Markera minst en studentrad.").showAndWait();
            return;
        }

        List<StudentRad> skickade = new ArrayList<>();
        List<RegistreringRad> rader = new ArrayList<>();
        for (StudentRad rad : markerade) {
            String fel = kontrolleraRad(rad);
            if (fel != null) {
                rad.registreringsstatusProperty().set(fel);
                continue;
            }

            skickade.add(rad);
            rader.add(new RegistreringRad(
                    rad.anvandarnamn(),
                    rad.examinationsdatumProperty().get(),
                    rad.ladokBetygProperty().get()));
        }

        if (rader.isEmpty()) {
            return;
        }

        Registrering begaran = new Registrering(modul.modulkod(), List.copyOf(rader));
        Parent rot = studentTabell.getScene().getRoot();
        rot.setDisable(true);
        skickade.forEach(rad -> rad.registreringsstatusProperty().set("Skickar..."));

        Task<List<RadStatus>> hamtning = new Task<>() {
            @Override
            protected List<RadStatus> call() throws IOException, InterruptedException {
                return canvasKlient.registrera(kurs.kurskod(), begaran);
            }
        };

        hamtning.setOnSucceeded(event -> {
            Map<String, String> statusPerStudent = new HashMap<>();
            for (RadStatus svar : hamtning.getValue()) {
                statusPerStudent.put(svar.anvandarnamn(), statusText(svar));
            }
            for (StudentRad rad : skickade) {
                rad.registreringsstatusProperty().set(statusPerStudent.get(rad.anvandarnamn()));
            }
            rot.setDisable(false);
        });

        hamtning.setOnFailed(event -> {
            skickade.forEach(rad -> rad.registreringsstatusProperty().set("Utfall okänt"));
            rot.setDisable(false);
            visaFel("Kunde inte bekräfta registreringen. Kontrollera resultatet i Ladok "
                    + "innan du skickar igen.", hamtning.getException());
        });

        startaTask(hamtning, "canvas-registrering");
    }

    private static void startaTask(Task<?> task, String tradnamn) {
        Thread trad = new Thread(task, tradnamn);
        trad.setDaemon(true);
        trad.start();
    }

    private static void visaFel(String meddelande, Throwable orsak) {
        orsak.printStackTrace();
        new Alert(Alert.AlertType.ERROR, meddelande).showAndWait();
    }

    private static String kontrolleraRad(StudentRad rad) {
        if (rad.anvandarnamn() == null || rad.anvandarnamn().isBlank()) {
            return "Användarnamn saknas";
        }
        if (rad.ladokBetygProperty().get() == null || rad.ladokBetygProperty().get().isBlank()) {
            return "Betyg saknas";
        }
        if (rad.examinationsdatumProperty().get() == null) {
            return "Datum saknas";
        }
        return null;
    }

    private static String statusText(RadStatus svar) {
        String text = switch (svar.status()) {
            case "registrerad" -> "Registrerad";
            case "hinder" -> "Kan inte registreras";
            case "fel" -> "Fel";
            default -> svar.status();
        };
        if (svar.orsak() != null && !svar.orsak().isBlank()) {
            return text + ": " + svar.orsak();
        }
        return text;
    }
}

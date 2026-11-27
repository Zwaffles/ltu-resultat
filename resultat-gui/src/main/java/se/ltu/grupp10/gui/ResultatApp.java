package se.ltu.grupp10.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import se.ltu.grupp10.gui.client.CanvasKlient;
import se.ltu.grupp10.gui.client.EpokKlient;
import se.ltu.grupp10.gui.config.GuiKonfiguration;

/** Resultat Canvas GUI: the teacher's view for reporting results to Ladok. */
public class ResultatApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        GuiKonfiguration konfiguration;
        try {
            konfiguration = GuiKonfiguration.lasIn();
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Kunde inte läsa anslutningsinställningarna. " + ex.getMessage()).showAndWait();
            Platform.exit();
            return;
        }

        MainController controller = new MainController(
                new CanvasKlient(konfiguration.canvasAdress()),
                new EpokKlient(konfiguration.epokAdress()));
        FXMLLoader loader = new FXMLLoader(ResultatApp.class.getResource("main-view.fxml"));
        loader.setControllerFactory(typ -> {
            if (typ == MainController.class) {
                return controller;
            }
            throw new IllegalArgumentException("Okänd controller: " + typ.getName());
        });
        Scene scene = new Scene(loader.load(), 1000, 650);
        stage.setTitle("Resultat Canvas GUI: rapportera betyg till Ladok");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

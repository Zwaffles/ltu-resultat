package se.ltu.grupp10.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Resultat Canvas GUI: the teacher's view for reporting results to Ladok. */
public class ResultatApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ResultatApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 650);
        stage.setTitle("Resultat Canvas GUI: rapportera betyg till Ladok");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

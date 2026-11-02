package se.ltu.grupp10.gui;

/**
 * Start class for running the GUI directly from IntelliJ (Run on this class).
 *
 * JavaFX refuses to start when the main class itself extends Application and
 * JavaFX is on the classpath ("JavaFX runtime components are missing").
 * Starting through a class that does not extend Application avoids that.
 * From the command line, use instead: mvn -pl resultat-gui javafx:run
 */
public final class Launcher {

    private Launcher() {
    }

    public static void main(String[] args) {
        ResultatApp.main(args);
    }
}

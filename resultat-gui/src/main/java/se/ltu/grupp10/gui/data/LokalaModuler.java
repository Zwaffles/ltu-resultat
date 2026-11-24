package se.ltu.grupp10.gui.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import se.ltu.grupp10.gui.model.Modul;

public class LokalaModuler {

    public List<Modul> forKurs(String kurskod) throws IOException {
        InputStream fil = LokalaModuler.class.getResourceAsStream("/moduler.csv");
        if (fil == null) {
            throw new IOException("Hittar inte moduler.csv.");
        }

        List<Modul> moduler = new ArrayList<>();

        try (BufferedReader lasare = new BufferedReader(
                new InputStreamReader(fil, StandardCharsets.UTF_8))) {
            lasare.readLine(); // Hoppa över rubrikraden.

            String rad;
            while ((rad = lasare.readLine()) != null) {
                String[] kolumner = rad.split(",", 3);

                if (kolumner.length == 3 && kolumner[0].trim().equalsIgnoreCase(kurskod)) {
                    moduler.add(new Modul(kolumner[1].trim(), kolumner[2].trim()));
                }
            }
        }

        return moduler;
    }
}

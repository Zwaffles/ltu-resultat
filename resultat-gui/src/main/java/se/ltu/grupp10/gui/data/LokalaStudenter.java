package se.ltu.grupp10.gui.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import se.ltu.grupp10.gui.model.StudentRad;

public class LokalaStudenter {

    public List<StudentRad> forUppgift(String kurskod, String uppgift) throws IOException {
        InputStream fil = LokalaStudenter.class.getResourceAsStream("/studenter.csv");
        if (fil == null) {
            throw new IOException("Hittar inte studenter.csv.");
        }

        List<StudentRad> studenter = new ArrayList<>();

        try (BufferedReader lasare = new BufferedReader(
                new InputStreamReader(fil, StandardCharsets.UTF_8))) {
            lasare.readLine(); // Hoppa över rubrikraden.

            String rad;
            while ((rad = lasare.readLine()) != null) {
                String[] kolumner = rad.split(",", -1);

                if (kolumner.length == 4
                        && kolumner[0].trim().equalsIgnoreCase(kurskod)
                        && kolumner[1].trim().equals(uppgift)) {
                    studenter.add(new StudentRad(kolumner[2].trim(), kolumner[3].trim()));
                }
            }
        }

        return studenter;
    }
}
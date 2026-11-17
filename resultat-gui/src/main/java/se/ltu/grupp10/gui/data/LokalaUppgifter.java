package se.ltu.grupp10.gui.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LokalaUppgifter {

    public List<String> forKurs(String kurskod) throws IOException {
        InputStream fil = LokalaUppgifter.class.getResourceAsStream("/uppgifter.csv");
        if (fil == null) {
            throw new IOException("Hittar inte uppgifter.csv.");
        }

        List<String> uppgifter = new ArrayList<>();

        try (BufferedReader lasare = new BufferedReader(
                new InputStreamReader(fil, StandardCharsets.UTF_8))) {
            lasare.readLine(); // Hoppa över rubrikraden.

            String rad;
            while ((rad = lasare.readLine()) != null) {
                String[] kolumner = rad.split(",", 2);

                if (kolumner.length == 2 && kolumner[0].trim().equalsIgnoreCase(kurskod)) {
                    uppgifter.add(kolumner[1].trim());
                }
            }
        }

        return uppgifter;
    }
}
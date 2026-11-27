package se.ltu.grupp10.gui.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LokalaBetyg {

    public List<String> hamtaAlla() throws IOException {
        InputStream fil = LokalaBetyg.class.getResourceAsStream("/betyg.csv");
        if (fil == null) {
            throw new IOException("Hittar inte betyg.csv.");
        }

        List<String> betyg = new ArrayList<>();

        try (BufferedReader lasare = new BufferedReader(
                new InputStreamReader(fil, StandardCharsets.UTF_8))) {
            lasare.readLine();

            String rad;
            while ((rad = lasare.readLine()) != null) {
                String alternativ = rad.trim();
                if (!alternativ.isEmpty()) {
                    betyg.add(alternativ);
                }
            }
        }

        return betyg;
    }
}

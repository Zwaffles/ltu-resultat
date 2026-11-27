package se.ltu.grupp10.gui.client;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.ltu.grupp10.gui.model.Modul;

public final class EpokKlient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final URI serveradress;
    private final HttpClient httpKlient;
    private final ObjectMapper json = new ObjectMapper();

    public EpokKlient(URI serveradress) {
        this.serveradress = serveradress;
        this.httpKlient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public List<Modul> hamtaModuler(String kurskod)
            throws IOException, InterruptedException {

        if (kurskod == null || kurskod.isBlank()) {
            throw new IllegalArgumentException("Välj en kurs.");
        }

        String kodForUrl = URLEncoder.encode(
                kurskod.strip(), StandardCharsets.UTF_8
        ).replace("+", "%20");

        URI adress = serveradress.resolve(
                "/epok/kurser/" + kodForUrl + "/moduler");

        HttpRequest anrop = HttpRequest.newBuilder(adress)
                .header("Accept", "application/json")
                .timeout(TIMEOUT)
                .GET()
                .build();

        HttpResponse<String> svar = httpKlient.send(
                anrop,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (svar.statusCode() != 200) {
            throw new IOException("Epok svarade med HTTP " + svar.statusCode()
                    + " vid hämtning av moduler för " + kurskod.strip());
        }

        return json.readValue(svar.body(), new TypeReference<List<Modul>>() {});
    }
}

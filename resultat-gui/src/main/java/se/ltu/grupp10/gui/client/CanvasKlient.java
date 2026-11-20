package se.ltu.grupp10.gui.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import se.ltu.grupp10.gui.model.Kurs;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import se.ltu.grupp10.gui.model.Uppgift;
import java.net.URLEncoder;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

public final class CanvasKlient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final URI serveradress;
    private final HttpClient httpKlient;
    private final ObjectMapper json = new ObjectMapper();

    public CanvasKlient(URI serveradress) {
        this.serveradress = serveradress;
        this.httpKlient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public List<Kurs> hamtaKurser() throws IOException, InterruptedException {
        String svar = hamtaJson("/canvas/kurser");
        return json.readValue(svar, new TypeReference<List<Kurs>>() {});
    }

    public List<Uppgift> hamtaUppgifter(String kurskod)
            throws IOException, InterruptedException {

        if (kurskod == null || kurskod.isBlank()) {
            throw new IllegalArgumentException("Välj en kurs.");
        }

        String kodForUrl = URLEncoder.encode(
                kurskod.strip(), StandardCharsets.UTF_8
        ).replace("+", "%20");

        String svar = hamtaJson("/canvas/kurser/" + kodForUrl + "/uppgifter");

        return json.readValue(svar, new TypeReference<List<Uppgift>>() {});
    }

    private String hamtaJson(String sokvag) throws IOException, InterruptedException {
        HttpRequest anrop = HttpRequest.newBuilder(serveradress.resolve(sokvag))
                .header("Accept", "application/json")
                .timeout(TIMEOUT)
                .GET()
                .build();

        HttpResponse<String> svar = httpKlient.send(
                anrop,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (svar.statusCode() != 200) {
            throw new IOException("Canvas svarade med HTTP " + svar.statusCode()
                    + " vid anrop till " + sokvag);
        }

        return svar.body();
    }
}

package se.ltu.grupp10.gui.client;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import se.ltu.grupp10.gui.model.Kurs;
import se.ltu.grupp10.gui.model.RadStatus;
import se.ltu.grupp10.gui.model.Registrering;
import se.ltu.grupp10.gui.model.RegistreringRad;
import se.ltu.grupp10.gui.model.Student;
import se.ltu.grupp10.gui.model.Uppgift;

public final class CanvasKlient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private static final Duration REGISTRERING_TIMEOUT = Duration.ofSeconds(30);

    private final URI serveradress;
    private final HttpClient httpKlient;
    private final ObjectMapper json = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

        String svar = hamtaJson(kursSokvag(kurskod) + "/uppgifter");
        return json.readValue(svar, new TypeReference<List<Uppgift>>() {});
    }

    public List<Student> hamtaStudenter(String kurskod, Long uppgiftId)
            throws IOException, InterruptedException {

        if (uppgiftId == null) {
            throw new IllegalArgumentException("Välj en uppgift.");
        }

        String adress = kursSokvag(kurskod)
                + "/uppgifter/" + uppgiftId + "/studenter";

        String svar = hamtaJson(adress);
        return json.readValue(svar, new TypeReference<List<Student>>() {});
    }

    public List<RadStatus> registrera(String kurskod, Registrering begaran)
            throws IOException, InterruptedException {

        if (begaran == null || begaran.modul() == null || begaran.modul().isBlank()
                || begaran.rader() == null || begaran.rader().isEmpty()) {
            throw new IllegalArgumentException("Ange modul och minst en studentrad.");
        }

        Set<String> forvantade = new HashSet<>();
        for (RegistreringRad rad : begaran.rader()) {
            if (rad == null || rad.anvandarnamn() == null || rad.anvandarnamn().isBlank()
                    || !forvantade.add(rad.anvandarnamn())) {
                throw new IllegalArgumentException(
                        "Varje studentrad måste ha ett unikt användarnamn.");
            }
        }

        URI adress = serveradress.resolve(kursSokvag(kurskod) + "/registrering");
        HttpRequest anrop = HttpRequest.newBuilder(adress)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .timeout(REGISTRERING_TIMEOUT)
                .POST(HttpRequest.BodyPublishers.ofString(
                        json.writeValueAsString(begaran), StandardCharsets.UTF_8))
                .build();

        List<RadStatus> svar = json.readValue(
                skickaAnrop(anrop), new TypeReference<List<RadStatus>>() {});
        kontrolleraSvar(forvantade, svar);
        return svar;
    }

    private static void kontrolleraSvar(Set<String> forvantade, List<RadStatus> svar)
            throws IOException {
        if (svar == null) {
            throw new IOException("Canvas returnerade inget registreringssvar.");
        }

        Set<String> mottagna = new HashSet<>();
        for (RadStatus rad : svar) {
            if (rad == null || rad.status() == null || rad.status().isBlank()) {
                throw new IOException("Canvas returnerade en ofullständig radstatus.");
            }
            if (!forvantade.contains(rad.anvandarnamn())
                    || !mottagna.add(rad.anvandarnamn())) {
                throw new IOException("Canvas returnerade en oväntad eller dubblerad studentrad.");
            }
        }

        if (!mottagna.equals(forvantade)) {
            throw new IOException("Canvas svar saknar en eller flera studentrader.");
        }
    }

    private static String kursSokvag(String kurskod) {
        if (kurskod == null || kurskod.isBlank()) {
            throw new IllegalArgumentException("Välj en kurs.");
        }

        String kodForUrl = URLEncoder.encode(
                kurskod.strip(), StandardCharsets.UTF_8
        ).replace("+", "%20");

        return "/canvas/kurser/" + kodForUrl;
    }

    private String hamtaJson(String sokvag) throws IOException, InterruptedException {
        HttpRequest anrop = HttpRequest.newBuilder(serveradress.resolve(sokvag))
                .header("Accept", "application/json")
                .timeout(TIMEOUT)
                .GET()
                .build();

        return skickaAnrop(anrop);
    }

    private String skickaAnrop(HttpRequest anrop) throws IOException, InterruptedException {
        HttpResponse<String> svar = httpKlient.send(
                anrop,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (svar.statusCode() != 200) {
            throw new IOException("Canvas svarade med HTTP " + svar.statusCode()
                    + " vid anrop till " + anrop.uri().getPath());
        }

        return svar.body();
    }
}

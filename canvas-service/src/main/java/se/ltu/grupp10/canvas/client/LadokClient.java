package se.ltu.grupp10.canvas.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Calls Ladok's reg_Resultat over REST and turns every outcome into a LadokSvar. */
@Component
public class LadokClient {

    private static final int UNPROCESSABLE = 422;

    private final RestClient ladok;

    public LadokClient(@Qualifier("ladokKlient") RestClient ladok) {
        this.ladok = ladok;
    }

    public LadokSvar registerResult(LadokResultat resultat) {
        try {
            ladok.post()
                    .uri("/resultat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resultat)
                    .retrieve()
                    .toBodilessEntity();
            return new LadokSvar("registrerad", null);
        } catch (HttpClientErrorException e) {
            String status = e.getStatusCode().value() == UNPROCESSABLE ? "hinder" : "fel";
            return new LadokSvar(status, readReason(e));
        } catch (RestClientException e) {
            return new LadokSvar("fel", "Ladok svarar inte");
        }
    }

    /** Reads "orsak" from Ladok's error body, falling back to the status code. */
    private static String readReason(HttpClientErrorException e) {
        try {
            LadokSvar svar = e.getResponseBodyAs(LadokSvar.class);
            if (svar != null && svar.orsak() != null) {
                return svar.orsak();
            }
        } catch (RuntimeException ignored) {
            // Body missing or not JSON: use the fallback below.
        }
        return "Ladok svarade med HTTP " + e.getStatusCode().value();
    }
}
package se.ltu.grupp10.canvas.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * GET /canvas/systemstatus checks that Canvas can reach Epok, StudentITS and Ladok.
 *
 * Example response:
 * {"Canvas":"UP","Epok":"UP","StudentITS":"UP","Ladok":"NOT REACHABLE"}
 *
 * Useful on integration day: if this works, the service-to-service calls
 * in reg_Student_Resultat have a working path.
 */
@RestController
public class SystemStatusController {

    private final RestClient epok;
    private final RestClient studentIts;
    private final RestClient ladok;

    public SystemStatusController(@Qualifier("epokKlient") RestClient epok,
                                  @Qualifier("studentItsKlient") RestClient studentIts,
                                  @Qualifier("ladokKlient") RestClient ladok) {
        this.epok = epok;
        this.studentIts = studentIts;
        this.ladok = ladok;
    }

    @GetMapping("/systemstatus")
    public Map<String, String> systemstatus() {
        Map<String, String> status = new LinkedHashMap<>();
        status.put("Canvas", "UP");
        status.put("Epok", check(epok));
        status.put("StudentITS", check(studentIts));
        status.put("Ladok", check(ladok));
        return status;
    }

    private String check(RestClient client) {
        try {
            client.get().uri("/health").retrieve().toBodilessEntity();
            return "UP";
        } catch (RestClientException e) {
            return "NOT REACHABLE";
        }
    }
}

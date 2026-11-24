package se.ltu.grupp10.ladok.web;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.ladok.service.ResultatService;

/*
ResultatController är REST-lagret för Ladok och tar emot HTTP-anropen.
Eftersom tjänstens context-path redan är /ladok mappas klassen till /resultat.
En POST tar emot ett ResultatDto, lämnar över det till ResultatService och svarar 201 med status “registrerad” om inget går fel.
En GET med kurskod och modul som parametrar returnerar de resultat som redan är registrerade.
Controllern innehåller ingen affärslogik, den översätter bara mellan HTTP och tjänstelagret.
 */

@RestController
@RequestMapping("/resultat")
public class ResultatController {

    private final ResultatService service;

    public ResultatController(ResultatService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StatusDto> registrera(@RequestBody ResultatDto resultat) {
        service.registrera(resultat);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StatusDto("registrerad", null));
    }

    @GetMapping
    public List<ResultatDto> lista(@RequestParam String kurskod,
                                   @RequestParam String modul) {
        return service.hamta(kurskod, modul);
    }
}

package se.ltu.grupp10.canvas.web;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.canvas.service.RegistreringService;
import se.ltu.grupp10.canvas.web.dto.RadStatusDto;
import se.ltu.grupp10.canvas.web.dto.RegistreringDto;

/**
 * POST /canvas/kurser/{kurskod}/registrering runs reg_Student_Resultat.
 * Answers 200 with one status per row ("registrerad", "hinder" or "fel").
 */
@RestController
public class RegistreringController {

    private final RegistreringService service;

    public RegistreringController(RegistreringService service) {
        this.service = service;
    }

    @PostMapping("/kurser/{kurskod}/registrering")
    public List<RadStatusDto> register(@PathVariable("kurskod") String kurskod,
                                       @RequestBody RegistreringDto request) {
        return service.register(kurskod, request);
    }
}
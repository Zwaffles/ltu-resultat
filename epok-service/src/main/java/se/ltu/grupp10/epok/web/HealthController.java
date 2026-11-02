package se.ltu.grupp10.epok.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /epok/health returns {"system":"Epok","status":"UP"}.
 * Used to check that the service is running (by the GUI's status view,
 * Canvas's system status and the .http test files).
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("system", "Epok", "status", "UP");
    }
}

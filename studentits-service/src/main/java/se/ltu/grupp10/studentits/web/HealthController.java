package se.ltu.grupp10.studentits.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /studentits/health returns {"system":"StudentITS","status":"UP"}.
 * Used to check that the service is running (by the GUI's status view,
 * Canvas's system status and the .http test files).
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("system", "StudentITS", "status", "UP");
    }
}

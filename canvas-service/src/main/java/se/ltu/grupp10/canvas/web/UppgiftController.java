package se.ltu.grupp10.canvas.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.canvas.repository.AssignmentRepository;
import se.ltu.grupp10.canvas.web.dto.UppgiftDto;

/**
 * GET /canvas/kurser/{kurskod}/uppgifter returns the course's assignments.
 * Canvas has no course table, so an unknown course gives an empty list (200 []), not 404.
 */
@RestController
public class UppgiftController {

    private final AssignmentRepository assignments;

    public UppgiftController(AssignmentRepository assignments) {
        this.assignments = assignments;
    }

    @GetMapping("/kurser/{kurskod}/uppgifter")
    public List<UppgiftDto> uppgifter(@PathVariable String kurskod) {
        String normaliserad = kurskod.trim().toUpperCase();   // "d0031n" -> "D0031N"
        return assignments.findByKurskodOrderByDeadlineAsc(normaliserad).stream()
                .map(UppgiftDto::fromAssignment)
                .toList();
    }
}
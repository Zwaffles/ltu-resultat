package se.ltu.grupp10.canvas.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.canvas.repository.AssignmentRepository;
import se.ltu.grupp10.canvas.web.dto.KursDto;

/**
 * GET /canvas/kurser returns every course that has assignments in Canvas.
 * Canvas currently has no course table, so the courses are derived from the assignments.
 */
@RestController
public class KursController {

    private final AssignmentRepository assignments;

    public KursController(AssignmentRepository assignments) {
        this.assignments = assignments;
    }

    @GetMapping("/kurser")
    public List<KursDto> kurser() {
        return assignments.findDistinctKurskoder().stream()
                .map(KursDto::new)
                .toList();
    }
}
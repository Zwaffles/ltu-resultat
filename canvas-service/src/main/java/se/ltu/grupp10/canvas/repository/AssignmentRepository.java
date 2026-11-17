package se.ltu.grupp10.canvas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.canvas.domain.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /** All assignments for a course, earliest deadline first. */
    List<Assignment> findByKurskodOrderByDeadlineAsc(String kurskod);
}
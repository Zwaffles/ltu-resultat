package se.ltu.grupp10.canvas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.ltu.grupp10.canvas.domain.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /** All assignments for a course, earliest deadline first. */
    List<Assignment> findByKurskodOrderByDeadlineAsc(String kurskod);

    /** Every course code that has at least one assignment, sorted alphabetically. */
    @Query("SELECT DISTINCT a.kurskod FROM Assignment a ORDER BY a.kurskod")
    List<String> findDistinctKurskoder();
}
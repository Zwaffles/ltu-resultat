package se.ltu.grupp10.canvas.repository;

import se.ltu.grupp10.canvas.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
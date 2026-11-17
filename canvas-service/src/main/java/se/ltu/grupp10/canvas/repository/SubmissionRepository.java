package se.ltu.grupp10.canvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.canvas.domain.Submission;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(Long assignmentId);
    List<Submission> findByStudentId(Long studentId);
    Optional<Submission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
}
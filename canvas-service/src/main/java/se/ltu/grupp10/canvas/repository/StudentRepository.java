package se.ltu.grupp10.canvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.canvas.domain.Student;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
}
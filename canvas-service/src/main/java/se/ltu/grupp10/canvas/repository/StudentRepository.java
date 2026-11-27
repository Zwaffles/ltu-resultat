package se.ltu.grupp10.canvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.canvas.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
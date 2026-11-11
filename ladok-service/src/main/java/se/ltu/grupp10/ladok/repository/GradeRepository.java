package se.ltu.grupp10.ladok.repository;

import se.ltu.grupp10.ladok.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
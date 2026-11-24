package se.ltu.grupp10.ladok.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.ladok.domain.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    boolean existsByPersonnummerAndKurskodAndModulkod(
            String personnummer, String kurskod, String modulkod);
    List<Grade> findByKurskodAndModulkod(String kurskod, String modulkod);
}

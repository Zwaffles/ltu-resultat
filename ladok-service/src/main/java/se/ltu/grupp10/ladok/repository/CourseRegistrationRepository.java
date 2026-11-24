package se.ltu.grupp10.ladok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.ladok.domain.CourseRegistration;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    boolean existsByPersonnummerAndKurskod(String personnummer, String kurskod);
}

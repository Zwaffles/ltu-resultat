package se.ltu.grupp10.ladok.repository;

import org.springframework.stereotype.Component;

@Component
public class JpaKursregistreringKalla implements KursregistreringKalla {

    private final CourseRegistrationRepository repo;

    public JpaKursregistreringKalla(CourseRegistrationRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean arRegistrerad(String personnummer, String kurskod) {
        return repo.existsByPersonnummerAndKurskod(personnummer, kurskod);
    }
}

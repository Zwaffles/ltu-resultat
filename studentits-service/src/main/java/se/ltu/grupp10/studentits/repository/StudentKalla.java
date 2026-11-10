package se.ltu.grupp10.studentits.repository;

import java.util.Optional;

public interface StudentKalla {
    Optional<String> hittaPersonnummer(String anvandarnamn);
}

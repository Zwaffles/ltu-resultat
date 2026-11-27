package se.ltu.grupp10.studentits.repository;

import se.ltu.grupp10.studentits.domain.StudentAccount;
import java.util.Optional;

public interface StudentKalla {
    Optional<StudentAccount> hittaKonto(String anvandarnamn);
}


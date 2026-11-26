package se.ltu.grupp10.studentits.repository;

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.ltu.grupp10.studentits.domain.StudentAccount;

@Component
public class JpaStudentKalla implements StudentKalla {

    private final StudentAccountRepository repo;

    public JpaStudentKalla(StudentAccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<StudentAccount> hittaKonto(String anvandarnamn) {
        return repo.findByUsername(anvandarnamn);
    }

    }

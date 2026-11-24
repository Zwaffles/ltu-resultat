package se.ltu.grupp10.studentits.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.studentits.domain.StudentAccount;

public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
    Optional<StudentAccount> findByUsername(String username);
}

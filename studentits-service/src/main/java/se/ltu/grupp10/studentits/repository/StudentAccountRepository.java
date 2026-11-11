package se.ltu.grupp10.studentits.repository;

import se.ltu.grupp10.studentits.domain.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
}
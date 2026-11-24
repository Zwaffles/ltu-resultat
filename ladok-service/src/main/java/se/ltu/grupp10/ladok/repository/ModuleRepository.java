package se.ltu.grupp10.ladok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.ladok.domain.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    boolean existsByKurskodAndModulkodAndActiveTrue(String kurskod, String modulkod);
}

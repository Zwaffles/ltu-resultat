package se.ltu.grupp10.epok.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.ltu.grupp10.epok.domain.Modul;

public interface ModulRepository extends JpaRepository<Modul, Long> {
    boolean existsByKurskod(String kurskod);
    List<Modul> findByKurskodOrderByModulkod(String kurskod);
}

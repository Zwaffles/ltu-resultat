package se.ltu.grupp10.epok.repository;

import se.ltu.grupp10.epok.domain.Modul;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Modul, Long> {
    // Spring Data JPA automatically handles basic CRUD operations here!
    // You can also add custom search methods later if needed, like:
    // List<Module> findByKurskod(String kurskod);
}
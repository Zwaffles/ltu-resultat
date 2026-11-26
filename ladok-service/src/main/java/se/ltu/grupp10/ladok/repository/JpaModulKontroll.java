package se.ltu.grupp10.ladok.repository;

import org.springframework.stereotype.Component;

@Component
public class JpaModulKontroll implements ModulKontroll {

    private final ModuleRepository repo;

    public JpaModulKontroll(ModuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean finnsOchArAktiv(String kurskod, String modul) {
        return repo.existsByKurskodAndModulkodAndActiveTrue(kurskod, modul);
    }
}

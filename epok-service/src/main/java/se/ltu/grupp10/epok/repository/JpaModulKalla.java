package se.ltu.grupp10.epok.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;
import se.ltu.grupp10.epok.web.ModulDto;

@Component
public class JpaModulKalla implements ModulKalla {

    private final ModulRepository repo;

    public JpaModulKalla(ModulRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean kursFinns(String kurskod) {
        return repo.existsByKurskod(kurskod);
    }

    // TODO: Filtrera på aktiv när kolumnen är tillagd.
    @Override
    public List<ModulDto> aktivaModuler(String kurskod) {
        return repo.findByKurskodOrderByModulkod(kurskod).stream()
                .map(m -> new ModulDto(m.getModulkod(), m.getBenamning(),
                        BigDecimal.valueOf(m.getHp())))
                .toList();
    }
}

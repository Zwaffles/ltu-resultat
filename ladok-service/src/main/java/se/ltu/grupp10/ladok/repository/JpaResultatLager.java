package se.ltu.grupp10.ladok.repository;

import java.util.List;
import org.springframework.stereotype.Component;
import se.ltu.grupp10.ladok.domain.Grade;
import se.ltu.grupp10.ladok.web.ResultatDto;

@Component
public class JpaResultatLager implements ResultatLager {

    private final GradeRepository repo;

    public JpaResultatLager(GradeRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean finns(String personnummer, String kurskod, String modul) {
        return repo.existsByPersonnummerAndKurskodAndModulkod(personnummer, kurskod, modul);
    }

    @Override
    public void spara(ResultatDto dto) {
        Grade g = new Grade();
        g.setPersonnummer(dto.personnummer());
        g.setKurskod(dto.kurskod());
        g.setModulkod(dto.modul());
        g.setGrade(dto.betyg());
        g.setExaminationDate(dto.datum());
        repo.save(g);
    }

    @Override
    public List<ResultatDto> hamta(String kurskod, String modul) {
        return repo.findByKurskodAndModulkod(kurskod, modul).stream()
                .map(g -> new ResultatDto(g.getPersonnummer(), g.getKurskod(),
                        g.getModulkod(), g.getExaminationDate(), g.getGrade()))
                .toList();
    }
}

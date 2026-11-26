package se.ltu.grupp10.epok.repository;

import java.util.List;
import se.ltu.grupp10.epok.web.ModulDto;

public interface ModulKalla {
    boolean kursFinns(String kurskod);
    List<ModulDto> aktivaModuler(String kurskod);
}

/*
ModulKalla är ett gränssnitt med två frågor: finns en viss kurs, och vilka moduler är aktiva för den?
Det är två separata metoder eftersom Epok ska skilja på en kurs som inte finns alls (404) och en kurs som finns men saknar aktiva moduler (200 med tom lista).
Tjänsten känner bara till gränssnittet och inte hur svaren tas fram, så implementationen kan bytas ut mot en databasbaserad klass när roll A har lagt in databasen, utan att reglerna behöver ändras.
 */

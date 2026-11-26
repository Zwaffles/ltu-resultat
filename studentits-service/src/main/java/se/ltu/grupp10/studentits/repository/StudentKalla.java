package se.ltu.grupp10.studentits.repository;

import se.ltu.grupp10.studentits.domain.StudentAccount;
import java.util.Optional;

public interface StudentKalla {
    Optional<StudentAccount> hittaKonto(String anvandarnamn);
}

/*
StudentKalla är ett gränssnitt med en enda fråga: vilket personnummer hör till ett givet användarnamn?
Metoden returnerar en Optional, så att fallet "användaren finns inte" är en del av gränssnittet och den som anropar tvingas hantera det i stället för att riskera ett null-värde.
Tjänsten känner bara till gränssnittet och inte hur svaret tas fram, så implementationen kan bytas ut mot en databasbaserad klass när roll A har lagt in databasen, utan att reglerna behöver ändras.
 */

package se.ltu.grupp10.studentits.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.studentits.repository.StudentKalla;
import se.ltu.grupp10.studentits.web.PersonnummerDto;

/*
StudentService innehåller StudentITS affärslogik för get_Persnummer.
Metoden hamtaPersonnummer slår upp användarnamnet via StudentKalla och bygger ett PersonnummerDto om användaren finns.
Saknas användaren kastas ett ResponseStatusException med status 404.
Klassen får sitt beroende av StudentKalla via konstruktorn, vilket gör den lätt att testa med Mockito.
 */

@Service
public class StudentService {

    private final StudentKalla kalla;

    public StudentService(StudentKalla kalla) {
        this.kalla = kalla;
    }

    public PersonnummerDto hamtaPersonnummer(String anvandarnamn) {
        return kalla.hittaKonto(anvandarnamn)
                .map(a -> new PersonnummerDto(a.getUsername(), a.getPersonnummer(), a.isActive()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Användaren finns inte: " + anvandarnamn));
    }
    }

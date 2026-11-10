package se.ltu.grupp10.studentits.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.studentits.repository.StudentKalla;
import se.ltu.grupp10.studentits.web.PersonnummerDto;

@Service
public class StudentService {

    private final StudentKalla kalla;

    public StudentService(StudentKalla kalla) {
        this.kalla = kalla;
    }

    public PersonnummerDto hamtaPersonnummer(String anvandarnamn) {
        return kalla.hittaPersonnummer(anvandarnamn)
                .map(p -> new PersonnummerDto(anvandarnamn, p))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Användaren finns inte: " + anvandarnamn));
    }
}

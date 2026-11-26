package se.ltu.grupp10.canvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.canvas.client.LadokClient;
import se.ltu.grupp10.canvas.client.LadokResultat;
import se.ltu.grupp10.canvas.client.LadokSvar;
import se.ltu.grupp10.canvas.client.StudentItsClient;
import se.ltu.grupp10.canvas.client.StudentItsSvar;
import se.ltu.grupp10.canvas.web.dto.RadStatusDto;
import se.ltu.grupp10.canvas.web.dto.RegistreringDto;
import se.ltu.grupp10.canvas.web.dto.RegistreringRadDto;

@Service
public class RegistreringService {

    private final StudentItsClient studentIts;
    private final LadokClient ladok;

    public RegistreringService(StudentItsClient studentIts, LadokClient ladok) {
        this.studentIts = studentIts;
        this.ladok = ladok;
    }

    public List<RadStatusDto> register(String kurskod, RegistreringDto request) {
        if (request == null || isBlank(request.modul())
                || request.rader() == null || request.rader().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ange modul och minst en rad");
        }

        String normalizedKurskod = kurskod.trim().toUpperCase();
        return request.rader().stream()
                .map(rad -> registerRow(normalizedKurskod, request.modul(), rad))
                .toList();
    }

    private RadStatusDto registerRow(String kurskod, String modul, RegistreringRadDto rad) {
        String anvandarnamn = rad.anvandarnamn();

        // 1. StudentITS: username -> personnummer
        Optional<StudentItsSvar> student;
        try {
            student = studentIts.findStudent(anvandarnamn);
        } catch (RestClientException e) {
            return RadStatusDto.error(anvandarnamn, "StudentITS svarar inte");
        }
        if (student.isEmpty()) {
            return RadStatusDto.hinder(anvandarnamn, "Studenten finns inte i StudentITS");
        }
        if (Boolean.FALSE.equals(student.get().aktiv())) {
            return RadStatusDto.hinder(anvandarnamn, "Studentens konto är inaktivt");
        }

        // 2. Ladok: register the result
        LadokSvar svar = ladok.registerResult(new LadokResultat(
                student.get().personnummer(), kurskod, modul, rad.datum(), rad.betyg()));
        return new RadStatusDto(anvandarnamn, svar.status(), svar.orsak());
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
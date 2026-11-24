package se.ltu.grupp10.ladok.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.ladok.repository.KursregistreringKalla;
import se.ltu.grupp10.ladok.repository.ModulKontroll;
import se.ltu.grupp10.ladok.repository.ResultatLager;
import se.ltu.grupp10.ladok.web.ResultatDto;

/*
ResultatService innehåller Ladoks affärslogik, alltså valideringsreglerna för reg_Resultat.
Metoden registrera kontrollerar först att alla fält finns (annars 400) och prövar sedan i tur och ordning om studenten är registrerad på kursen, om modulen finns och är aktiv, om betyget finns i skalan, om datumet inte ligger i framtiden och om resultatet inte redan är registrerat.
Den första regel som bryts avbryter med ett HinderException, och om alla regler godkänns sparas resultatet via ResultatLager.
Klassen får sina tre beroenden, de ovan nämnda gränssnitten, via konstruktorn, vilket gör den lätt att testa med Mockito.
Metoden hamta lämnar bara vidare till ResultatLager.
 */

@Service
public class ResultatService {

    private static final Set<String> BETYG = Set.of("U", "G", "VG");

    private final KursregistreringKalla registreringar;
    private final ModulKontroll moduler;
    private final ResultatLager lager;

    public ResultatService(KursregistreringKalla registreringar,
                           ModulKontroll moduler, ResultatLager lager) {
        this.registreringar = registreringar;
        this.moduler = moduler;
        this.lager = lager;
    }

    public void registrera(ResultatDto r) {
        // 400: begäran är trasig (fält saknas). Det är inte ett "hinder".
        if (tom(r.personnummer()) || tom(r.kurskod()) || tom(r.modul())
                || tom(r.betyg()) || r.datum() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Alla fält måste anges: personnummer, kurskod, modul, datum, betyg");
        }

        // 422: affärsregler. Dessa blir status "hinder".
        if (!registreringar.arRegistrerad(r.personnummer(), r.kurskod())) {
            throw new HinderException("Studenten är inte registrerad på kursen");
        }
        if (!moduler.finnsOchArAktiv(r.kurskod(), r.modul())) {
            throw new HinderException("Modulen finns inte eller är inaktiv");
        }
        if (!BETYG.contains(r.betyg())) {
            throw new HinderException("Ogiltigt betyg: " + r.betyg());
        }
        if (r.datum().isAfter(LocalDate.now())) {
            throw new HinderException("Datumet ligger i framtiden");
        }
        if (lager.finns(r.personnummer(), r.kurskod(), r.modul())) {
            throw new HinderException("Resultat redan registrerat");
        }

        lager.spara(r);
    }

    public List<ResultatDto> hamta(String kurskod, String modul) {
        return lager.hamta(kurskod, modul);
    }

    private static boolean tom(String s) {
        return s == null || s.isBlank();
    }
}

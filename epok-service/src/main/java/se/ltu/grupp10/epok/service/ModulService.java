package se.ltu.grupp10.epok.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.epok.repository.ModulKalla;
import se.ltu.grupp10.epok.web.ModulDto;

/*
ModulService innehåller Epoks affärslogik för get_Modul.
Metoden aktivaModuler kontrollerar först att kursen finns och kastar annars ett ResponseStatusException med status 404.
Finns kursen returneras dess aktiva moduler via ModulKalla, och en känd kurs utan aktiva moduler ger alltså en tom lista i stället för ett fel.
Klassen får sitt beroende av ModulKalla via konstruktorn, vilket gör den lätt att testa med Mockito.
 */

@Service
public class ModulService {

    private final ModulKalla kalla;

    public ModulService(ModulKalla kalla) {
        this.kalla = kalla;
    }

    public List<ModulDto> aktivaModuler(String kurskod) {
        if (!kalla.kursFinns(kurskod)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Kursen finns inte: " + kurskod);
        }
        return kalla.aktivaModuler(kurskod);
    }
}

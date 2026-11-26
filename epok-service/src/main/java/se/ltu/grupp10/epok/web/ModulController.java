package se.ltu.grupp10.epok.web;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.epok.service.ModulService;

/*
ModulController är REST-lagret för Epok och tar emot HTTP-anropen.
Eftersom tjänstens context-path redan är /epok mappas klassen till /kurser, och en GET på /kurser/{kurskod}/moduler returnerar kursens aktiva moduler som en lista av ModulDto.
Kurskoden hämtas ur adressen med @PathVariable.
Controllern innehåller ingen affärslogik, den översätter bara mellan HTTP och tjänstelagret.
 */

@RestController
@RequestMapping("/kurser")
public class ModulController {

    private final ModulService service;

    public ModulController(ModulService service) {
        this.service = service;
    }

    @GetMapping("/{kurskod}/moduler")
    public List<ModulDto> moduler(@PathVariable String kurskod) {
        return service.aktivaModuler(kurskod);
    }
}

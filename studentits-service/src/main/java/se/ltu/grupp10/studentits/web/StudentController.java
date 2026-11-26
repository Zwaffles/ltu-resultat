package se.ltu.grupp10.studentits.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.studentits.service.StudentService;

/*
StudentController är REST-lagret för StudentITS och tar emot HTTP-anropen.
Eftersom tjänstens context-path redan är /studentits mappas klassen till /studenter, och en GET på /studenter/{anvandarnamn}/personnummer returnerar studentens personnummer som en PersonnummerDto.
Användarnamnet hämtas ur adressen med @PathVariable.
Controllern innehåller ingen affärslogik, den översätter bara mellan HTTP och tjänstelagret.
 */

@RestController
@RequestMapping("/studenter")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/{anvandarnamn}/personnummer")
    public PersonnummerDto personnummer(@PathVariable String anvandarnamn) {
        return service.hamtaPersonnummer(anvandarnamn);
    }
}

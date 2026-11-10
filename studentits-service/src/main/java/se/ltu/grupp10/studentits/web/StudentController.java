package se.ltu.grupp10.studentits.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ltu.grupp10.studentits.service.StudentService;

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

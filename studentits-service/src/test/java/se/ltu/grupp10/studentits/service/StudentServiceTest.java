package se.ltu.grupp10.studentits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.studentits.repository.StudentKalla;
import se.ltu.grupp10.studentits.web.PersonnummerDto;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentKalla kalla;
    @InjectMocks StudentService service;

    @Test
    void kandAnvandareGerPersonnummer() {
        when(kalla.hittaPersonnummer("annand-1")).thenReturn(Optional.of("19990512-1234"));

        assertEquals(new PersonnummerDto("annand-1", "19990512-1234"),
                service.hamtaPersonnummer("annand-1"));
    }

    @Test
    void okandAnvandareGer404() {
        when(kalla.hittaPersonnummer("finns-inte")).thenReturn(Optional.empty());

        var e = assertThrows(ResponseStatusException.class,
                () -> service.hamtaPersonnummer("finns-inte"));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }
}

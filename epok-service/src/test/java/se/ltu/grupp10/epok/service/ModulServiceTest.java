package se.ltu.grupp10.epok.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.epok.repository.ModulKalla;
import se.ltu.grupp10.epok.web.ModulDto;

@ExtendWith(MockitoExtension.class)
class ModulServiceTest {

    @Mock ModulKalla kalla;
    @InjectMocks ModulService service;

    @Test
    void kandKursReturnerarAktivaModuler() {
        var modul = new ModulDto("0005", "Inlämningsuppgift", new BigDecimal("3.0"));
        when(kalla.kursFinns("D0031N")).thenReturn(true);
        when(kalla.aktivaModuler("D0031N")).thenReturn(List.of(modul));

        assertEquals(List.of(modul), service.aktivaModuler("D0031N"));
    }

    @Test
    void kandKursUtanAktivaModulerGerTomLista() {
        when(kalla.kursFinns("D0012E")).thenReturn(true);
        when(kalla.aktivaModuler("D0012E")).thenReturn(List.of());

        assertEquals(List.of(), service.aktivaModuler("D0012E"));
    }

    @Test
    void okandKursGer404OchHamtarInteModuler() {
        when(kalla.kursFinns("XXXX")).thenReturn(false);

        var e = assertThrows(ResponseStatusException.class,
                () -> service.aktivaModuler("XXXX"));

        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        verify(kalla, never()).aktivaModuler("XXXX");
    }
}

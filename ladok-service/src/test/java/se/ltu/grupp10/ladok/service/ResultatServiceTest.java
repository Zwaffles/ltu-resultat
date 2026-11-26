package se.ltu.grupp10.ladok.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.ladok.repository.KursregistreringKalla;
import se.ltu.grupp10.ladok.repository.ModulKontroll;
import se.ltu.grupp10.ladok.repository.ResultatLager;
import se.ltu.grupp10.ladok.web.ResultatDto;

@ExtendWith(MockitoExtension.class)
class ResultatServiceTest {

    private static final String PNR = "19990512-1234";

    @Mock KursregistreringKalla registreringar;
    @Mock ModulKontroll moduler;
    @Mock ResultatLager lager;
    @InjectMocks ResultatService service;

    @BeforeEach
    void grundlage() {
        // Normalfallet: allt är giltigt. Varje test ändrar det som ska bli fel.
        lenient().when(registreringar.arRegistrerad(any(), any())).thenReturn(true);
        lenient().when(moduler.finnsOchArAktiv(any(), any())).thenReturn(true);
        lenient().when(lager.finns(any(), any(), any())).thenReturn(false);
    }

    private ResultatDto resultat(String betyg, LocalDate datum) {
        return new ResultatDto(PNR, "D0031N", "0005", datum, betyg);
    }

    private LocalDate igar() {
        return LocalDate.now().minusDays(1);
    }

    @Test
    void giltigtResultatSparas() {
        var r = resultat("VG", igar());

        service.registrera(r);

        verify(lager).spara(r);
    }

    @ParameterizedTest
    @ValueSource(strings = {"U", "G", "VG"})
    void allaBetygIFoljandeSkalaGodkanns(String betyg) {
        var r = resultat(betyg, igar());

        service.registrera(r);

        verify(lager).spara(r);
    }

    @Test
    void dagensDatumArGiltigt() {
        var r = resultat("G", LocalDate.now());

        service.registrera(r);

        verify(lager).spara(r);
    }

    @Test
    void studentSomInteArRegistreradGerHinder() {
        when(registreringar.arRegistrerad(PNR, "D0031N")).thenReturn(false);

        var e = assertThrows(HinderException.class,
                () -> service.registrera(resultat("VG", igar())));

        assertEquals("Studenten är inte registrerad på kursen", e.getMessage());
        verify(lager, never()).spara(any());
    }

    @Test
    void inaktivModulGerHinder() {
        when(moduler.finnsOchArAktiv("D0031N", "0005")).thenReturn(false);

        var e = assertThrows(HinderException.class,
                () -> service.registrera(resultat("VG", igar())));

        assertEquals("Modulen finns inte eller är inaktiv", e.getMessage());
        verify(lager, never()).spara(any());
    }

    @Test
    void ogiltigtBetygGerHinder() {
        var e = assertThrows(HinderException.class,
                () -> service.registrera(resultat("Q", igar())));

        assertEquals("Ogiltigt betyg: Q", e.getMessage());
        verify(lager, never()).spara(any());
    }

    @Test
    void framtidaDatumGerHinder() {
        var e = assertThrows(HinderException.class,
                () -> service.registrera(resultat("VG", LocalDate.now().plusDays(1))));

        assertEquals("Datumet ligger i framtiden", e.getMessage());
        verify(lager, never()).spara(any());
    }

    @Test
    void redanRegistreratResultatGerHinder() {
        when(lager.finns(PNR, "D0031N", "0005")).thenReturn(true);

        var e = assertThrows(HinderException.class,
                () -> service.registrera(resultat("VG", igar())));

        assertEquals("Resultat redan registrerat", e.getMessage());
        verify(lager, never()).spara(any());
    }

    @Test
    void saknatFaltGer400() {
        var r = new ResultatDto(PNR, "D0031N", null, null, "VG");

        var e = assertThrows(ResponseStatusException.class, () -> service.registrera(r));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        verify(lager, never()).spara(any());
    }

    @Test
    void tomtBetygGer400() {
        var e = assertThrows(ResponseStatusException.class,
                () -> service.registrera(resultat("", igar())));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

    @Test
    void hamtaLamnarVidareTillLagret() {
        var lista = List.of(new ResultatDto(PNR, "D0031N", "0001", LocalDate.of(2026, 10, 25), "VG"));
        when(lager.hamta("D0031N", "0001")).thenReturn(lista);

        assertEquals(lista, service.hamta("D0031N", "0001"));
    }
}

package se.ltu.grupp10.ladok.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.ltu.grupp10.ladok.repository.KursregistreringKalla;
import se.ltu.grupp10.ladok.repository.ModulKontroll;
import se.ltu.grupp10.ladok.repository.ResultatLager;
import se.ltu.grupp10.ladok.service.ResultatService;

@ExtendWith(MockitoExtension.class)
class ResultatControllerTest {

    private static final String PNR = "19990512-1234";

    @Mock KursregistreringKalla registreringar;
    @Mock ModulKontroll moduler;
    @Mock ResultatLager lager;
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        // Normalfallet: allt är giltigt. Varje test ändrar det som ska bli fel.
        lenient().when(registreringar.arRegistrerad(any(), any())).thenReturn(true);
        lenient().when(moduler.finnsOchArAktiv(any(), any())).thenReturn(true);
        lenient().when(lager.finns(any(), any(), any())).thenReturn(false);

        mvc = MockMvcBuilders
                .standaloneSetup(new ResultatController(
                        new ResultatService(registreringar, moduler, lager)))
                .setControllerAdvice(new FelHanterare())
                .build();
    }

    private ResultActions skicka(String modul, LocalDate datum, String betyg) throws Exception {
        String json = """
                {"personnummer":"%s","kurskod":"D0031N","modul":"%s","datum":"%s","betyg":"%s"}
                """.formatted(PNR, modul, datum, betyg);
        return mvc.perform(post("/resultat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private LocalDate igar() {
        return LocalDate.now().minusDays(1);
    }

    @Test
    void giltigtResultatGer201OchRegistrerad() throws Exception {
        skicka("0005", igar(), "VG")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("registrerad"))
                .andExpect(jsonPath("$.orsak").doesNotExist());

        verify(lager).spara(any());
    }

    @Test
    void studentSomInteArRegistreradGer422Hinder() throws Exception {
        when(registreringar.arRegistrerad(PNR, "D0031N")).thenReturn(false);

        skicka("0005", igar(), "VG")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value("hinder"))
                .andExpect(jsonPath("$.orsak").value(containsString("registrerad")));

        verify(lager, never()).spara(any());
    }

    @Test
    void inaktivModulGer422Hinder() throws Exception {
        when(moduler.finnsOchArAktiv("D0031N", "0003")).thenReturn(false);

        skicka("0003", igar(), "VG")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value("hinder"))
                .andExpect(jsonPath("$.orsak").value(containsString("inaktiv")));

        verify(lager, never()).spara(any());
    }

    @Test
    void ogiltigtBetygGer422Hinder() throws Exception {
        skicka("0005", igar(), "4")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value("hinder"))
                .andExpect(jsonPath("$.orsak").value(containsString("Ogiltigt betyg")));

        verify(lager, never()).spara(any());
    }

    @Test
    void framtidaDatumGer422Hinder() throws Exception {
        skicka("0005", LocalDate.now().plusDays(1), "VG")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value("hinder"))
                .andExpect(jsonPath("$.orsak").value(containsString("framtiden")));

        verify(lager, never()).spara(any());
    }

    @Test
    void redanRegistreratResultatGer422Hinder() throws Exception {
        when(lager.finns(PNR, "D0031N", "0005")).thenReturn(true);

        skicka("0005", igar(), "VG")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value("hinder"))
                .andExpect(jsonPath("$.orsak").value(containsString("redan registrerat")));

        verify(lager, never()).spara(any());
    }

    @Test
    void saknatFaltGer400() throws Exception {
        mvc.perform(post("/resultat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personnummer\":\"" + PNR + "\",\"kurskod\":\"D0031N\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fel"));

        verify(lager, never()).spara(any());
    }

    @Test
    void felaktigtDatumformatGer400() throws Exception {
        String json = """
                {"personnummer":"%s","kurskod":"D0031N","modul":"0005","datum":"10/11/2026","betyg":"VG"}
                """.formatted(PNR);

        mvc.perform(post("/resultat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fel"));

        verify(lager, never()).spara(any());
    }

    @Test
    void listaResultatGer200OchLista() throws Exception {
        when(lager.hamta("D0031N", "0001")).thenReturn(List.of(
                new ResultatDto(PNR, "D0031N", "0001", LocalDate.of(2026, 10, 25), "VG")));

        mvc.perform(get("/resultat").param("kurskod", "D0031N").param("modul", "0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].personnummer").value(PNR))
                .andExpect(jsonPath("$[0].betyg").value("VG"));
    }
}

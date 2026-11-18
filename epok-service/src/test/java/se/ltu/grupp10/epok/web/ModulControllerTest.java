package se.ltu.grupp10.epok.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.ltu.grupp10.epok.repository.ModulKalla;
import se.ltu.grupp10.epok.service.ModulService;

@ExtendWith(MockitoExtension.class)
class ModulControllerTest {

    @Mock ModulKalla kalla;
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new ModulController(new ModulService(kalla)))
                .setControllerAdvice(new FelHanterare())
                .build();
    }

    @Test
    void kandKursGer200OchModulerMedRattFalt() throws Exception {
        when(kalla.kursFinns("D0031N")).thenReturn(true);
        when(kalla.aktivaModuler("D0031N")).thenReturn(List.of(
                new ModulDto("0001", "Tentamen", new BigDecimal("4.5")),
                new ModulDto("0005", "Inlamningsuppgift", new BigDecimal("3.0"))));

        mvc.perform(get("/kurser/D0031N/moduler"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].modulkod").value("0001"))
                .andExpect(jsonPath("$[0].benamning").value("Tentamen"))
                .andExpect(jsonPath("$[0].hp").value(4.5))
                .andExpect(jsonPath("$[1].modulkod").value("0005"));
    }

    @Test
    void kandKursUtanAktivaModulerGer200OchTomLista() throws Exception {
        when(kalla.kursFinns("D0012E")).thenReturn(true);
        when(kalla.aktivaModuler("D0012E")).thenReturn(List.of());

        mvc.perform(get("/kurser/D0012E/moduler"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void okandKursGer404OchFelformat() throws Exception {
        when(kalla.kursFinns("XXXX")).thenReturn(false);

        mvc.perform(get("/kurser/XXXX/moduler"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fel"))
                .andExpect(jsonPath("$.orsak").value("Kursen finns inte: XXXX"));
    }
}

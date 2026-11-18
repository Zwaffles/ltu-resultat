package se.ltu.grupp10.studentits.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.ltu.grupp10.studentits.repository.StudentKalla;
import se.ltu.grupp10.studentits.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock StudentKalla kalla;
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new StudentController(new StudentService(kalla)))
                .setControllerAdvice(new FelHanterare())
                .build();
    }

    @Test
    void kandAnvandareGer200OchPersonnummer() throws Exception {
        when(kalla.hittaPersonnummer("annand-1")).thenReturn(Optional.of("19990512-1234"));

        mvc.perform(get("/studenter/annand-1/personnummer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anvandarnamn").value("annand-1"))
                .andExpect(jsonPath("$.personnummer").value("19990512-1234"));
    }

    @Test
    void okandAnvandareGer404OchFelformat() throws Exception {
        when(kalla.hittaPersonnummer("finns-inte")).thenReturn(Optional.empty());

        mvc.perform(get("/studenter/finns-inte/personnummer"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fel"))
                .andExpect(jsonPath("$.orsak").exists());
    }
}

package se.ltu.grupp10.canvas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.canvas.client.LadokClient;
import se.ltu.grupp10.canvas.client.LadokResultat;
import se.ltu.grupp10.canvas.client.LadokSvar;
import se.ltu.grupp10.canvas.client.StudentItsClient;
import se.ltu.grupp10.canvas.client.StudentItsSvar;
import se.ltu.grupp10.canvas.web.dto.RadStatusDto;
import se.ltu.grupp10.canvas.web.dto.RegistreringDto;
import se.ltu.grupp10.canvas.web.dto.RegistreringRadDto;

class RegistreringServiceTest {

    private static final LocalDate DATUM = LocalDate.of(2026, 11, 20);

    private final StudentItsClient studentIts = mock(StudentItsClient.class);
    private final LadokClient ladok = mock(LadokClient.class);
    private final RegistreringService service = new RegistreringService(studentIts, ladok);

    @Test
    void registersWhenStudentFoundAndLadokAccepts() {
        when(studentIts.findStudent("oscnil-6"))
                .thenReturn(Optional.of(new StudentItsSvar("oscnil-6", "20030414-2345", true)));
        when(ladok.registerResult(any())).thenReturn(new LadokSvar("registrerad", null));

        List<RadStatusDto> svar = service.register("d0031n", request(row("oscnil-6")));

        assertEquals(List.of(RadStatusDto.registered("oscnil-6")), svar);
        verify(ladok).registerResult(
                new LadokResultat("20030414-2345", "D0031N", "0005", DATUM, "G"));
    }

    @Test
    void unknownStudentGivesHinderAndLadokIsNotCalled() {
        when(studentIts.findStudent("okand-10")).thenReturn(Optional.empty());

        List<RadStatusDto> svar = service.register("D0031N", request(row("okand-10")));

        assertEquals(List.of(RadStatusDto.hinder("okand-10", "Studenten finns inte i StudentITS")), svar);
        verify(ladok, never()).registerResult(any());
    }

    @Test
    void inactiveAccountGivesHinder() {
        when(studentIts.findStudent("vikpet-8"))
                .thenReturn(Optional.of(new StudentItsSvar("vikpet-8", "19970210-1122", false)));

        List<RadStatusDto> svar = service.register("D0031N", request(row("vikpet-8")));

        assertEquals(List.of(RadStatusDto.hinder("vikpet-8", "Studentens konto är inaktivt")), svar);
        verify(ladok, never()).registerResult(any());
    }

    @Test
    void ladokHinderIsPassedOn() {
        when(studentIts.findStudent("sarlin-5"))
                .thenReturn(Optional.of(new StudentItsSvar("sarlin-5", "20020105-7890", null)));
        when(ladok.registerResult(any()))
                .thenReturn(new LadokSvar("hinder", "Studenten är inte registrerad på kursen"));

        List<RadStatusDto> svar = service.register("D0031N", request(row("sarlin-5")));

        assertEquals(List.of(RadStatusDto.hinder("sarlin-5", "Studenten är inte registrerad på kursen")), svar);
    }

    @Test
    void unreachableStudentItsGivesErrorButOtherRowsContinue() {
        when(studentIts.findStudent("annand-1")).thenThrow(new ResourceAccessException("timeout"));
        when(studentIts.findStudent("majols-7"))
                .thenReturn(Optional.of(new StudentItsSvar("majols-7", "20040929-6789", true)));
        when(ladok.registerResult(any())).thenReturn(new LadokSvar("registrerad", null));

        List<RadStatusDto> svar = service.register("D0031N",
                request(row("annand-1"), row("majols-7")));

        assertEquals(List.of(
                RadStatusDto.error("annand-1", "StudentITS svarar inte"),
                RadStatusDto.registered("majols-7")), svar);
    }

    @Test
    void requestWithoutRowsGives400() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.register("D0031N", new RegistreringDto("0005", List.of())));

        assertEquals(400, ex.getStatusCode().value());
    }

    private static RegistreringDto request(RegistreringRadDto... rader) {
        return new RegistreringDto("0005", List.of(rader));
    }

    private static RegistreringRadDto row(String anvandarnamn) {
        return new RegistreringRadDto(anvandarnamn, DATUM, "G");
    }
}
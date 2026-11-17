package se.ltu.grupp10.canvas.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import se.ltu.grupp10.canvas.domain.Assignment;
import se.ltu.grupp10.canvas.repository.AssignmentRepository;
import se.ltu.grupp10.canvas.web.dto.UppgiftDto;

class UppgiftControllerTest {

    private final AssignmentRepository repository = mock(AssignmentRepository.class);
    private final UppgiftController controller = new UppgiftController(repository);

    @Test
    void returnerarKursensUppgifterMedIdOchNamn() {
        when(repository.findByKurskodOrderByDeadlineAsc("D0031N"))
                .thenReturn(List.of(uppgift(1L, "Inlämningsuppgift 1")));

        List<UppgiftDto> svar = controller.uppgifter("D0031N");

        assertEquals(List.of(new UppgiftDto(1L, "Inlämningsuppgift 1")), svar);
    }

    @Test
    void kurskodenNormaliseras() {
        controller.uppgifter(" d0031n ");

        verify(repository).findByKurskodOrderByDeadlineAsc("D0031N");
    }

    @Test
    void okandKursGerTomLista() {
        when(repository.findByKurskodOrderByDeadlineAsc("X9999X")).thenReturn(List.of());

        assertTrue(controller.uppgifter("X9999X").isEmpty());
    }

    private static Assignment uppgift(Long id, String titel) {
        Assignment a = new Assignment();
        a.setId(id);
        a.setTitle(titel);
        return a;
    }
}
package se.ltu.grupp10.canvas.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import se.ltu.grupp10.canvas.repository.AssignmentRepository;
import se.ltu.grupp10.canvas.web.dto.KursDto;

class KursControllerTest {

    private final AssignmentRepository repository = mock(AssignmentRepository.class);
    private final KursController controller = new KursController(repository);

    @Test
    void returnsEveryCourseCodeAsKursDto() {
        when(repository.findDistinctKurskoder()).thenReturn(List.of("D0024E", "D0031N"));

        List<KursDto> svar = controller.kurser();

        assertEquals(List.of(new KursDto("D0024E"), new KursDto("D0031N")), svar);
    }

    @Test
    void noAssignmentsGivesEmptyList() {
        when(repository.findDistinctKurskoder()).thenReturn(List.of());

        assertTrue(controller.kurser().isEmpty());
    }
}
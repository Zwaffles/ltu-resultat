package se.ltu.grupp10.canvas.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.canvas.domain.Assignment;
import se.ltu.grupp10.canvas.domain.Student;
import se.ltu.grupp10.canvas.domain.Submission;
import se.ltu.grupp10.canvas.repository.AssignmentRepository;
import se.ltu.grupp10.canvas.repository.StudentRepository;
import se.ltu.grupp10.canvas.repository.SubmissionRepository;
import se.ltu.grupp10.canvas.web.dto.StudentDto;

class StudentControllerTest {

    private final AssignmentRepository assignments = mock(AssignmentRepository.class);
    private final SubmissionRepository submissions = mock(SubmissionRepository.class);
    private final StudentRepository students = mock(StudentRepository.class);
    private final StudentController controller =
            new StudentController(assignments, submissions, students);

    @Test
    void returnsStudentsSortedByLastNameWithCanvasGrade() {
        when(assignments.findById(1L)).thenReturn(Optional.of(assignment(1L, "D0031N")));
        when(submissions.findByAssignmentId(1L)).thenReturn(List.of(
                submission(10L, "VG"),
                submission(11L, null)));
        when(students.findAllById(anyIterable())).thenReturn(List.of(
                student(10L, "olsmaj-7", "Maja", "Olsson"),
                student(11L, "annand-1", "Anna", "Andersson")));

        List<StudentDto> svar = controller.studenter("d0031n", 1L);

        assertEquals(List.of(
                new StudentDto("annand-1", "Andersson, Anna", null),
                new StudentDto("olsmaj-7", "Olsson, Maja", "VG")), svar);
    }

    @Test
    void unknownAssignmentGives404() {
        when(assignments.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> controller.studenter("D0031N", 99L));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void assignmentInAnotherCourseGives404() {
        when(assignments.findById(1L)).thenReturn(Optional.of(assignment(1L, "D0024E")));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> controller.studenter("D0031N", 1L));

        assertEquals(404, ex.getStatusCode().value());
    }

    private static Assignment assignment(Long id, String kurskod) {
        Assignment a = new Assignment();
        a.setId(id);
        a.setKurskod(kurskod);
        return a;
    }

    private static Submission submission(Long studentId, String grade) {
        Submission s = new Submission();
        s.setStudentId(studentId);
        s.setGrade(grade);
        return s;
    }

    private static Student student(Long id, String username, String firstName, String lastName) {
        Student s = new Student();
        s.setId(id);
        s.setUsername(username);
        s.setFirstName(firstName);
        s.setLastName(lastName);
        return s;
    }
}
package se.ltu.grupp10.canvas.web;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.canvas.domain.Assignment;
import se.ltu.grupp10.canvas.domain.Student;
import se.ltu.grupp10.canvas.domain.Submission;
import se.ltu.grupp10.canvas.repository.AssignmentRepository;
import se.ltu.grupp10.canvas.repository.StudentRepository;
import se.ltu.grupp10.canvas.repository.SubmissionRepository;
import se.ltu.grupp10.canvas.web.dto.StudentDto;

/**
 * GET /canvas/kurser/{kurskod}/uppgifter/{id}/studenter returns the students who have
 * a submission on the assignment, with their Canvas grade, sorted by last name.
 * Answers 404 if the assignment does not exist or belongs to another course.
 */
@RestController
public class StudentController {

    private final AssignmentRepository assignments;
    private final SubmissionRepository submissions;
    private final StudentRepository students;

    public StudentController(AssignmentRepository assignments,
                             SubmissionRepository submissions,
                             StudentRepository students) {
        this.assignments = assignments;
        this.submissions = submissions;
        this.students = students;
    }

    @GetMapping("/kurser/{kurskod}/uppgifter/{id}/studenter")
    public List<StudentDto> studenter(@PathVariable("kurskod") String kurskod,
                                      @PathVariable("id") Long id) {
        Assignment assignment = findAssignmentInCourse(kurskod.trim().toUpperCase(), id);

        List<Submission> assignmentSubmissions = submissions.findByAssignmentId(assignment.getId());

        // Fetch all students in one query and look them up by id.
        List<Long> studentIds = assignmentSubmissions.stream()
                .map(Submission::getStudentId)
                .toList();
        Map<Long, Student> studentsById = students.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Student::getId, Function.identity()));

        return assignmentSubmissions.stream()
                .filter(submission -> studentsById.containsKey(submission.getStudentId()))
                .sorted(Comparator
                        .comparing((Submission s) -> studentsById.get(s.getStudentId()).getLastName())
                        .thenComparing(s -> studentsById.get(s.getStudentId()).getFirstName()))
                .map(submission -> StudentDto.fromSubmission(
                        studentsById.get(submission.getStudentId()), submission))
                .toList();
    }

    private Assignment findAssignmentInCourse(String kurskod, Long id) {
        return assignments.findById(id)
                .filter(assignment -> assignment.getKurskod().equalsIgnoreCase(kurskod))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Uppgift " + id + " finns inte i kurs " + kurskod));
    }
}
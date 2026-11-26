package se.ltu.grupp10.canvas.web.dto;

import se.ltu.grupp10.canvas.domain.Student;
import se.ltu.grupp10.canvas.domain.Submission;

public record StudentDto(String anvandarnamn, String namn, String omdome) {

    public static StudentDto fromSubmission(Student student, Submission submission) {
        return new StudentDto(
                student.getUsername(),
                student.getLastName() + ", " + student.getFirstName(),
                submission.getGrade());   // null = not graded yet
    }
}
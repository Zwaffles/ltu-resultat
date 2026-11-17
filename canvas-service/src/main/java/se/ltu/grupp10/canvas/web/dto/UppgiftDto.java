package se.ltu.grupp10.canvas.web.dto;

import se.ltu.grupp10.canvas.domain.Assignment;

public record UppgiftDto(Long id, String namn) {

    public static UppgiftDto fromAssignment(Assignment assignment) {
        return new UppgiftDto(assignment.getId(), assignment.getTitle());
    }
}
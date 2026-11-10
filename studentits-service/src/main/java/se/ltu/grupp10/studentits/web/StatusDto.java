package se.ltu.grupp10.studentits.web;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatusDto(String status, String orsak) {}

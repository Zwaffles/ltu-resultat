package se.ltu.grupp10.studentits.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class FelHanterare {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StatusDto> fel(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(new StatusDto("fel", e.getReason()));
    }
}

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

/*
FelHanterare är en @RestControllerAdvice som fångar de undantag som uppstår i tjänsten och omvandlar dem till rätt HTTP-statuskod och ett JSON-svar i StatusDto-formatet.
I StudentITS handlar det om ResponseStatusException, som används när en användare inte finns och ger 404 med status "fel" och en orsakstext.
Genom att hanteringen ligger på ett ställe slipper controllern try/catch-block, och alla fel ser likadana ut för den som anropar.
 */
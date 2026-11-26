package se.ltu.grupp10.epok.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/*
FelHanterare är en @RestControllerAdvice som fångar de undantag som uppstår i tjänsten och omvandlar dem till rätt HTTP-statuskod och ett JSON-svar i StatusDto-formatet.
I Epok handlar det om ResponseStatusException, som används när en kurs inte finns och ger 404 med status "fel" och en orsakstext.
Genom att hanteringen ligger på ett ställe slipper controllern try/catch-block, och alla fel ser likadana ut för den som anropar.
 */

@RestControllerAdvice
public class FelHanterare {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StatusDto> fel(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(new StatusDto("fel", e.getReason()));
    }
}

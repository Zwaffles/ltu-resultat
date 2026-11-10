package se.ltu.grupp10.ladok.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import se.ltu.grupp10.ladok.service.HinderException;

/*
FelHanterare är en @RestControllerAdvice som fångar de undantag som uppstår i tjänsten och omvandlar dem till rätt HTTP-statuskod och ett JSON-svar i StatusDto-formatet.
Ett HinderException blir 422 med status “hinder” och en orsakstext.
Ett ResponseStatusException, som används för trasiga anrop med saknade fält, behåller sin statuskod (400).
Ogiltig JSON eller felaktigt datumformat ger också 400 med en förklaring.
Genom att hanteringen ligger på ett ställe slipper controllern try/catch-block, och alla fel ser likadana ut för den som anropar.
 */

@RestControllerAdvice
public class FelHanterare {

    @ExceptionHandler(HinderException.class)
    public ResponseEntity<StatusDto> hinder(HinderException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new StatusDto("hinder", e.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StatusDto> fel(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(new StatusDto("fel", e.getReason()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StatusDto> ogiltigJson(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(new StatusDto("fel", "Ogiltig JSON eller datumformat (ÅÅÅÅ-MM-DD)"));
    }
}

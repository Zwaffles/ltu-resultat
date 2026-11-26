package se.ltu.grupp10.canvas.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/** Calls StudentITS's get_Persnummer over REST. */
@Component
public class StudentItsClient {

    private final RestClient studentIts;

    public StudentItsClient(@Qualifier("studentItsKlient") RestClient studentIts) {
        this.studentIts = studentIts;
    }

    /**
     * Returns the student, or empty if StudentITS answers 404.
     * Throws RestClientException if StudentITS cannot be reached or answers with another error.
     */
    public Optional<StudentItsSvar> findStudent(String anvandarnamn) {
        try {
            StudentItsSvar svar = studentIts.get()
                    .uri("/studenter/{anvandarnamn}/personnummer", anvandarnamn)
                    .retrieve()
                    .body(StudentItsSvar.class);
            return Optional.ofNullable(svar);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}
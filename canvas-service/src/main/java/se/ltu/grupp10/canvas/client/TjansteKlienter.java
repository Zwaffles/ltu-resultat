package se.ltu.grupp10.canvas.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * HTTP clients for the systems that the process reg_Student_Resultat uses.
 * Canvas talks to the other systems only through their REST interfaces,
 * never through their databases.
 *
 * Base URLs come from application.properties (tjanster.*.url).
 */
@Configuration
public class TjansteKlienter {

    @Bean
    public RestClient epokKlient(@Value("${tjanster.epok.url}") String url) {
        return RestClient.create(url);
    }

    @Bean
    public RestClient studentItsKlient(@Value("${tjanster.studentits.url}") String url) {
        return RestClient.create(url);
    }

    @Bean
    public RestClient ladokKlient(@Value("${tjanster.ladok.url}") String url) {
        return RestClient.create(url);
    }
}

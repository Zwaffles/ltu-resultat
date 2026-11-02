package se.ltu.grupp10.epok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Epok: runs as its own system on port 8081 with base path /epok.
 * Simulates a separate server in the EA overview (Figure 3).
 */
@SpringBootApplication
public class EpokApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpokApplication.class, args);
    }
}

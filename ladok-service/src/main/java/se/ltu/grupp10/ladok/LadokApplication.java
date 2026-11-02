package se.ltu.grupp10.ladok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ladok: runs as its own system on port 8083 with base path /ladok.
 * Simulates a separate server in the EA overview (Figure 3).
 */
@SpringBootApplication
public class LadokApplication {

    public static void main(String[] args) {
        SpringApplication.run(LadokApplication.class, args);
    }
}

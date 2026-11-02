package se.ltu.grupp10.studentits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * StudentITS: runs as its own system on port 8082 with base path /studentits.
 * Simulates a separate server in the EA overview (Figure 3).
 */
@SpringBootApplication
public class StudentItsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentItsApplication.class, args);
    }
}

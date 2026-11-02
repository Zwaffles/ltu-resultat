package se.ltu.grupp10.canvas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Canvas: runs as its own system on port 8084 with base path /canvas.
 * Simulates a separate server in the EA overview (Figure 3).
 */
@SpringBootApplication
public class CanvasApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanvasApplication.class, args);
    }
}

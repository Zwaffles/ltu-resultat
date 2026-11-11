package se.ltu.grupp10.studentits.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "student_accounts")
public class StudentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;     // e.g. "abc-123"
    private String email;        // e.g. "abc-123@student.ltu.se"
    private String personnummer; // Student national ID
    private boolean active;

    public StudentAccount() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPersonnummer() { return personnummer; }
    public void setPersonnummer(String personnummer) { this.personnummer = personnummer; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
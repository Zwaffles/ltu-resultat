package se.ltu.grupp10.ladok.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "course_registrations")
public class CourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personnummer;
    private String kurskod;

    public CourseRegistration() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPersonnummer() { return personnummer; }
    public void setPersonnummer(String personnummer) { this.personnummer = personnummer; }

    public String getKurskod() { return kurskod; }
    public void setKurskod(String kurskod) { this.kurskod = kurskod; }
}
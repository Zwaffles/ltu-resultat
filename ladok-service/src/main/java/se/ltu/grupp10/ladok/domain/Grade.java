package se.ltu.grupp10.ladok.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personnummer;
    private String kurskod;
    private String grade;
    private LocalDate examinationDate;

    public Grade() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPersonnummer() { return personnummer; }
    public void setPersonnummer(String personnummer) { this.personnummer = personnummer; }

    public String getKurskod() { return kurskod; }
    public void setKurskod(String kurskod) { this.kurskod = kurskod; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public LocalDate getExaminationDate() { return examinationDate; }
    public void setExaminationDate(LocalDate examinationDate) { this.examinationDate = examinationDate; }
}
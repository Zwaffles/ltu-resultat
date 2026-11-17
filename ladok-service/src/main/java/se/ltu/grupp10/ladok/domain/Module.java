package se.ltu.grupp10.ladok.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kurskod;
    private String modulkod;
    private boolean active;

    public Module() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKurskod() { return kurskod; }
    public void setKurskod(String kurskod) { this.kurskod = kurskod; }

    public String getModulkod() { return modulkod; }
    public void setModulkod(String modulkod) { this.modulkod = modulkod; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
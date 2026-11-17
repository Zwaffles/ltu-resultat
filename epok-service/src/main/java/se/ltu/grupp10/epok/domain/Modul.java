package se.ltu.grupp10.epok.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "modules")
public class Modul {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kurskod;
    private String modulkod;
    private String benamning;   // e.g. "Inlämningsuppgift"
    private Double hp;          // e.g. 3.0
    private boolean aktiv;

    public Modul() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKurskod() { return kurskod; }
    public void setKurskod(String kurskod) { this.kurskod = kurskod; }

    public String getModulkod() { return modulkod; }
    public void setModulkod(String modulkod) { this.modulkod = modulkod; }

    public String getBenamning() { return benamning; }
    public void setBenamning(String benamning) { this.benamning = benamning; }

    public Double getHp() { return hp; }
    public void setHp(Double hp) { this.hp = hp; }

    public boolean isAktiv() { return aktiv; }
    public void setAktiv(boolean aktiv) { this.aktiv = aktiv; }
}
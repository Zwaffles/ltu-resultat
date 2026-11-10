package se.ltu.grupp10.ladok.repository;

public interface ModulKontroll {
    boolean finnsOchArAktiv(String kurskod, String modul);
}

/*
ModulKontroll är ett gränssnitt som svarar på om en modul finns och är aktiv för en given kurs.
 */

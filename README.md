# ltu-resultat: Registrera studieresultat

D0031N Enterprise Architecture & SOA, examinationsuppgift 3, grupp 10.

En prototyp för processen *Registrera studieresultat*: fyra fristående REST-webbtjänster som simulerar separata system, plus ett JavaFX-gränssnitt. Varje system har egen port, egen bas-sökväg och (när roll A lagt till dem) egen databas, i linje med EA-översikten i figur 3.

| Modul | System | Port | Bas-URL | Tjänst enligt uppgiften | Ansvar |
|---|---|---|---|---|---|
| `epok-service` | Epok | 8081 | `http://localhost:8081/epok` | `get_Modul` | B |
| `studentits-service` | StudentITS | 8082 | `http://localhost:8082/studentits` | `get_Persnummer` | B |
| `ladok-service` | Ladok | 8083 | `http://localhost:8083/ladok` | `reg_Resultat` | B |
| `canvas-service` | Canvas | 8084 | `http://localhost:8084/canvas` | `reg_Student_Resultat` | C |
| `resultat-gui` | Resultat Canvas GUI | – | – | (klient) | D |
| databaser, scheman, testdata | Postgres | 5432 | – | – | A |

## Krav

- **JDK 21** (t.ex. Temurin 21). Kontrollera med `java -version`.
- **IntelliJ IDEA** (Community räcker). Maven ingår i IntelliJ.
- **Docker Desktop**, när roll A har lagt till `docker-compose.yml`.

## Kom igång

1. Klona repot och öppna **mappen** (`ltu-resultat/`) i IntelliJ. IntelliJ känner igen Maven-projektet och laddar ner beroendena.
2. Ställ in *File → Project Structure → SDK* på Java 21.
3. Välj körkonfigurationen **Alla tjänster** uppe till höger och klicka Run. Alla fyra tjänsterna startar.
4. Välj **Resultat GUI** och klicka Run. Ett tomt fönster ska öppnas.
5. Öppna `http/health.http` och kör anropen för att testa tjänsterna direkt.

## Projektstruktur

```
ltu-resultat/
├── pom.xml                  föräldraprojekt (Spring Boot 4.0, Java 21, JavaFX 21)
├── .run/                    delade körkonfigurationer för IntelliJ
├── http/                    .http-filer för att testa tjänsterna
├── epok-service/            se.ltu.grupp10.epok
├── studentits-service/      se.ltu.grupp10.studentits
├── ladok-service/           se.ltu.grupp10.ladok
├── canvas-service/          se.ltu.grupp10.canvas
└── resultat-gui/            se.ltu.grupp10.gui
```

Varje tjänst har samma paketindelning:

- `domain/`: JPA-entiteter (ägs av roll A)
- `repository/`: Spring Data JPA-repositories
- `web/`: REST-controllers. URL:er och JSON följer API-kontraktet i planen.

Tjänsterna har avsiktligt **ingen delad kodmodul**. Varje system ska vara fristående och bara kommunicera via sitt REST-gränssnitt, även om det innebär att små DTO-klasser finns i flera moduler.

## Regler

- **Gränssnittet pratar bara HTTP.** Inga JPA-, Spring- eller databasberoenden i `resultat-gui`.
- **Tjänsterna pratar bara HTTP med varandra.** Canvas anropar Epok, StudentITS och Ladok via `RestClient` (se `canvas-service/.../client/TjansteKlienter.java`), aldrig via deras databaser.
- **Bara roll A ändrar databasscheman**, via nya Flyway-migreringar i `src/main/resources/db/migration`. Tjänsterna körs med `spring.jpa.hibernate.ddl-auto=validate`.
- **Ingen hårdkodad data** i Javakoden. Testdata ligger i migreringarna.
- **Ingen pushar direkt till `main`.** Gör en branch per uppgift (`feature/ladok-validering`, `feature/gui-studenttabell` …), öppna en pull request och låt någon annan granska.

## Nästa steg per roll

- **A:** lägg till `docker-compose.yml` (Postgres med databaserna `epok`, `studentits`, `ladok`, `canvas`), avkommentera databasberoendena i tjänsternas `pom.xml` och inställningarna i `application.properties`, och skriv de första Flyway-migreringarna med testdata.
- **B:** controllers för `GET /epok/kurser/{kurskod}/moduler`, `GET /studentits/studenter/{anvandarnamn}/personnummer` och `POST /ladok/resultat` med "hinder"-reglerna. Börja mot mockdata tills A:s scheman finns.
- **C:** Canvas läs-endpoints och `POST /canvas/kurser/{kurskod}/registrering` (orkestreringen). Klienterna mot de andra systemen finns redan.
- **D:** bygg gränssnittet i `main-view.fxml` och `MainController` (se figur 2 i uppgiften), samt klientklasser som anropar tjänsterna med `java.net.http.HttpClient`. Gör anropen utanför JavaFX-tråden så att fönstret inte fryser.

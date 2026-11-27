# Registrera studieresultat – REST-webbtjänster

**D0031N Enterprise Architecture och SOA · Examinationsuppgift 3 · Grupp 10**
Nelly Norlin, André Söderholm Östling, Johan Tauriainen, Andreas Zweifel

En fungerande prototyp av processen *Registrera studieresultat*. En lärare väljer kurs, uppgift och modul i ett gränssnitt, sätter betyg och datum, och skickar resultaten till Ladok. Lösningen består av fyra fristående system som pratar med varandra via REST och JSON, och ett JavaFX-gränssnitt ovanpå.

---

## Innehåll

1. [Arkitektur](#arkitektur)
2. [Krav](#krav)
3. [Kom igång](#kom-igång)
4. [Köra demon](#köra-demon)
5. [Testa tjänsterna direkt](#testa-tjänsterna-direkt)
6. [API](#api)
7. [Testdata](#testdata)
8. [Felsökning](#felsökning)
9. [Projektstruktur](#projektstruktur)
10. [Avgränsningar](#avgränsningar)

---

## Arkitektur

Varje system körs som en egen Spring Boot-applikation med egen port, egen bas-sökväg och egen databas, i linje med EA-översikten (figur 3) i uppgiften. Systemen läser aldrig varandras databaser, utan allt utbyte sker via REST.

| System | Tjänst enligt uppgiften | Port | Bas-URL | Databas |
|---|---|---|---|---|
| **Epok** | `get_Modul` | 8081 | `http://localhost:8081/epok` | `epok_db` |
| **StudentITS** | `get_Persnummer` | 8082 | `http://localhost:8082/studentits` | `studentits_db` |
| **Ladok** | `reg_Resultat` | 8083 | `http://localhost:8083/ladok` | `ladok_db` |
| **Canvas** | `reg_Student_Resultat` (process) | 8084 | `http://localhost:8084/canvas` | `canvas_db` |
| **Resultat Canvas GUI** | – | – | (JavaFX-klient) | – |

Alla fyra databaser ligger i samma PostgreSQL-container men är helt separata. Schema och testdata byggs upp automatiskt av Flyway när respektive tjänst startar.

### Flödet

```
GUI ── välj kurs/uppgift ──▶ Canvas            (kurser, uppgifter, studenter med omdömen)
GUI ── välj modul ─────────▶ Epok              (aktiva moduler för kursen)

GUI ── Skicka ─────────────▶ Canvas: reg_Student_Resultat
                               för varje rad:
                                 ├─▶ StudentITS   användarnamn → personnummer, aktiv
                                 └─▶ Ladok        registrerar resultatet eller svarar med hinder
GUI ◀── status per rad ───── Canvas            (registrerad / hinder / fel)
```

---

## Krav

| Verktyg | Version | Kommentar |
|---|---|---|
| **JDK** | 21 | T.ex. Temurin 21 eller Microsoft OpenJDK 21 |
| **IntelliJ IDEA** | 2024 eller senare | Community räcker. Maven ingår i IntelliJ. |
| **Docker Desktop** | – | Kör PostgreSQL-databasen |

Inga andra installationer behövs. JavaFX och alla bibliotek hämtas automatiskt av Maven.

---

## Kom igång

### 1. Starta databasen

Öppna en terminal i projektets rotmapp (där `docker-compose.yml` ligger) och kör:

```bash
docker compose up -d
```

Det startar containern `postgres-ltu-db` på **port 5433** och skapar de fyra databaserna. Tabeller och testdata skapas först när tjänsterna startar (steg 4).

### 2. Öppna projektet i IntelliJ

Välj **File → Open** och öppna **rotmappen** (`ltu-resultat/`, där `pom.xml` ligger). IntelliJ känner igen Maven-projektet med dess fem moduler och laddar ner beroendena. Det kan ta en stund första gången.

### 3. Ställ in Java 21

**File → Project Structure → Project → SDK:** välj en JDK 21. Om ingen finns kan IntelliJ ladda ner en via *Add SDK → Download JDK*.

### 4. Starta tjänsterna

Välj körkonfigurationen **Alla tjänster** uppe till höger och klicka **Run**. Det startar Epok, StudentITS, Ladok och Canvas samtidigt. Varje tjänst kör sina Flyway-migreringar vid start.

Kontrollera att allt är igång genom att öppna `http/health.http` och köra anropen (den gröna pilen vid varje anrop). Anropet `GET /canvas/systemstatus` visar status för alla fyra system på en gång:

```json
{ "Canvas": "UP", "Epok": "UP", "StudentITS": "UP", "Ladok": "UP" }
```

### 5. Starta gränssnittet

Välj körkonfigurationen **Resultat GUI** och klicka **Run**. Fönstret hämtar kurserna från Canvas direkt vid start.

---

## Köra demon

Börja från en ren databas, så att inga tidigare testregistreringar ligger kvar:

```bash
docker compose down
docker compose up -d
```

Starta sedan **Alla tjänster** och **Resultat GUI**, och gå igenom flödet:

1. **Välj kurs** `D0031N` och klicka **Hämta uppgifter**. Uppgifterna kommer från Canvas och modulerna från Epok. Modul 0003 visas inte, eftersom den är inaktiv.
2. **Välj uppgift** *Inlämningsuppgift 1: Mikrotjänster*. Studenterna och deras omdömen hämtas från Canvas.
3. **Välj modul** *0005 – Inlämningsuppgift*.
4. Klicka **Markera alla**, välj ett datum och klicka **Sätt datum**. Välj ett betyg i Ladok för varje rad.
5. Klicka **Skicka markerade**. Statuskolumnen visar utfallet per student:

| Student | Förväntat utfall | Avgörs av |
|---|---|---|
| oscnil-6, annand-1, johjoh-2, elikar-3, marber-4 | Registrerad | Ladok |
| vikpet-8 | Hinder: studentens konto är inaktivt | StudentITS |
| sarlin-5, majols-7 | Hinder: studenten är inte registrerad på kursen | Ladok |
| okand-10 | Hinder: studenten finns inte i StudentITS | StudentITS |
| emmsve-9 | Saknar omdöme i Canvas. Utan valt betyg stoppas raden i gränssnittet; med betyg blir det hinder eftersom kontot är inaktivt | GUI / StudentITS |

6. Skicka **samma rader igen**. De som registrerades får nu *Resultat redan registrerat*, vilket visar Ladoks kontroll mot dubbletter.

---

## Testa tjänsterna direkt

Mappen `http/` innehåller färdiga anrop för IntelliJ:s inbyggda HTTP-klient. Öppna en fil och klicka på den gröna pilen vid ett anrop för att köra det.

| Fil | Innehåll |
|---|---|
| `health.http` | Hälsokontroll för alla fyra system och `/canvas/systemstatus` |
| `epok.http` | Moduler per kurs, inklusive okänd kurs (404) |
| `studentits.http` | Personnummer och kontostatus, inklusive okänd användare (404) |
| `ladok.http` | Registrering, varje hinder-regel (422) och felaktiga anrop (400) |
| `canvas.http` | Kurser, uppgifter, studenter och hela `reg_Student_Resultat` med en rad per demofall |

Den sista begäran i `canvas.http` kör hela demoflödet utan gränssnittet och fungerar som reserv om GUI:t inte går att starta.

---

## API

Alla tjänster tar emot och returnerar JSON. Datum skrivs som `ÅÅÅÅ-MM-DD`.

### Epok

| Metod | Resurs | Svar |
|---|---|---|
| GET | `/epok/kurser/{kurskod}/moduler` | `200 [{ modulkod, benamning, hp }]`, endast aktiva moduler · `404` okänd kurs |

### StudentITS

| Metod | Resurs | Svar |
|---|---|---|
| GET | `/studentits/studenter/{anvandarnamn}/personnummer` | `200 { anvandarnamn, personnummer, aktiv }` · `404` okänd användare |

### Ladok

| Metod | Resurs | Svar |
|---|---|---|
| POST | `/ladok/resultat` | Body: `{ personnummer, kurskod, modul, datum, betyg }` |
| | | `201 { "status": "registrerad" }` |
| | | `422 { "status": "hinder", "orsak": "…" }` om en affärsregel bryts |
| | | `400 { "status": "fel", "orsak": "…" }` om begäran saknar fält eller har fel format |
| GET | `/ladok/resultat?kurskod=…&modul=…` | `200 [{ personnummer, kurskod, modul, datum, betyg }]` |

Ladok ger *hinder* när studenten inte är registrerad på kursen, modulen inte finns eller är inaktiv, betyget inte är U/G/VG, datumet ligger i framtiden eller resultatet redan är registrerat.

### Canvas

| Metod | Resurs | Svar |
|---|---|---|
| GET | `/canvas/kurser` | `200 [{ kurskod }]` |
| GET | `/canvas/kurser/{kurskod}/uppgifter` | `200 [{ id, namn }]`, sorterade på deadline |
| GET | `/canvas/kurser/{kurskod}/uppgifter/{id}/studenter` | `200 [{ anvandarnamn, namn, omdome }]` · `404` okänd uppgift eller uppgift i annan kurs |
| POST | `/canvas/kurser/{kurskod}/registrering` | Body: `{ modul, rader: [{ anvandarnamn, datum, betyg }] }` |
| | | `200 [{ anvandarnamn, status, orsak }]`, en status per rad · `400` utan modul eller rader |

`POST /registrering` är processen `reg_Student_Resultat`. Varje rad hanteras oberoende: Canvas hämtar personnumret från StudentITS och skickar sedan resultatet till Ladok. Ett hinder för en student stoppar inte de övriga raderna.

### Övrigt

Varje tjänst har `GET /{system}/health`, och Canvas har `GET /canvas/systemstatus` som kontrollerar att Canvas når de andra tre systemen.

---

## Testdata

Testdatan ligger i Flyway-migreringarna under varje tjänsts `src/main/resources/db/migration/` och är utformad så att demon visar både lyckade registreringar och varje typ av hinder. Studenterna har samma användarnamn i Canvas och StudentITS, och samma personnummer i StudentITS och Ladok.

| Användarnamn | Särskilt i testdatan |
|---|---|
| oscnil-6, annand-1, johjoh-2, elikar-3, marber-4 | Registrerade på D0031N, inget resultat på modul 0005 |
| vikpet-8, emmsve-9 | Inaktiva konton i StudentITS |
| sarlin-5, majols-7 | Inte registrerade på D0031N i Ladok |
| okand-10 | Finns bara i Canvas, inte i StudentITS |

Kurs D0031N har modulerna 0001 *Tentamen*, 0002 *Laborationer*, 0003 *Seminarium* (inaktiv) och 0005 *Inlämningsuppgift*.

---

## Felsökning

| Problem | Orsak och lösning |
|---|---|
| Tjänsterna startar inte: `Connection to localhost:5433 refused` | Docker körs inte. Starta Docker Desktop och kör `docker compose up -d`. |
| `password authentication failed for user "postgres"` | En annan PostgreSQL svarar på porten. Kontrollera att `docker compose up -d` är körd och att ingen lokal PostgreSQL använder port 5433. |
| `database "canvas_db" does not exist` | Containern skapades innan `init-dbs.sql` fanns. Kör `docker compose down` och sedan `docker compose up -d`. |
| `Validate failed: Migration checksum mismatch` | En redan körd migrering har ändrats lokalt. Återställ databasen med `docker compose down` och `docker compose up -d`. |
| Demon visar *Resultat redan registrerat* direkt | Registreringar från tidigare körningar ligger kvar. Återställ databasen enligt ovan. |
| Gränssnittet säger att Canvas eller Epok inte svarar | Tjänsterna är inte igång. Starta **Alla tjänster** och kontrollera med `http/health.http`. |
| Röda fel i IntelliJ efter att projektet öppnats | Maven har inte laddat klart. Klicka **Load Maven Changes** (Maven-ikonen uppe till höger) och kontrollera att projektets SDK är Java 21. |

Enhetstesterna (JUnit 5 och Mockito) körs med högerklick på en testmapp → **Run Tests**. Testet `contextLoads` i varje tjänst startar hela applikationen och kräver därför att databasen är igång.

---

## Projektstruktur

```
ltu-resultat/
├── pom.xml                  Föräldraprojekt: Spring Boot 4, Java 21, JavaFX 21
├── docker-compose.yml       PostgreSQL 15 på port 5433
├── init-dbs.sql             Skapar de fyra databaserna
├── .run/                    Körkonfigurationer för IntelliJ
├── http/                    Färdiga anrop för att testa tjänsterna
├── epok-service/            Epok        se.ltu.grupp10.epok
├── studentits-service/      StudentITS  se.ltu.grupp10.studentits
├── ladok-service/           Ladok       se.ltu.grupp10.ladok
├── canvas-service/          Canvas      se.ltu.grupp10.canvas
└── resultat-gui/            JavaFX-gränssnitt, ren REST-klient
```

Varje tjänst följer samma uppdelning:

| Paket | Innehåll |
|---|---|
| `web` | REST-controllers och svarsklasser (DTO:er) |
| `service` | Affärslogik och regler |
| `repository` | Spring Data JPA-repositories |
| `domain` | JPA-entiteter |
| `client` | (Canvas) klienter mot StudentITS och Ladok |

Tjänsterna delar avsiktligt ingen kod. Varje system är fristående och kommunicerar bara via sitt REST-gränssnitt. Gränssnittet har inga beroenden till databaser eller Spring.

**Tekniker:** Java 21, Spring Boot 4 (Spring Web MVC, Spring Data JPA), Hibernate, PostgreSQL 15, Flyway, Docker Compose, JavaFX 21 med FXML, Jackson, JUnit 5 och Mockito.

---

## Avgränsningar

Prototypen fokuserar på integrationen mellan systemen och gör några medvetna förenklingar:

- **Ingen inloggning eller behörighetskontroll.** I ett verkligt system skulle det avgöra vilken lärare som får rapportera på vilken kurs.
- **Canvas härleder kurslistan från uppgifterna** i stället för att ha en egen kurstabell.
- **Personnumret skickas från StudentITS via Canvas till Ladok**, som uppgiften beskriver. Canvas använder det bara under anropet och lagrar eller visar det aldrig.
- **Anropen till StudentITS och Ladok görs rad för rad**, vilket räcker för en prototyp.
- **Alla tjänster körs lokalt** med fasta adresser, angivna i varje tjänsts `application.properties` och i gränssnittets `gui.properties`.

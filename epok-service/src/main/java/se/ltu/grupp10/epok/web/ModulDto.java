package se.ltu.grupp10.epok.web;

import java.math.BigDecimal;

public record ModulDto(String modulkod, String benamning, BigDecimal hp) {}

/*
ModulDto är en record som representerar en modul i den form som Epok skickar ut.
Den innehåller modulkod, benämning och antal högskolepoäng, vilket följer API-kontraktet för get_Modul.
Högskolepoängen är av typen BigDecimal eftersom de kan ha decimaler (till exempel 1,5 hp) och ska undvika avrundningsfel.
Jackson omvandlar objektet automatiskt till JSON, och som record är klassen oföränderlig, vilket passar en DTO som bara bär data.
 */

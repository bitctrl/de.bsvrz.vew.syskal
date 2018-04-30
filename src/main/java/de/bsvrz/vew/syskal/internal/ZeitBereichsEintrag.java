/*
 * SWE Systemkalender - Version 2
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49-341-49067-0
 * Fax: +49-341-49067-15
 * mailto: info@bitctrl.de
 */

package de.bsvrz.vew.syskal.internal;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentation der Daten eines {@link KalenderEintrag}, der durch einen
 * Zeitbereich definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitBereichsEintrag extends KalenderEintrag {

    private static final Debug LOGGER = Debug.getLogger();

    private LocalDateTime start;
    private LocalDateTime ende;

    /** das Datumsformat mit Zeitangabe. */
    private static String formatMitZeit = "dd.MM.yyyy HH:mm:ss,SSS";

    /** verkürztes Datumsformat für 0 Uhr. */
    private static String formatOhneZeit = "dd.MM.yyyy";

    /**
     * erzeugt eine neue Instanz eines {@link ZeitBereichsEintrag} aus den
     * übergebenen Daten.
     * 
     * @param name
     *            der Name des Eintrags
     * @param start
     *            der Anfangszeitpunkt des Gültigkeitsbereichs
     * @param ende
     *            der Endzeitpunkt des Gültigkeitsbereichs
     * @param grenzen
     *            die Liste der Zeitgrenzen innerhalb eines Tages
     * @return die neue Instanz
     */
    public static ZeitBereichsEintrag of(String name, LocalDateTime start, LocalDateTime ende,
            List<ZeitGrenze> grenzen) {
        ZeitBereichsEintrag eintrag = new ZeitBereichsEintrag(name, start, ende);

        for (ZeitGrenze grenze : grenzen) {
            eintrag.addZeitGrenze(grenze);
        }

        eintrag.setDefinition(eintrag.toString());
        return eintrag;
    }

    /**
     * Konstruktor.
     * 
     * @param name
     *            der Name des Eintrags
     * @param definition
     *            der definierende Textstring
     */
    public ZeitBereichsEintrag(final String name, final String definition) {
        super(name, definition);
        if (definition == null) {
            start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
            ende = start.plusDays(1);
        } else {
            if (definition.contains("-")) {
                final String[] parts = definition.split("-");
                try {
                    start = parseDatum(parts[0].trim());
                    ende = parseDatum(parts[1].trim());
                    if (!ende.toLocalTime().isAfter(LocalTime.MIDNIGHT)) {
                        ende = ende.plusDays(1);
                    }
                } catch (final ParseException e) {
                    String message = "Fehler beim Parsen des Eintrags: " + definition + ": " + e.getLocalizedMessage();
                    LOGGER.warning(message);
                    addFehler(message);
                }
            } else {
                if (definition.length() > 0) {
                    addFehler("Trenner '-' nicht im Definitionsstring enthalten");
                }
                start = SystemKalender.MIN_DATETIME;
                ende = SystemKalender.MAX_DATETIME;
            }
        }
    }

    private ZeitBereichsEintrag(String name, LocalDateTime start, LocalDateTime ende) {
        super(name, null);
        this.start = start;
        this.ende = ende;
    }

    @Override
    public EintragsArt getEintragsArt() {
        return EintragsArt.DATUMSBEREICH;
    }

    /**
     * liefert den Endzeitpunkt in Millisekunden.
     * 
     * @return den Zeitpunkt
     */
    public LocalDateTime getEnde() {
        return ende;
    }

    /**
     * liefert den Anfangszeitpunkt in Millisekunden.
     * 
     * @return den Zeitpunkt
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * liefert die zugewiesenen Zeitgrenzen als Textstring.
     * 
     * @return den Text
     */
    String getZeitBereicheAlsString() {
        final StringBuffer buffer = new StringBuffer();

        if (getZeitGrenzen().size() > 0) {
            buffer.append('(');
            for (final ZeitGrenze bereich : getZeitGrenzen()) {
                buffer.append(bereich.toString());
            }
            buffer.append(')');
        }

        return buffer.toString();
    }

    /**
     * interpretiert den übergebenen Text als Datumsangabe mit optionaler Zeit.
     * 
     * @param string
     *            der Textstring
     * @return die Zeit in Millisekunden
     * @throws ParseException
     *             der Text konnte nicht als Zeit oder Datum interpretiert
     *             werden
     */
    private LocalDateTime parseDatum(final String string) throws ParseException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.M.u[ H[:m[:s[.SSS]]]]");
        try {
            return LocalDateTime.parse(string.replace(',', '.'), dateTimeFormatter);
        } catch (@SuppressWarnings("unused") DateTimeParseException e) {
            try {
                return LocalDateTime.of(LocalDate.parse(string.replace(',', '.'), dateTimeFormatter),
                        LocalTime.MIDNIGHT);
            } catch (DateTimeParseException exception) {
                throw new ParseException(exception.getLocalizedMessage(), exception.getErrorIndex());
            }
        }
    }

    @Override
    public String toString() {

        final StringBuffer buffer = new StringBuffer(getName());
        buffer.append(":=");
        if (start.isAfter(SystemKalender.MIN_DATETIME) && ende.isBefore(SystemKalender.MAX_DATETIME)) {
            buffer.append('<');
            start.format(verwendetesFormat(start));
            buffer.append(start.format(verwendetesFormat(start)));
            buffer.append('-');
            buffer.append(ende.format(verwendetesFormat(ende)));
            buffer.append('>');
        }

        buffer.append(getZeitBereicheAlsString());

        return buffer.toString();
    }

    /**
     * ermittelt das Format für die Ausgabe des Datums. Wenn Stunde, Minute und
     * Sekunde den Wert 0 haben, wird das verkürzte Format verwendet.
     * 
     * @param kalender
     *            die auszugebende Zeit
     * @return das Format
     */
    private DateTimeFormatter verwendetesFormat(final LocalDateTime zeitstempel) {
        if (zeitstempel.getHour() == 0) {
            if (zeitstempel.getMinute() == 0) {
                if (zeitstempel.getSecond() == 0) {
                    return DateTimeFormatter.ofPattern(ZeitBereichsEintrag.formatOhneZeit);
                }
            }
        }
        return DateTimeFormatter.ofPattern(ZeitBereichsEintrag.formatMitZeit);
    }

    @Override
    public boolean bestimmeGueltigkeit(LocalDateTime zeitPunkt) {
        if (zeitPunkt.isBefore(start) || !zeitPunkt.isBefore(ende)) {
            return false;
        }

        List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
        if (zeitGrenzen.isEmpty()) {
            return true;
        }

        LocalTime checkTime = zeitPunkt.toLocalTime();
        for (ZeitGrenze grenze : zeitGrenzen) {
            if (!checkTime.isBefore(grenze.getStart()) && checkTime.isBefore(grenze.getEnde())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {

        List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
        if (zeitGrenzen.isEmpty()) {
            return berechneZeitlicheGueltigkeitOhneZeitgrenzen(zeitpunkt);
        }

        if (zeitpunkt.isBefore(start)) {
            return SystemkalenderGueltigkeit.ungueltig(SystemKalender.MIN_DATETIME,
                    sucheFruehestMoeglichenIntervallStart(zeitGrenzen));
        }

        if (!zeitpunkt.isBefore(ende)) {
            return SystemkalenderGueltigkeit.of(
                    ZustandsWechsel.aufUngueltig(sucheSpaetestMoeglichesIntervallEnde(zeitGrenzen)),
                    ZustandsWechsel.aufUngueltig(SystemKalender.MAX_DATETIME));
        }

        for (ZeitGrenze grenze : zeitGrenzen) {
            SystemkalenderGueltigkeit gueltigkeit = berechneGueltigkeitMitGrenze(zeitpunkt, grenze);
            if (gueltigkeit != null) {
                return gueltigkeit;
            }
        }

        LocalDateTime aktivierungsZeit = getZeitPunktAmGrenzenEnde(zeitpunkt.toLocalDate(),
                zeitGrenzen.get(zeitGrenzen.size() - 1));
        LocalDateTime wechselZeit = getZeitPunktAmGrenzenStartOderDatum(zeitpunkt.toLocalDate().plusDays(1),
                zeitGrenzen.get(0), SystemKalender.MAX_DATETIME);
        return SystemkalenderGueltigkeit.ungueltig(aktivierungsZeit, wechselZeit);
    }

    private SystemkalenderGueltigkeit berechneGueltigkeitMitGrenze(LocalDateTime zeitpunkt, ZeitGrenze grenze) {
        LocalDateTime aktivierungsZeit = null;
        LocalDateTime wechselZeit = null;
        boolean gueltig = false;

        LocalDate datum = zeitpunkt.toLocalDate();
        LocalTime abfrageZeit = zeitpunkt.toLocalTime();

        if (abfrageZeit.equals(grenze.getStart())) {
            aktivierungsZeit = getZeitPunktAmGrenzenStart(datum, grenze);
            wechselZeit = getZeitPunktAmGrenzenEnde(datum, grenze);
            gueltig = true;
        } else if (abfrageZeit.isBefore(grenze.getStart())) {
            aktivierungsZeit = getZeitPunktAmVorigenGrenzenEnde(datum, grenze);
            wechselZeit = getZeitPunktAmGrenzenStart(datum, grenze);
        } else if (abfrageZeit.isBefore(grenze.getEnde())) {
            aktivierungsZeit = getZeitPunktAmGrenzenStartOderDatum(datum, grenze, start);
            wechselZeit = getZeitPunktAmGrenzenEndeOderDatum(datum, grenze, ende);
            gueltig = true;
        }

        if ((wechselZeit != null) && (aktivierungsZeit != null)) {
            if (gueltig) {
                return SystemkalenderGueltigkeit.gueltig(aktivierungsZeit, wechselZeit);
            }
            return SystemkalenderGueltigkeit.ungueltig(aktivierungsZeit, wechselZeit);
        }

        return null;

    }

    private LocalDateTime sucheSpaetestMoeglichenIntervallStart(List<ZeitGrenze> zeitGrenzen) {

        LocalDate endeDatum = ende.toLocalDate();
        LocalDateTime result = null;

        do {
            for (ZeitGrenze grenze : zeitGrenzen) {
                LocalDateTime pruefZeit = LocalDateTime.of(endeDatum, grenze.getStart());
                if (pruefZeit.isBefore(ende)) {
                    result = pruefZeit;
                } else {
                    break;
                }
            }

            endeDatum = endeDatum.minusDays(1);
        } while (result == null);

        return result;
    }

    private LocalDateTime sucheSpaetestMoeglichesIntervallEnde(List<ZeitGrenze> zeitGrenzen) {

        if (zeitGrenzen.isEmpty()) {
            return ende;
        }

        LocalDate endeDatum = ende.toLocalDate();

        List<ZeitGrenze> reverseGrenzen = new ArrayList<>(zeitGrenzen);
        Collections.reverse(reverseGrenzen);
        do {
            for (ZeitGrenze grenze : reverseGrenzen) {
                LocalDateTime grenzStart = LocalDateTime.of(endeDatum, grenze.getStart());
                LocalDateTime grenzEnde = LocalDateTime.of(endeDatum, grenze.getEnde());
                if (grenzEnde.isBefore(ende)) {
                    return grenzEnde;
                } else if (grenzStart.isBefore(ende)) {
                    return ende;
                }
            }

            endeDatum = endeDatum.minusDays(1);
        } while (!endeDatum.isBefore(start.toLocalDate()));

        return SystemKalender.MAX_DATETIME;
    }

    private LocalDateTime sucheFruehestMoeglichenIntervallStart(List<ZeitGrenze> zeitGrenzen) {

        if (zeitGrenzen.isEmpty()) {
            return start;
        }

        LocalDate startDatum = start.toLocalDate();

        do {
            for (ZeitGrenze grenze : zeitGrenzen) {
                LocalDateTime grenzStart = LocalDateTime.of(startDatum, grenze.getStart());
                LocalDateTime grenzEnde = LocalDateTime.of(startDatum, grenze.getEnde());

                if (grenzStart.isBefore(start) && grenzEnde.isBefore(start)) {
                    continue;
                }

                if (!start.isAfter(grenzEnde)) {
                    if (!grenzStart.isBefore(start)) {
                        return grenzStart;
                    }
                    return start;
                }
            }

            startDatum = startDatum.plusDays(1);
        } while (!startDatum.isAfter(ende.toLocalDate()));

        return SystemKalender.MIN_DATETIME;
    }

    @Override
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVor(LocalDateTime zeitpunkt) {

        List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
        if (zeitGrenzen.isEmpty()) {
            return berechneZeitlicheGueltigkeitVorOhneZeitgrenzen(zeitpunkt);
        }

        LocalDateTime fruehesterStart = sucheFruehestMoeglichenIntervallStart(zeitGrenzen);
        LocalDateTime spaetestesEnde = sucheSpaetestMoeglichesIntervallEnde(zeitGrenzen);

        if (zeitpunkt.isBefore(fruehesterStart)) {
            return SystemkalenderGueltigkeit.of(ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME),
                    ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME));
        }

        if (!zeitpunkt.isBefore(spaetestesEnde)) {
            return SystemkalenderGueltigkeit.gueltig(sucheSpaetestMoeglichenIntervallStart(zeitGrenzen),
                    spaetestesEnde);
        }

        final LocalDate datum = zeitpunkt.toLocalDate();
        LocalTime abfrageZeit = zeitpunkt.toLocalTime();

        for (ZeitGrenze grenze : zeitGrenzen) {

            if (abfrageZeit.equals(grenze.getStart())) {
                LocalDateTime aktivierungsZeit = getZeitPunktAmVorigenGrenzenEnde(datum, grenze);
                LocalDateTime wechselZeit = getZeitPunktAmGrenzenStart(datum, grenze);
                return SystemkalenderGueltigkeit.ungueltig(aktivierungsZeit, wechselZeit);
            }

            if (abfrageZeit.isBefore(grenze.getStart())) {
                LocalDateTime wechselZeit = getZeitPunktAmVorigenGrenzenEnde(datum, grenze);
                LocalDateTime aktivierungsZeit = getZeitPunktAmVorigenGrenzenStart(datum, grenze);
                return SystemkalenderGueltigkeit.gueltig(aktivierungsZeit, wechselZeit);
            }

            if (abfrageZeit.isBefore(grenze.getEnde())) {
                LocalDateTime aktivierungsZeit = getZeitPunktAmVorigenGrenzenEnde(datum, grenze);
                LocalDateTime wechselZeit = LocalDateTime.of(datum, grenze.getStart());
                if (wechselZeit.isBefore(start)) {
                    wechselZeit = sucheFruehestMoeglichenIntervallStart(zeitGrenzen);
                }

                return SystemkalenderGueltigkeit.ungueltig(aktivierungsZeit, wechselZeit);
            }

        }

        LocalDateTime wechselZeit = getZeitPunktAmGrenzenEnde(datum, zeitGrenzen.get(zeitGrenzen.size() - 1));
        LocalDateTime aktivierungsZeit = getZeitPunktAmGrenzenStart(datum, zeitGrenzen.get(zeitGrenzen.size() - 1));
        return SystemkalenderGueltigkeit.gueltig(aktivierungsZeit, wechselZeit);

    }

    private SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitOhneZeitgrenzen(LocalDateTime zeitPunkt) {

        if (zeitPunkt.isBefore(start)) {
            return SystemkalenderGueltigkeit.ungueltig(SystemKalender.MIN_DATETIME, start);
        }

        if (zeitPunkt.isBefore(ende)) {
            return SystemkalenderGueltigkeit.gueltig(start, ende);
        }

        return SystemkalenderGueltigkeit.of(ZustandsWechsel.aufUngueltig(ende),
                ZustandsWechsel.aufUngueltig(SystemKalender.MAX_DATETIME));
    }

    private SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVorOhneZeitgrenzen(LocalDateTime zeitPunkt) {

        if (zeitPunkt.isBefore(start)) {
            return SystemkalenderGueltigkeit.of(ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME),
                    ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME));
        }

        if (zeitPunkt.isBefore(ende)) {
            return SystemkalenderGueltigkeit.ungueltig(SystemKalender.MIN_DATETIME, start);
        }

        return SystemkalenderGueltigkeit.gueltig(start, ende);
    }

    private LocalDateTime getZeitPunktAmGrenzenStartOderDatum(LocalDate datum, ZeitGrenze grenze,
            LocalDateTime ersatz) {

        LocalDateTime result = LocalDateTime.of(datum, grenze.getStart());
        if (result.isBefore(start)) {
            return ersatz;
        }
        return result;
    }

    private LocalDateTime getZeitPunktAmGrenzenStart(LocalDate datum, ZeitGrenze grenze) {
        return getZeitPunktAmGrenzenStartOderDatum(datum, grenze, SystemKalender.MIN_DATETIME);
    }

    private LocalDateTime getZeitPunktAmVorigenGrenzenStart(LocalDate datum, ZeitGrenze grenze) {

        List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
        int grenzIndex = zeitGrenzen.indexOf(grenze);
        LocalDateTime result = null;

        if (grenzIndex > 0) {
            result = LocalDateTime.of(datum, zeitGrenzen.get(grenzIndex - 1).getStart());
        } else {
            result = LocalDateTime.of(datum.minusDays(1), zeitGrenzen.get(zeitGrenzen.size() - 1).getStart());
        }
        if (result.isBefore(start)) {
            result = sucheFruehestMoeglichenIntervallStart(zeitGrenzen);
        }
        return result;
    }

    private LocalDateTime getZeitPunktAmGrenzenEndeOderDatum(LocalDate datum, ZeitGrenze grenze, LocalDateTime ersatz) {

        LocalDateTime result = LocalDateTime.of(datum, grenze.getEnde());
        if (result.isAfter(ende)) {
            return ersatz;
        }
        return result;
    }

    private LocalDateTime getZeitPunktAmGrenzenEnde(LocalDate datum, ZeitGrenze grenze) {
        return getZeitPunktAmGrenzenEndeOderDatum(datum, grenze, SystemKalender.MAX_DATETIME);
    }

    private LocalDateTime getZeitPunktAmVorigenGrenzenEnde(LocalDate datum, ZeitGrenze grenze) {

        List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
        int grenzIndex = zeitGrenzen.indexOf(grenze);
        LocalDateTime result = null;

        if (grenzIndex > 0) {
            result = LocalDateTime.of(datum, zeitGrenzen.get(grenzIndex - 1).getEnde());
        } else {
            result = LocalDateTime.of(datum.minusDays(1), zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
        }
        if (result.isBefore(start)) {
            result = SystemKalender.MIN_DATETIME;
        }

        return result;
    }

    @Override
    public boolean benutzt(KalenderEintrag referenz) {
        return false;
    }

    @Override
    public Set<KalenderEintragMitOffset> getAufgeloesteVerweise() {
        return Collections.singleton(new KalenderEintragMitOffset(this));
    }
}

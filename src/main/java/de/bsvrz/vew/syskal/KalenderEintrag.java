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

package de.bsvrz.vew.syskal;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.Fehler.FehlerType;
import de.bsvrz.vew.syskal.internal.DatumsEintrag;
import de.bsvrz.vew.syskal.internal.EintragsArt;
import de.bsvrz.vew.syskal.internal.EintragsVerwaltung;
import de.bsvrz.vew.syskal.internal.KalenderEintragMitOffset;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.OderVerknuepfung;
import de.bsvrz.vew.syskal.internal.UndVerknuepfung;
import de.bsvrz.vew.syskal.internal.VerweisEintrag;
import de.bsvrz.vew.syskal.internal.VorDefinierterEintrag;
import de.bsvrz.vew.syskal.internal.ZeitBereichsEintrag;
import de.bsvrz.vew.syskal.internal.ZeitGrenze;

/**
 * Logische Repräsentation eines Kalendereintrags, dessen Inhalt durch einen
 * Definitionsstring innerhalb des Datenverteiler-Modells definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
// @SuppressWarnings("javadoc")
public abstract class KalenderEintrag {

    private static final Debug LOGGER = Debug.getLogger();

    /** das Pattern eines Datumsbereiches im Definitionsstring. */
    private static final Pattern DATUMSBEREICH_PATTERN = Pattern.compile("<.*>");

    /** das Pattern eines Zeitgrenzenbereiches im Definitionsstring. */
    private static final Pattern ZEITBEREICHSLISTE_PATTERN = Pattern.compile("\\(.*\\)");

    /** das Pattern eines Zeitbereiches oder einer Verknüpfungsliste. */
    protected static final Pattern ZEITBEREICH_PATTERN = Pattern.compile("\\{.[^\\{]*\\}");

    private static String entferneNamensPrefix(final String name, final String definition) {

        final String[] parts = definition.split(":=");

        if (parts.length < 2) {
            return parts[0].trim();
        }

        final String defName = parts[0].trim();
        if (!defName.equals(name)) {
            LOGGER.warning("Für den Systemkalendereintrag " + name + " ist der abweichende Name: \""
                    + defName + "\" definiert!");
        }
        return parts[1].trim();
    }

    private static String ermittleZeitBereiche(String source, List<ZeitGrenze> parsedZeitBereiche)
            throws ParseException {

        String definition = source;

        Matcher mat = KalenderEintrag.ZEITBEREICHSLISTE_PATTERN.matcher(definition);
        while (mat.find()) {
            final String bereich = mat.group();
            definition = definition.replace(bereich, "");
            final String zeitBereich = bereich.substring(1, bereich.length() - 1);
            final Matcher zeitMat = KalenderEintrag.ZEITBEREICH_PATTERN.matcher(zeitBereich);
            while (zeitMat.find()) {
                String zb = zeitMat.group();
                zb = zb.substring(1, zb.length() - 1);
                parsedZeitBereiche.add(new ZeitGrenze(zb));
            }
        }

        return definition;
    }

    /**
     * zerlegt den übergebenen Definitionsstring und erzeugt einen
     * entsprechenden {@link KalenderEintrag}.
     * 
     * @param provider
     *            die Verwaltung aller bekannten Kalendereinträge zur
     *            Verifizierung von Referenzen
     * @param name
     *            der Name des Eintrags
     * @param definition
     *            der Definitionsstring
     * @return den aus der Definition ermittelten Kalendereintrag, dessen
     *         konkreter Typ vom Inhalt der Definition abhängt
     */
    public static KalenderEintrag parse(KalenderEintragProvider provider, final String name,
            final String definition) {

        KalenderEintrag result = null;

        result = VorDefinierterEintrag.getEintrag(name);
        if (result != null) {
            return result;
        }

        String rest = entferneNamensPrefix(name, definition);
        String benutzteDefinition = rest;

        boolean zeitBereichsfehler = false;
        final List<ZeitGrenze> parsedZeitBereiche = new ArrayList<>();
        try {
            rest = ermittleZeitBereiche(rest, parsedZeitBereiche);
        } catch (ParseException e) {
            LOGGER.warning(
                    "Fehler beim Einlesen der Zeitbereiche des Eintrags '" + name + "': " + e.getLocalizedMessage());
            zeitBereichsfehler = true;
        }

        if (rest.toLowerCase(Locale.getDefault()).startsWith("und")) {
            result = new UndVerknuepfung(provider, name, rest.substring("und".length()));
        } else if (rest.toLowerCase(Locale.getDefault()).startsWith("oder")) {
            result = new OderVerknuepfung(provider, name, rest.substring("oder".length()));
        } else {
            Matcher mat = KalenderEintrag.DATUMSBEREICH_PATTERN.matcher(rest);
            if (mat.find()) {
                result = new ZeitBereichsEintrag(name, mat.group().substring(1, mat.group().length() - 1));
            } else {
                if (rest.trim().length() == 0) {
                    result = new ZeitBereichsEintrag(name, "");
                } else {
                    if (rest.contains(",")) {
                        result = new DatumsEintrag(name, rest);
                    } else {
                        result = new VerweisEintrag(provider, name, rest);
                    }
                }
            }

        }

        if (zeitBereichsfehler) {
            result.addFehler(Fehler.common("Zeitbereich konnte nicht geparst werden"));
        } else {
            result.komprimiereZeitBereiche(parsedZeitBereiche.stream().sorted().collect(Collectors.toList()));
        }

        result.definition = benutzteDefinition;

        if (result.benutzt(result)) {
            result.addFehler(Fehler.common("Rekursive Verwendung"));
        }

        return result;
    }

    /* der Definitionsstring des Eintrags. */
    private String definition;

    /* der Name des Eintrags. */
    private String name;

    /**
     * die Zeitgrenzen, die den Kalendereintrag zeitlich einschränken können.
     */
    private final List<ZeitGrenze> zeitGrenzen = new ArrayList<>();

    /** der Definitionseintrag konnte nicht korrekt eingelesen werden. */
    private List<Fehler> fehler = new ArrayList<>();

    /**
     * Basiskonstruktor für einen Kalendereintrag.
     * 
     * @param name
     *            Name des Kalendereintrags
     * @param definition
     *            Definitionsstring des Eintrags
     */
    protected KalenderEintrag(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    /**
     * fügt eine Fehlermeldung für den Eintrag hinzu und macht den Eintrag damit
     * ungültig.
     * 
     * @param message
     *            die Fehlermeldung
     */
    protected void addFehler(Fehler message) {
        fehler.add(message);
    }

    /**
     * fügt eine Zeitgrenze hinzu.
     * 
     * @param grenze
     *            die neue Zeitgrenze
     */
    protected void addZeitGrenze(final ZeitGrenze grenze) {
        zeitGrenzen.add(grenze);
    }

    /**
     * die Funktion ermittelt, ob der Kalendereintrag den übergebenen Eintrag
     * benutzt, womit sich die Gültigkeit des Eintrags ändern könnte, wenn sich
     * der geprüfte EIntrag geändert hat.
     * 
     * @param referenz
     *            der potentiell verwendete Eintrag
     * @return true, wenn der übergebene Eintrag zur Definition verwendet wird
     */
    protected abstract boolean benutzt(KalenderEintrag referenz);

    /**
     * berechnet die zeitliche Gültigkeit des Eintrags zum übergebenen
     * Zeitpunkt.
     * 
     * Das Ergebnis enthält zusätzlich den Zeitpunkt, zu dem der aktuelle
     * Zustand eingetreten ist und den Zeitpunkt und Zustand des nächsten zu
     * erwartenden Wechsels.
     * 
     * Die Funktion wird intern verwendet, ein Client sollte die Funktion
     * {@link #getZeitlicheGueltigkeit(LocalDateTime)} aufrufen!
     * 
     * @param zeitpunkt
     *            der Zeitpunkt für den für Gültigkeit berechnet werden soll
     * @return die berechnete Gültigkeit
     */
    public abstract SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt);

    /**
     * berechnet die zeitliche Gültigkeit des Eintrags vor dem übergebenen
     * Zeitpunkt.
     * 
     * Das Ergebnis enthält zusätzlich den Zeitpunkt, zu dem der aktuelle
     * Zustand eingetreten ist und den Zeitpunkt und Zustand des vorigen
     * Wechsels.
     * 
     * Die Funktion wird intern verwendet, ein Client sollte die Funktion
     * {@link #getZeitlicheGueltigkeitVor(LocalDateTime)} aufrufen!
     * 
     * @param zeitpunkt
     *            der Zeitpunkt für den für Gültigkeit berechnet werden soll
     * @return die berechnete Gültigkeit
     */
    public abstract SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVor(LocalDateTime zeitpunkt);

    /**
     * die Funktion bestimmt, ob der Eintrag zum aktuellen Zeitpunkt gültig ist.
     * Daebi wird nur die reine zeitliche Gültigkeit geprüft und keine
     * Zustandswechsel berechnet.
     * 
     * Die Funktion wird intern verwendet, ein Client sollte die Funktion
     * {@link #isGueltig(LocalDateTime)} aufrufen!
     * 
     * @param zeitPunkt
     *            der Zeitpunkt für den die Gültigkeit geprüft werden soll
     * @return true, wenn der Eintrag zum übergebenen Zeitpunkt gültig ist
     */
    protected abstract boolean bestimmeGueltigkeit(LocalDateTime zeitPunkt);

    /**
     * ermittelt die Kalendereinträge, die für die Berechnung eines komplexen
     * Eintrags verwendet werden inklusive der Offsets für die entsprechenden
     * Verweise.
     * 
     * @return die Menge der Verweise, die den Eintrag bildet
     */
    public abstract Set<KalenderEintragMitOffset> getAufgeloesteVerweise();

    /**
     * liefert die Zeichenkette mit der initialen Definitionszeichenkette des
     * Eintrags.
     * 
     * @return die Definition als Zeichenkette
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * liefert die Art des Dateneintrags.
     * 
     * @return die Art
     */
    public abstract EintragsArt getEintragsArt();

    /**
     * liefert die Liste der Fehler, die beim Parsen des Definitionsstrings
     * eines Eintrags ermittelt wurden.
     * 
     * @return die Liste der Fehler als Textstrings
     */
    public Collection<String> getFehler() {
        List<String> result = new ArrayList<>();
        for( Fehler item : fehler) {
            result.add(item.getMessage());
        }
        return result;
    }

    /**
     * liefert die Liste der Intervalle, in denen der Kalendereintrag innerhalb
     * der übergebenen Zeitspanne gültig ist. Das erste und letzte Intervall
     * wird gegebenenfalls auf den Abfragebereich beschnitten.
     * 
     * @param startTime
     *            der Beginn des Abfragebereiches
     * @param endTime
     *            das Ende des Abfragebereiches
     * @return die Liste der Intervalle, in denen der Eintrag gültig ist
     */
    public List<Intervall> getIntervalle(LocalDateTime startTime, LocalDateTime endTime) {

        if (hasFehler()) {
            return Collections.emptyList();
        }

        List<Intervall> result = new ArrayList<>();

        SystemkalenderGueltigkeit gueltigkeit = getZeitlicheGueltigkeit(startTime);
        LocalDateTime aktuellerZeitPunkt = null;

        do {
            if (gueltigkeit.isZeitlichGueltig()) {
                LocalDateTime start = gueltigkeit.getErsterWechsel().getZeitPunkt();
                if (start.isBefore(startTime)) {
                    start = startTime;
                }
                LocalDateTime ende = gueltigkeit.getNaechsterWechsel().getZeitPunkt();
                if (ende.isAfter(endTime)) {
                    ende = endTime;
                }
                result.add(Intervall.of(start, ende));
            }

            ZustandsWechsel wechsel = gueltigkeit.getNaechsterWechsel();
            aktuellerZeitPunkt = wechsel.getZeitPunkt();
            gueltigkeit = getZeitlicheGueltigkeit(aktuellerZeitPunkt);

        } while (!aktuellerZeitPunkt.isAfter(endTime));

        return result;
    }

    /**
     * liefert den Name des Eintrags.
     * 
     * @return den Name
     */
    public String getName() {
        return name;
    }

    /**
     * liefert die Liste der für den Eintrag definierten Zeitgrenzen.
     * 
     * @return die Liste der definierten Grenzen
     */
    public List<ZeitGrenze> getZeitGrenzen() {
        return zeitGrenzen;
    }

    /**
     * ermittelt die zeitliche Gültigkeit des Eintrags zum übergebenen
     * Zeitpunkt.
     * 
     * Das Ergebnis enthält zusätzlich den Zeitpunkt, zu dem der aktuelle
     * Zustand eingetreten ist und den Zeitpunkt und Zustand des nächsten zu
     * erwartenden Wechsels.
     * 
     * @param zeitpunkt
     *            der Zeitpunkt für den für Gültigkeit ermittelt werden soll
     * @return die berechnete Gültigkeit
     */
    public final SystemkalenderGueltigkeit getZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {
        if (hasFehler()) {
            return SystemkalenderGueltigkeit.NICHT_GUELTIG;
        }

        return berechneZeitlicheGueltigkeit(zeitpunkt);
    }

    /**
     * ermittelt die zeitliche Gültigkeit des Eintrags vor dem übergebenen
     * Zeitpunkt.
     * 
     * Das Ergebnis enthält zusätzlich den Zeitpunkt, zu dem der aktuelle
     * Zustand eingetreten ist und den Zeitpunkt und Zustand des vorigen
     * Wechsels.
     * 
     * 
     * @param zeitPunkt
     *            der Zeitpunkt für den für Gültigkeit berechnet werden soll
     * @return die berechnete Gültigkeit
     */
    public SystemkalenderGueltigkeit getZeitlicheGueltigkeitVor(LocalDateTime zeitPunkt) {
        if (hasFehler()) {
            return SystemkalenderGueltigkeit.NICHT_GUELTIG;
        }

        return berechneZeitlicheGueltigkeitVor(zeitPunkt);
    }

    /**
     * ermittelt die Liste der Zustandswechsel im übergebenen Zeitraum.
     * 
     * Wenn der Kalendereintrag zum Startzeitpunkt der Abfrage gültig ist, wird
     * als erstes der Zustandswechsel geliefert, zu dem der Eintrag ursprünglich
     * gültig wurde.
     * 
     * @param start
     *            der Anfangszeitpunkt des Abfragezeitraums
     * @param ende
     *            der Endzeitpunkt des Abfragezeitraums
     * @return die Liste der Zustandswechsel
     */
    public final List<ZustandsWechsel> getZustandsWechsel(LocalDateTime start, LocalDateTime ende) {

        if (hasFehler()) {
            return Collections.singletonList(ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME));
        }

        List<ZustandsWechsel> result = new ArrayList<>();

        SystemkalenderGueltigkeit gueltigkeit = getZeitlicheGueltigkeit(start);
        result.add(ZustandsWechsel.of(gueltigkeit.getErsterWechsel().getZeitPunkt(), gueltigkeit.isZeitlichGueltig()));

        LocalDateTime aktuellerZeitPunkt = null;
        do {
            ZustandsWechsel wechsel = gueltigkeit.getNaechsterWechsel();
            aktuellerZeitPunkt = wechsel.getZeitPunkt();

            if (!aktuellerZeitPunkt.isAfter(ende)) {
                result.add(wechsel);
                gueltigkeit = getZeitlicheGueltigkeit(wechsel.getZeitPunkt());
            }

        } while (!aktuellerZeitPunkt.isAfter(ende));

        return result;
    }

    /**
     * ermittelt, ob der Eintrag fehlerhaft eingelesen wurde.
     * 
     * @return true, wenn der Definitionseintrag nicht korrekt interpretiert
     *         werden konnte
     */
    public boolean hasFehler() {
        return !fehler.isEmpty();
    }

    /**
     * die Funktion bestimmt, ob der Eintrag zum aktuellen Zeitpunkt gültig ist.
     * Dabei wird nur die reine zeitliche Gültigkeit geprüft und keine
     * Zustandswechsel berechnet.
     * 
     * @param zeitPunkt
     *            der Zeitpunkt für den die Gültigkeit geprüft werden soll
     * @return true, wenn der Eintrag zum übergebenen Zeitpunkt gültig ist
     */
    public final boolean isGueltig(LocalDateTime zeitPunkt) {
        if (hasFehler()) {
            return false;
        }

        return bestimmeGueltigkeit(zeitPunkt);
    }

    private void komprimiereZeitBereiche(List<ZeitGrenze> grenzen) {

        ZeitGrenze aktuell = null;

        for (ZeitGrenze grenze : grenzen) {
            if (aktuell == null) {
                aktuell = grenze;
                continue;
            }

            if (grenze.getStart().isAfter(aktuell.getEnde())) {
                zeitGrenzen.add(aktuell);
                aktuell = grenze;
            } else {
                if (!grenze.getEnde().isBefore(aktuell.getEnde())) {
                    aktuell = new ZeitGrenze(aktuell.getStart(), grenze.getEnde());
                }
            }
        }

        if (aktuell != null) {
            zeitGrenzen.add(aktuell);
        }
    }

    /**
     * setzt den Definitionstext des Eintrags.
     * 
     * @param definition
     *            der Text
     */
    protected void setDefinition(String definition) {
        this.definition = definition;
    }

    protected void clearFehler(FehlerType type) {
        Iterator<Fehler> iterator = fehler.iterator();
        while (iterator.hasNext()) {
            Fehler next = iterator.next();
            if( next.getType() == type) {
                iterator.remove();
            }
        }
    }
    
    /**
     * Berechnet die Gültigkeit eines Kalendereintrags neu, wenn potentiell neue
     * oder andere Referenzeinträge zur Verfügung stehen.
     * 
     * @param provider
     *            der Provider mit den zur Verfügung stehenden Einträgen
     * @return true, wenn die Gültigkeit geändert wurde
     */
    public abstract boolean recalculateVerweise(KalenderEintragProvider provider);
}

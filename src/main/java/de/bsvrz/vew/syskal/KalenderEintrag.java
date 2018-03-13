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
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.internal.DatumsEintrag;
import de.bsvrz.vew.syskal.internal.EintragsArt;
import de.bsvrz.vew.syskal.internal.KalenderEintragMitOffset;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.OderVerknuepfung;
import de.bsvrz.vew.syskal.internal.UndVerknuepfung;
import de.bsvrz.vew.syskal.internal.VerweisEintrag;
import de.bsvrz.vew.syskal.internal.VorDefinierterEintrag;
import de.bsvrz.vew.syskal.internal.ZeitBereichsEintrag;
import de.bsvrz.vew.syskal.internal.ZeitGrenze;

public abstract class KalenderEintrag {

    private static final Debug LOGGER = Debug.getLogger();

    /** das Pattern eines Datumsbereiches im Definitionsstring. */
    private static final Pattern DATUMSBEREICH_PATTERN = Pattern.compile("<.*>");

    /** das Pattern eines Zeitgrenzenbereiches im Definitionsstring. */
    private static final Pattern ZEITBEREICHSLISTE_PATTERN = Pattern.compile("\\(.*\\)");

    /** das Pattern eines Zeitbereiches oder einer Verknüpfungsliste. */
    protected static final Pattern ZEITBEREICH_PATTERN = Pattern.compile("\\{.[^\\{]*\\}");

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

        boolean zeitBereichsfehler = false;
        final List<ZeitGrenze> parsedZeitBereiche = new ArrayList<>();
        try {
            rest = ermittleZeitBereiche(rest, parsedZeitBereiche);
        } catch (ParseException e) {
            LOGGER.warning("Fehler beim Einlesen der Zeitbereiche: " + e.getLocalizedMessage());
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
            result.addFehler("Zeitbereich konnte nicht geparst werden");
        } else {
            result.komprimiereZeitBereiche(parsedZeitBereiche.stream().sorted().collect(Collectors.toList()));
        }

        result.definition = definition;

        if (result.benutzt(result)) {
            result.addFehler("Rekursive Verwendung");
        }

        return result;
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

    private String definition;

    private String name;

    /**
     * die Zeitgrenzen, die den Kalendereintrag zeitlich einschränken können.
     */
    private final List<ZeitGrenze> zeitGrenzen = new ArrayList<>();

    /** der Definitionseintrag konnte nicht korrekt eingelesen werden. */
    private List<String> fehler = new ArrayList<>();

    protected KalenderEintrag(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }

    /**
     * fügt eine Zeitgrenze hinzu.
     * 
     * @param grenze
     *            die neue Zeitgrenze
     */
    public void addZeitGrenze(final ZeitGrenze grenze) {
        zeitGrenzen.add(grenze);
    }

    public abstract SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt);

    public abstract SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVor(LocalDateTime zeitpunkt);

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

    public boolean isGueltig(LocalDateTime zeitPunkt) {
        return getZeitlicheGueltigkeit(zeitPunkt).isZeitlichGueltig();
    }

    public final SystemkalenderGueltigkeit getZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {
        if (isFehler()) {
            return SystemkalenderGueltigkeit.NICHT_GUELTIG;
        }
        
        return berechneZeitlicheGueltigkeit(zeitpunkt);
    }

    public SystemkalenderGueltigkeit getZeitlicheGueltigkeitVor(LocalDateTime zeitPunkt) {
        if (isFehler()) {
            return SystemkalenderGueltigkeit.NICHT_GUELTIG;
        }

        return berechneZeitlicheGueltigkeitVor(zeitPunkt);
    }

    public final List<ZustandsWechsel> getZustandsWechsel(LocalDateTime start, LocalDateTime ende) {

        if (isFehler()) {
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

    public List<Intervall> getIntervalle(LocalDateTime startTime, LocalDateTime endTime) {

        if (isFehler()) {
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
     * ermittelt, ob der Eintrag fehlerhaft eingelesen wurde.
     * 
     * @return true, wenn der Definitionseintrag nicht korrekt interpretiert
     *         werden konnte
     */
    public boolean isFehler() {
        return !fehler.isEmpty();
    }

    /**
     * fügt eine Fehlermeldung für den Eintrag hinzu und macht den Eintrag damit
     * ungültig.
     * 
     * @param message
     *            die Fehlermeldung
     */
    protected void addFehler(String message) {
        fehler.add(message);
    }

    public Collection<String> getFehler() {
        return Collections.unmodifiableList(fehler);
    }

    public abstract boolean benutzt(KalenderEintrag referenz);

    public boolean isVerwendbar() {
        return !isFehler();
    }

    protected void setDefinition(String definition) {
        this.definition = definition;
    }
    
	public abstract Set<KalenderEintragMitOffset> getAufgeloesteVerweise();
}

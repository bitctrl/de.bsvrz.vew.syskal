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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentation der Daten eines {@link KalenderEintrag}, der durch die
 * logische Verknüpfung mehrerer anderer Einträge definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public abstract class LogischerVerkuepfungsEintrag extends KalenderEintrag {

    private static final Debug LOGGER = Debug.getLogger();

    /** die Liste der Verweise, die den Eintrag definieren. */
    private final List<VerweisEintrag> verweise = new ArrayList<>();

    /**
     * ein optionales Anfangsjahr, mit dem der Gültigkeitsbereich des Eintrags
     * eingeschränkt werden kann.
     */
    private int startJahr;

    /**
     * ein optionales Endjahr, mit dem der Gültigkeitsbereich des Eintrags
     * eingeschränkt werden kann.
     */
    private int endJahr;

    /**
     * Konstruktor.
     * 
     * @param name
     *            der Name des Eintrags
     * @param definition
     *            der definierende Textstring des Eintrags
     */
    LogischerVerkuepfungsEintrag(KalenderEintragProvider provider, final String name, final String definition) {
        super(name, definition);

        if (definition != null) {
            String rest = definition;

            final Matcher mat = KalenderEintrag.ZEITBEREICH_PATTERN.matcher(rest);
            while (mat.find()) {
                String elemente = mat.group();
                rest = rest.replace(elemente, "").trim();
                elemente = elemente.substring(1, elemente.length() - 1);
                final String[] verweisDefinitionen = elemente.split(",");
                for (final String def : verweisDefinitionen) {
                    try {
                        Verweis verweis = new Verweis(provider, def);
                        if (verweis.isUngueltig()) {
                            addFehler(verweis.getName() + " ist ungültig");
                        }
                        verweise.add(new VerweisEintrag(verweis));
                    } catch (final ParseException e) {
                        String message = "Fehler beim Parsen des Kalendereintrags: " + name + ": "
                                + e.getLocalizedMessage();
                        LOGGER.warning(message);
                        addFehler(message);
                    }
                }
            }

            if (verweise.size() <= 0) {
                addFehler("Verknüpfung enthält keine Verweise");
            }

            scanJahresBereich(name, rest);
        }
    }

    private void scanJahresBereich(final String name, String rest) {
        if (!rest.isEmpty()) {
            final String[] parts = rest.split(",");
            if (parts.length > 0) {
                if (!"*".equals(parts[0].trim())) {
                    try {
                        startJahr = Integer.parseInt(parts[0]);
                    } catch (final NumberFormatException e) {
                        LOGGER.warning(
                                "Fehler beim Parsen des Eintrags: " + name + ": " + e.getLocalizedMessage());
                        // Jahr wird als nicht gesetzt angenommen
                    }
                }
            }
            if (parts.length > 1) {
                if (!"*".equals(parts[1].trim())) {
                    try {
                        endJahr = Integer.parseInt(parts[1]);
                    } catch (final NumberFormatException e) {
                        LOGGER.warning(
                                "Fehler beim Parsen des Eintrags: " + name + ": " + e.getLocalizedMessage());
                        // Jahr wird als nicht gesetzt angenommen
                    }
                }
            }
        }
    }

    @Override
    public EintragsArt getEintragsArt() {
        return EintragsArt.VERKNUEPFT;
    }

    /**
     * liefert das optional beschränkende Endjahr. 0 steht für ein
     * unbeschränktes Ende.
     * 
     * @return das Jahr
     */
    public int getEndJahr() {
        return endJahr;
    }

    /**
     * liefert das optional beschränkende Anfangsjahr. 0 steht für einen
     * unbeschränkten Anfang.
     * 
     * @return das Jahr
     */
    public int getStartJahr() {
        return startJahr;
    }

    /**
     * liefert die Art der logischen Verknüpfung als Textstring.
     * 
     * @return den Text
     */
    public abstract String getVerknuepfungsArt();

    /**
     * liefert die Liste der Verweise, die den Eintrag definieren.
     * 
     * @return die Liste
     */
    public List<VerweisEintrag> getVerweise() {
        return verweise;
    }

    /**
     * definiert die Liste der Verweise, die der Eintrag verknüpft.
     * 
     * @param verweisListe
     *            die Liste der Verweise
     */
    protected void setVerweise(List<Verweis> verweisListe) {
        verweise.clear();
        for (Verweis verweis : verweisListe) {
            verweise.add(new VerweisEintrag(verweis));
        }
    }

    /**
     * setzt das optionale Endjahr des Eintrags.
     * 
     * @param endJahr
     *            das Jahr
     */
    public void setEndJahr(final int endJahr) {
        this.endJahr = endJahr;
    }

    /**
     * setzt das optionale Anfangsjahr des Eintrags.
     * 
     * @param startJahr
     *            das Jahr
     */
    public void setStartJahr(final int startJahr) {
        this.startJahr = startJahr;
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(getName());
        buffer.append(":=");
        buffer.append(getVerknuepfungsArt());
        buffer.append('{');
        int idx = 0;
        for (final VerweisEintrag verweis : verweise) {
            if (idx > 0) {
                buffer.append(',');
            }
            buffer.append(verweis.getVerweis());
            idx++;
        }
        buffer.append('}');

        if (startJahr > 0) {
            buffer.append(startJahr);
        } else {
            buffer.append('*');
        }
        buffer.append(',');
        if (endJahr > 0) {
            buffer.append(endJahr);
        } else {
            buffer.append('*');
        }

        return buffer.toString();
    }

    @Override
    public boolean benutzt(KalenderEintrag referenz) {
        for (VerweisEintrag verweis : verweise) {
            if (verweis.getVerweis().getName().equals(referenz.getName())) {
                return true;
            }
        }
        return false;
    }

    boolean isErlaubteWechselZeit(LocalDateTime wechselZeit) {
        if (startJahr > 0 && wechselZeit.getYear() < startJahr) {
            return false;
        }
        if (endJahr > 0 && wechselZeit.getYear() > endJahr) {
            return false;
        }
        return true;
    }

    @Override
    public Set<KalenderEintragMitOffset> getAufgeloesteVerweise() {
        Set<KalenderEintragMitOffset> result = new LinkedHashSet<>();
        for (VerweisEintrag verweisEintrag : getVerweise()) {
            result.addAll(verweisEintrag.getAufgeloesteVerweise());
        }
        return result;
    }

    /**
     * prüft ob der Eintrag zum übergebenen Zeitpunkt die gewüschte Gültogkeit
     * hat.
     * 
     * @param wechselZeit
     *            der Zeitpunkt für den die Prüfung erfolgen soll
     * @param zielZustand
     *            der gewünschte Gültigkeitszustand
     * @return tre, wenn der gewünschte Zustand besteht
     */
    protected final boolean pruefeGueltigKeit(LocalDateTime wechselZeit, boolean zielZustand) {
        return zielZustand == isGueltig(wechselZeit);
    }

    /**
     * berechnet, dem nächsten Gültigkeitswechsel des Eintrags auf den
     * angegebenen Zielzustand ausgehend von der Liste der übergebenen
     * Wechselkandidaten.
     * 
     * @param zielZustand
     *            der gewünschte Zustand
     * @param potentielleEndWechsel
     *            die Liste der Zustandswechselkandidaten
     * @return den ermittelten Zustandswechsel
     */
    protected final ZustandsWechsel berechneNaechstenWechselAuf(boolean zielZustand,
            Map<KalenderEintragMitOffset, ZustandsWechsel> potentielleEndWechsel) {

        LocalDateTime wechselZeit = null;
        Map<KalenderEintragMitOffset, ZustandsWechsel> verweisWechsel = new LinkedHashMap<>(potentielleEndWechsel);

        do {
            ZustandsWechsel wechsel = verweisWechsel.values().stream().min(ZustandsWechsel.ZEIT_COMPARATOR).get();
            if (wechsel == null) {
                return ZustandsWechsel.of(SystemKalender.MAX_DATETIME, !zielZustand);
            }

            wechselZeit = wechsel.getZeitPunkt();
            if (isErlaubteWechselZeit(wechselZeit)) {
                if (pruefeGueltigKeit(wechselZeit, zielZustand)) {
                    return ZustandsWechsel.of(wechselZeit, zielZustand);
                }
            }

            for (Entry<KalenderEintragMitOffset, ZustandsWechsel> entry : verweisWechsel.entrySet()) {
                if (!entry.getValue().getZeitPunkt().isAfter(wechselZeit)) {
                    entry.setValue(entry.getKey().berechneZeitlicheGueltigkeit(entry.getValue().getZeitPunkt())
                            .getNaechsterWechsel());
                }
            }

        } while (wechselZeit.isBefore(SystemKalender.MAX_DATETIME)
                && (getEndJahr() == 0 || getEndJahr() >= wechselZeit.getYear()));

        return ZustandsWechsel.of(SystemKalender.MAX_DATETIME, !zielZustand);
    }

    /**
     * berechnet, dem vorigen Gültigkeitswechsel des Eintrags der auf den
     * angegebenen Zielzustand ausgehend von der Liste der übergebenen
     * Wechselkandidaten.
     * 
     * @param zielZustand
     *            der gewünschte Zustand
     * @param potentielleStartWechsel
     *            die Liste der Zustandswechselkandidaten
     * @return den ermittelten Zustandswechsel
     */
    protected final ZustandsWechsel berechneVorigenWechselAuf(boolean zielZustand,
            Map<KalenderEintragMitOffset, ZustandsWechsel> potentielleStartWechsel) {
        LocalDateTime wechselZeit = null;
        Map<KalenderEintragMitOffset, ZustandsWechsel> verweisWechsel = new LinkedHashMap<>(potentielleStartWechsel);
        ZustandsWechsel potentiellerWechsel = null;

        do {
            ZustandsWechsel wechsel = verweisWechsel.values().stream().max(ZustandsWechsel.ZEIT_COMPARATOR).get();
            if (wechsel == null) {
                return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, zielZustand);
            }

            wechselZeit = wechsel.getZeitPunkt();
            if (isErlaubteWechselZeit(wechselZeit)) {
                if (pruefeGueltigKeit(wechselZeit, zielZustand)) {
                    potentiellerWechsel = ZustandsWechsel.of(wechselZeit, zielZustand);
                } else if (potentiellerWechsel != null) {
                    return potentiellerWechsel;
                }
            }

            for (Entry<KalenderEintragMitOffset, ZustandsWechsel> entry : verweisWechsel.entrySet()) {
                if (!entry.getValue().getZeitPunkt().isBefore(wechselZeit)) {
                    entry.setValue(entry.getKey().berechneZeitlicheGueltigkeitVor(entry.getValue().getZeitPunkt())
                            .getErsterWechsel());
                }
            }

        } while (wechselZeit.isAfter(SystemKalender.MIN_DATETIME)
                && (getStartJahr() == 0 || getStartJahr() <= wechselZeit.getYear()));

        return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, zielZustand);
    }

    @Override
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitPunkt) {

        boolean zustand = isGueltig(zeitPunkt);

        Map<KalenderEintragMitOffset, ZustandsWechsel> potentielleEndWechsel = new LinkedHashMap<>();
        Map<KalenderEintragMitOffset, ZustandsWechsel> potentielleStartWechsel = new LinkedHashMap<>();

        for (KalenderEintragMitOffset eintrag : getAufgeloesteVerweise()) {
            SystemkalenderGueltigkeit gueltigKeit = eintrag.berechneZeitlicheGueltigkeit(zeitPunkt);
            potentielleStartWechsel.put(eintrag, gueltigKeit.getErsterWechsel());
            potentielleEndWechsel.put(eintrag, gueltigKeit.getNaechsterWechsel());

        }

        ZustandsWechsel beginn = berechneVorigenWechselAuf(zustand, potentielleStartWechsel);
        ZustandsWechsel wechsel = berechneNaechstenWechselAuf(!zustand, potentielleEndWechsel);

        return SystemkalenderGueltigkeit.of(beginn, wechsel);
    }

    /**
     * ermittelt den initialen Berechnungszustand für die jeweilige logische
     * Verknüpfungsart, von dem bei der Bestimmung der Gültigkeit des Eintrags
     * ausgegangen wird.
     * 
     * @return den initalen Zustand
     */
    protected abstract boolean getInitialenBerechnungsZustand();

    @Override
    public final SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVor(LocalDateTime zeitPunkt) {

        SystemkalenderGueltigkeit zeitlicheGueltigkeit = berechneZeitlicheGueltigkeit(zeitPunkt);
        if (!zeitlicheGueltigkeit.getErsterWechsel().getZeitPunkt().isAfter(SystemKalender.MIN_DATETIME)) {
            return SystemkalenderGueltigkeit.of(zeitlicheGueltigkeit.getErsterWechsel(),
                    zeitlicheGueltigkeit.getErsterWechsel());
        }

        boolean zustand = zeitlicheGueltigkeit.isZeitlichGueltig();
        Map<KalenderEintragMitOffset, ZustandsWechsel> potentielleStartWechsel = new LinkedHashMap<>();

        for (VerweisEintrag verweis : getVerweise()) {
            if (verweis.hasFehler()) {
                return SystemkalenderGueltigkeit.NICHT_GUELTIG;
            }
        }

        for (KalenderEintragMitOffset eintrag : getAufgeloesteVerweise()) {
            SystemkalenderGueltigkeit gueltigKeit = eintrag
                    .berechneZeitlicheGueltigkeitVor(zeitlicheGueltigkeit.getErsterWechsel().getZeitPunkt());
            potentielleStartWechsel.put(eintrag, gueltigKeit.getNaechsterWechsel());
        }

        ZustandsWechsel beginn = berechneVorigenWechselAuf(!zustand, potentielleStartWechsel);

        return SystemkalenderGueltigkeit.of(beginn, zeitlicheGueltigkeit.getErsterWechsel());
    }

    @Override
    public boolean bestimmeGueltigkeit(LocalDateTime zeitPunkt) {

        boolean zustand = (getStartJahr() == 0 || getStartJahr() <= zeitPunkt.getYear())
                && (getEndJahr() == 0 || getEndJahr() >= zeitPunkt.getYear());
        if (!zustand) {
            return false;
        }

        boolean initialerZustand = getInitialenBerechnungsZustand();
        for (VerweisEintrag verweis : getVerweise()) {
            if (verweis.isGueltig(zeitPunkt) != initialerZustand) {
                return !initialerZustand;
            }
        }
        return initialerZustand;
    }

}

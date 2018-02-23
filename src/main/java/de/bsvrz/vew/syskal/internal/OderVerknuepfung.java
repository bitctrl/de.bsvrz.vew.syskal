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

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentiert die Daten eines Eintrags, der mehrere andere
 * Systemkalendereinträge logisch per ODER verknüpft.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class OderVerknuepfung extends LogischerVerkuepfungsEintrag {

    /**
     * Konstruktor.
     * 
     * @param provider
     *            die Verwaltung aller bekannten Systemkalendereinträge zur
     *            Verifizierung von Referenzen
     * @param name
     *            der Name des Eintrags
     * @param definition
     *            der definierende Text des Eintrags
     */
    public OderVerknuepfung(KalenderEintragProvider provider, final String name, final String definition) {
        super(provider, name, definition);
    }

    @Override
    public String getVerknuepfungsArt() {
        return "ODER";
    }

    @Override
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {

        boolean zustand = false;
        Map<KalenderEintrag, ZustandsWechsel> potentielleStartWechsel = new LinkedHashMap<>();
        Map<KalenderEintrag, ZustandsWechsel> potentielleEndWechsel = new LinkedHashMap<>();

        for (VerweisEintrag verweis : getVerweise()) {

            SystemkalenderGueltigkeit gueltigKeit = verweis.getZeitlicheGueltigkeit(zeitpunkt);
            if (gueltigKeit.isZeitlichGueltig()) {
                zustand = true;
            }
            potentielleStartWechsel.put(verweis, gueltigKeit.getErsterWechsel());
            potentielleEndWechsel.put(verweis, gueltigKeit.getNaechsterWechsel());
        }

        ZustandsWechsel beginn = berechneVorigenWechselAuf(zustand, potentielleStartWechsel);
        ZustandsWechsel wechsel = berechneNaechstenWechselAuf(!zustand, potentielleEndWechsel);

        return SystemkalenderGueltigkeit.of(beginn, wechsel);
    }

    @Override
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitpunkt) {

        SystemkalenderGueltigkeit zeitlicheGueltigkeit = berechneZeitlicheGueltigkeit(zeitpunkt);

        boolean zustand = zeitlicheGueltigkeit.isZeitlichGueltig();
        Map<KalenderEintrag, ZustandsWechsel> potentielleStartWechsel = new LinkedHashMap<>();

        for (VerweisEintrag verweis : getVerweise()) {
            if (verweis.isFehler()) {
                return SystemkalenderGueltigkeit.NICHT_GUELTIG;
            }

            SystemkalenderGueltigkeit gueltigKeit = verweis
                    .getZeitlicheGueltigkeitVor(zeitlicheGueltigkeit.getErsterWechsel().getZeitPunkt());
            potentielleStartWechsel.put(verweis, gueltigKeit.getNaechsterWechsel());
        }

        ZustandsWechsel beginn = berechneVorigenWechselAuf(!zustand, potentielleStartWechsel);

        return SystemkalenderGueltigkeit.of(beginn, zeitlicheGueltigkeit.getErsterWechsel());
    }

    private ZustandsWechsel berechneNaechstenWechselAuf(boolean zielZustand,
            Map<KalenderEintrag, ZustandsWechsel> potentielleWechsel) {

        LocalDateTime wechselZeit = null;
        Map<KalenderEintrag, ZustandsWechsel> verweisWechsel = new LinkedHashMap<>(potentielleWechsel);

        do {
            ZustandsWechsel wechsel = verweisWechsel.values().stream().min(ZustandsWechsel.ZEIT_COMPARATOR).get();
            if (wechsel == null) {
                return ZustandsWechsel.of(SystemKalender.MAX_DATETIME, !zielZustand);
            }

            wechselZeit = wechsel.getZeitPunkt();
            if (pruefeGueltigKeit(wechselZeit, zielZustand)) {
                return ZustandsWechsel.of(wechselZeit, zielZustand);
            }

            for (Entry<KalenderEintrag, ZustandsWechsel> entry : verweisWechsel.entrySet()) {
                if (!entry.getValue().getZeitPunkt().isAfter(wechselZeit)) {
                    entry.setValue(entry.getKey().berechneZeitlicheGueltigkeit(entry.getValue().getZeitPunkt())
                            .getNaechsterWechsel());
                }
            }

        } while (wechselZeit.isBefore(SystemKalender.MAX_DATETIME));

        return ZustandsWechsel.of(SystemKalender.MAX_DATETIME, !zielZustand);
    }

    private ZustandsWechsel berechneVorigenWechselAuf(boolean zielZustand,
            Map<KalenderEintrag, ZustandsWechsel> potentielleWechsel) {
        LocalDateTime wechselZeit = null;
        Map<KalenderEintrag, ZustandsWechsel> verweisWechsel = new LinkedHashMap<>(potentielleWechsel);
        ZustandsWechsel potentiellerWechsel = null;

        do {
            ZustandsWechsel wechsel = verweisWechsel.values().stream().max(ZustandsWechsel.ZEIT_COMPARATOR).get();
            if (wechsel == null) {
                return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, zielZustand);
            }

            wechselZeit = wechsel.getZeitPunkt();
            if (pruefeGueltigKeit(wechselZeit, zielZustand)) {
                if (!zielZustand) {
                    return ZustandsWechsel.of(wechselZeit, zielZustand);
                }
                potentiellerWechsel = ZustandsWechsel.of(wechselZeit, zielZustand);
            } else {
                if (zielZustand && potentiellerWechsel != null) {
                    return potentiellerWechsel;
                }
            }

            for (Entry<KalenderEintrag, ZustandsWechsel> entry : verweisWechsel.entrySet()) {
                if (!entry.getValue().getZeitPunkt().isBefore(wechselZeit)) {
                    entry.setValue(entry.getKey().berechneZeitlicheGueltigkeitsVor(entry.getValue().getZeitPunkt())
                            .getErsterWechsel());
                }
            }

        } while (wechselZeit.isAfter(SystemKalender.MIN_DATETIME));

        return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, zielZustand);
    }

    private boolean pruefeGueltigKeit(LocalDateTime wechselZeit, boolean zielZustand) {

        int trueCounter = 0;
        for (VerweisEintrag verweis : getVerweise()) {
            if (verweis.berechneZeitlicheGueltigkeit(wechselZeit).isZeitlichGueltig()) {
                trueCounter++;
            }
        }

        if (zielZustand) {
            return trueCounter > 0;
        }
        return trueCounter == 0;
    }
}

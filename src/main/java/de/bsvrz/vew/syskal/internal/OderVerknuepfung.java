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

import java.util.List;

/**
 * Repräsentiert die Daten eines Eintrags, der mehrere andere
 * Systemkalendereinträge logisch per ODER verknüpft.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class OderVerknuepfung extends LogischerVerkuepfungsEintrag {

    /**
     * erzeugt eine neue {@link OderVerknuepfung} mit dem angebenen Name
     * innerhalb der übergebenen Eintragsverwaltung.
     * 
     * @param provider
     *            die Eintragsverwaltung zur Verifizierung der Verweiseinträge
     * @param name
     *            der Name des neuen Eintrags
     * @return die neue Instanz
     */
    public static OderVerknuepfung of(KalenderEintragProvider provider, String name) {
        return new OderVerknuepfung(provider, name);
    }

    /**
     * erzeugt eine neue {@link OderVerknuepfung} mit den angebenen Daten
     * innerhalb der übergebenen Eintragsverwaltung.
     * 
     * @param provider
     *            die Eintragsverwaltung zur Verifizierung der Verweiseinträge
     * @param name
     *            der Name des neuen Eintrags
     * @param verweise
     *            die Liste der zu verknüpfenden Einträge
     * @param startJahr
     *            das Jahr ab dem der Eintrag gültig ist
     * @param endJahr
     *            das Jahr bis zu dem der Eintrag gültig ist
     * @return die neue Instanz
     */
    public static OderVerknuepfung of(KalenderEintragProvider provider, String name, List<Verweis> verweise,
            int startJahr, int endJahr) {
        OderVerknuepfung result = OderVerknuepfung.of(provider, name);
        result.setStartJahr(startJahr);
        result.setEndJahr(endJahr);
        result.setVerweise(verweise);
        return result;
    }

    private OderVerknuepfung(KalenderEintragProvider provider, String name) {
        super(provider, name, null);
    }

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
    protected boolean getInitialenBerechnungsZustand() {
        return false;
    }

}

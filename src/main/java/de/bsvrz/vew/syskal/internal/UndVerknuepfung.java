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
 * Repräsentation einer logischen Verknüpfung mehrere Systemkalendereinträge mit
 * UND.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class UndVerknuepfung extends LogischerVerkuepfungsEintrag {

    public static UndVerknuepfung of(KalenderEintragProvider provider, String name) {
        return new UndVerknuepfung(provider, name);
    }

    public static UndVerknuepfung of(KalenderEintragProvider provider, String name, List<Verweis> verweise,
            int startJahr, int endJahr) {
        UndVerknuepfung result = UndVerknuepfung.of(provider, name);
        result.setStartJahr(startJahr);
        result.setEndJahr(endJahr);
        result.setVerweise(verweise);
        return result;
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
     *            der definierende String des Eintrags
     */
    public UndVerknuepfung(KalenderEintragProvider provider, final String name, final String definition) {
        super(provider, name, definition);
    }

    private UndVerknuepfung(KalenderEintragProvider provider, String name) {
        super(provider, name, null);
    }

    @Override
    public String getVerknuepfungsArt() {
        return "UND";
    }

    @Override
    protected boolean getInitialenBerechnungsZustand() {
        return true;
    }
}

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

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;

/**
 * Repräsentiert einen Kalendereintrag mit einem zugeordneten Zeitoffset, der
 * innerhalb eines {@link LogischerVerkuepfungsEintrag} verwendet wird, um die
 * Gültigkeit zu bestimmen.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class KalenderEintragMitOffset {

    private final KalenderEintrag kalenderEintrag;
    private final int tagesOffset;

    /**
     * erzeugt eine Instanz mit dem übergebenen Kalendereintrag und dem Offset
     * 0.
     * 
     * @param kalenderEintrag
     *            der KalenderEintrag
     */
    public KalenderEintragMitOffset(KalenderEintrag kalenderEintrag) {
        this(kalenderEintrag, 0);
    }

    /**
     * erzeugt eine Instanz aus übergebenen Kalendereintrag und Tagesoffset.
     * 
     * @param kalenderEintrag
     *            der KalenderEintrag
     * @param tagesOffset
     *            der Offset in Tagen
     */
    public KalenderEintragMitOffset(KalenderEintrag kalenderEintrag, int tagesOffset) {
        this.kalenderEintrag = kalenderEintrag;
        this.tagesOffset = tagesOffset;
    }

    /**
     * berechnet die Gültigkeit des Kalendereintrags gemäß
     * {@link KalenderEintrag#getZeitlicheGueltigkeit(LocalDateTime)} unter
     * Berücksichtigung des zugewiesenen Tagesoffsets.
     * 
     * @param zeitpunkt
     *            der Zeitpunkt für den die Gültigkeit bestimmt werden soll.
     * @return die berechnete Gültigkeit
     */
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {
        SystemkalenderGueltigkeit gueltigkeit = kalenderEintrag.berechneZeitlicheGueltigkeit(zeitpunkt);
        return gueltigkeit.withTagesOffset(tagesOffset);
    }

    /**
     * berechnet die Gültigkeit des Kalendereintrags gemäß
     * {@link KalenderEintrag#getZeitlicheGueltigkeitVor(LocalDateTime)} unter
     * Berücksichtigung des zugewiesenen Tagesoffsets.
     * 
     * @param zeitPunkt
     *            der Zeitpunkt für den die Gültigkeit bestimmt werden soll.
     * @return die berechnete Gültigkeit
     */
    public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVor(LocalDateTime zeitPunkt) {
        SystemkalenderGueltigkeit gueltigkeit = kalenderEintrag
                .berechneZeitlicheGueltigkeitVor(zeitPunkt.minusDays(tagesOffset));
        return gueltigkeit.withTagesOffset(tagesOffset);
    }

    /**
     * erzeugt eine neue Instanz unter Verwendung des übergebenen Tagesoffsets.
     * 
     * @param offset
     *            der Tagesoffset
     * @return neue Instanz
     */
    public KalenderEintragMitOffset withTagesOffset(int offset) {
        return new KalenderEintragMitOffset(kalenderEintrag, tagesOffset + offset);
    }
    
    @Override
    public String toString() {
        return kalenderEintrag + " " + tagesOffset + " Tage";
    }
}

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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Die Gültigkeit eines Systemkalendereintrags.
 * 
 * Eine Instanz der Gültigkeit enthält zwei Zustandswechsel, die den aktuellen
 * Zustand mit dem Zeitpunkt zu dem dieser entstanden ist und den
 * nächstfolgenden Zustandswechsel mit dem Wechselzeitpunkt repräsentieren.
 * 
 * @author BitCtrl Systems GmbH. Uwe Peuker
 */
public class SystemkalenderGueltigkeit {

    /**
     * Standardwert für einen ungültigen Zustand über den gesamten zulässigen
     * Zeitbereich.
     */
    public static final SystemkalenderGueltigkeit NICHT_GUELTIG = SystemkalenderGueltigkeit.of(
            ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME),
            ZustandsWechsel.aufUngueltig(SystemKalender.MAX_DATETIME));

    ZustandsWechsel ersterWechsel = ZustandsWechsel.aufUngueltig(SystemKalender.MIN_DATETIME);
    ZustandsWechsel naechsterWechsel = ZustandsWechsel.aufUngueltig(SystemKalender.MAX_DATETIME);

    private SystemkalenderGueltigkeit() {
    }

    /**
     * erzeugt eine Instanz der {@link SystemkalenderGueltigkeit} mit den
     * übergebenen Zustandswechseln.
     * 
     * @param beginn
     *            der aktuelle Zustandswechsel
     * @param wechsel
     *            der folgende Zustandswechsel
     * @return die neue Instanz
     */
    public static SystemkalenderGueltigkeit of(ZustandsWechsel beginn, ZustandsWechsel wechsel) {
        SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
        gueltigkeit.ersterWechsel = beginn;
        gueltigkeit.naechsterWechsel = wechsel;
        return gueltigkeit;
    }

    /**
     * erzeugt eine Instanz der {@link SystemkalenderGueltigkeit} die Gültig
     * wird und zukünftig auf den Zustand UNGÜLTIG wechselt.
     * 
     * @param beginn
     *            der Zeitpunkt zu dem die Gültigkeit erreicht wurde
     * @param wechsel
     *            der Zeitpunkt zu dem die Gültigkeit endet
     * @return die neue Instanz
     */
    public static SystemkalenderGueltigkeit gueltig(LocalDateTime beginn, LocalDateTime wechsel) {
        SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
        gueltigkeit.ersterWechsel = ZustandsWechsel.aufGueltig(beginn);
        gueltigkeit.naechsterWechsel = ZustandsWechsel.aufUngueltig(wechsel);
        return gueltigkeit;
    }

    /**
     * erzeugt eine Instanz der {@link SystemkalenderGueltigkeit} die Gültig
     * wird und zukünftig auf den Zustand UNGÜLTIG wechselt.
     * 
     * @param beginn
     *            das Datum zu dem die Gültigkeit erreicht wurde
     * @param wechsel
     *            das Datum zu dem die Gültigkeit endet
     * @return die neue Instanz
     */
    public static SystemkalenderGueltigkeit gueltig(LocalDate beginn, LocalDate wechsel) {
        SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
        gueltigkeit.ersterWechsel = ZustandsWechsel.aufGueltig(beginn);
        gueltigkeit.naechsterWechsel = ZustandsWechsel.aufUngueltig(wechsel);
        return gueltigkeit;
    }

    /**
     * erzeugt eine Instanz der {@link SystemkalenderGueltigkeit} die Ungültig
     * ist und zukünftig auf den Zustand GÜLTIG wechselt.
     * 
     * @param beginn
     *            der Zeitpunkt zu dem die Ungültigkeit erreicht wurde
     * @param wechsel
     *            der Zeitpunkt zu dem die folgende Gültigkeit beginnt
     * @return die neue Instanz
     */
    public static SystemkalenderGueltigkeit ungueltig(LocalDateTime beginn, LocalDateTime wechsel) {
        SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
        gueltigkeit.ersterWechsel = ZustandsWechsel.aufUngueltig(beginn);
        gueltigkeit.naechsterWechsel = ZustandsWechsel.aufGueltig(wechsel);
        return gueltigkeit;
    }

    /**
     * erzeugt eine Instanz der {@link SystemkalenderGueltigkeit} die Ungültig
     * ist und zukünftig auf den Zustand GÜLTIG wechselt.
     * 
     * @param beginn
     *            das Datum zu dem die Ungültigkeit erreicht wurde
     * @param wechsel
     *            das Datum zu dem die folgende Gültigkeit beginnt
     * @return die neue Instanz
     */
    public static SystemkalenderGueltigkeit ungueltig(LocalDate beginn, LocalDate wechsel) {
        SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
        gueltigkeit.ersterWechsel = ZustandsWechsel.aufUngueltig(beginn);
        gueltigkeit.naechsterWechsel = ZustandsWechsel.aufGueltig(wechsel);
        return gueltigkeit;
    }

    /**
     * liefert den initialen Zustandswechsel.
     * 
     * @return den Wechsel
     */
    public ZustandsWechsel getErsterWechsel() {
        return ersterWechsel;
    }

    /**
     * liefert den zukünftigen Zustandswechsel.
     * 
     * @return den Wechsel
     */
    public ZustandsWechsel getNaechsterWechsel() {
        return naechsterWechsel;
    }

    /**
     * ermittelt, ob aktuell Gültigkeit besteht.
     * 
     * @return die Gültigkeit
     */
    public boolean isZeitlichGueltig() {
        return ersterWechsel.isWirdGueltig();
    }

    @Override
    public String toString() {
        return ersterWechsel + " --> " + naechsterWechsel;
    }

    /**
     * erzeugt eine neue Instanz der Gültigkeit, bei der die Zeitpunkte um den
     * übergebenen Tagesoffset angepasst werden.
     * 
     * @param tagesOffset
     *            der Tagesoffset
     * @return die neue Instanz
     */
    public SystemkalenderGueltigkeit withTagesOffset(int tagesOffset) {
        SystemkalenderGueltigkeit result = new SystemkalenderGueltigkeit();
        result.ersterWechsel = ersterWechsel.withTagesOffset(tagesOffset);
        result.naechsterWechsel = naechsterWechsel.withTagesOffset(tagesOffset);
        return result;
    }
}

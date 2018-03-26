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
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

/**
 * Definition eines Zustandswechsels in der Gültigkeit eines
 * Systemkalendereintrags.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZustandsWechsel {

    /** Comparator zur zeitlichen Sortierung von Zustandswechseln. */
    public static final Comparator<ZustandsWechsel> ZEIT_COMPARATOR = new Comparator<ZustandsWechsel>() {

        @Override
        public int compare(ZustandsWechsel o1, ZustandsWechsel o2) {
            return o1.getZeitPunkt().compareTo(o2.getZeitPunkt());
        }
    };

    private LocalDateTime zeitPunkt;
    private boolean wirdGueltig;

    /**
     * erzeugt eine neue Instanz eines Zustandswechsels, der zum übergebenen
     * Datum gültig wird.
     * 
     * @param datum
     *            das Datum
     * @return die neue Instanz
     */
    public static ZustandsWechsel aufGueltig(LocalDate datum) {
        return new ZustandsWechsel(LocalDateTime.of(datum, LocalTime.MIDNIGHT), true);
    }

    /**
     * erzeugt eine neue Instanz eines Zustandswechsels, der zum übergebenen
     * Zeitpunkt gültig wird.
     * 
     * @param zeitPunkt
     *            der Zeitpunkt
     * @return die neue Instanz
     */
    public static ZustandsWechsel aufGueltig(LocalDateTime zeitPunkt) {
        return new ZustandsWechsel(zeitPunkt, true);
    }

    /**
     * erzeugt eine neue Instanz eines Zustandswechsels, der zum übergebenen
     * Datum ungültig wird.
     * 
     * @param datum
     *            das Datum
     * @return die neue Instanz
     */
    public static ZustandsWechsel aufUngueltig(LocalDate datum) {
        return new ZustandsWechsel(LocalDateTime.of(datum, LocalTime.MIDNIGHT), false);
    }

    /**
     * erzeugt eine neue Instanz eines Zustandswechsels, der zum übergebenen
     * Zeitpunkt ungültig wird.
     * 
     * @param zeitPunkt
     *            der Zeitpunkt
     * @return die neue Instanz
     */
    public static ZustandsWechsel aufUngueltig(LocalDateTime zeitPunkt) {
        return new ZustandsWechsel(zeitPunkt, false);
    }

    /**
     * erzeugt eine neue Instanz eines Zustandswechsels, der zum übergebenen
     * Zeitpunkt die angegebene Gültigkeit hat.
     * 
     * @param zeitPunkt
     *            der Zeitpunkt
     * @param wirdGueltig
     *            die Gültigkeit
     * @return die neue Instanz
     */
    public static ZustandsWechsel of(LocalDateTime zeitPunkt, boolean wirdGueltig) {
        return new ZustandsWechsel(zeitPunkt, wirdGueltig);
    }

    /**
     * erzeugt eine neue Instanz eines Zustandswechsels, der zum übergebenen
     * Datum die angegebene Gültigkeit hat.
     * 
     * @param datum
     *            das Datum
     * @param wirdGueltig
     *            die Gültigkeit
     * @return die neue Instanz
     */
    public static ZustandsWechsel of(LocalDate datum, boolean wirdGueltig) {
        return new ZustandsWechsel(LocalDateTime.of(datum, LocalTime.MIDNIGHT), wirdGueltig);
    }

    private ZustandsWechsel(LocalDateTime zeitPunkt, boolean wirdGueltig) {
        this.zeitPunkt = zeitPunkt;
        this.wirdGueltig = wirdGueltig;
    }

    /**
     * liefert den Zeitpunkt des Zustandswechsels.
     * 
     * @return den Zeitpunkt
     */
    public LocalDateTime getZeitPunkt() {
        return zeitPunkt;
    }

    /**
     * liefert den Zielzustand des Wechsels
     * 
     * @return der Zustand
     */
    public boolean isWirdGueltig() {
        return wirdGueltig;
    }

    @Override
    public String toString() {
        return "ZustandsWechsel [" + zeitPunkt + ": " + wirdGueltig + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(zeitPunkt, wirdGueltig);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        ZustandsWechsel other = (ZustandsWechsel) obj;
        return Objects.equals(zeitPunkt, other.zeitPunkt) && wirdGueltig == other.wirdGueltig;
    }

    /**
     * liefert eine neue Instanz eines Zustandswechsel mit dem um den
     * angegebenen Tagesoffset verschobenen Zeitstempel.
     * 
     * @param tagesOffset
     *            der Tagesoffset
     * @return die neue Instanz
     */
    public ZustandsWechsel withTagesOffset(int tagesOffset) {
        LocalDateTime neuerZeitPunkt = zeitPunkt.plusDays(tagesOffset);
        if (neuerZeitPunkt.isBefore(SystemKalender.MIN_DATETIME)) {
            neuerZeitPunkt = SystemKalender.MIN_DATETIME;
        } else if (neuerZeitPunkt.isAfter(SystemKalender.MAX_DATETIME)) {
            neuerZeitPunkt = SystemKalender.MAX_DATETIME;
        }

        return ZustandsWechsel.of(neuerZeitPunkt, wirdGueltig);
    }
}

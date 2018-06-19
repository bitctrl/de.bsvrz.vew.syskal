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

import java.time.LocalDateTime;
import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.puk.param.lib.daten.UrlasserInfo;
import de.bsvrz.vew.syskal.internal.EintragsVerwaltung;

/**
 * Zentraler Zugriffspunkt die die Daten eines Kalenderobjekts in einer
 * Datenverteilerkonfiguration.
 * 
 * Über eine Instanz des {@link SystemKalender} kann auf die Menge der für ein
 * Systemobjekt vom Typ "typ.kalender" definierten Systemkalendereinträge
 * zugegriffen werden.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class SystemKalender {

    /** der früheste vom Systemkalender betrachtete Zeitpunkt. */
    public static final LocalDateTime MIN_DATETIME = LocalDateTime.of(1000, 1, 1, 0, 0, 0);

    /** der späteste vom Systemkalender betrachtete Zeitpunkt. */
    public static final LocalDateTime MAX_DATETIME = LocalDateTime.of(3000, 1, 1, 0, 0, 0);

    private final EintragsVerwaltung verwaltung;

    /**
     * erzeugt eine {@link SystemKalender}-Instanz für die übergebene
     * Datenverteilerverbindung und das angegebene Kalenderobjekt (vom Typ
     * "typ.kalender")
     * 
     * @param dav
     *            die Datenverteilerverbindung
     * @param object
     *            das Kalenderobjekt
     */
    public SystemKalender(ClientDavInterface dav, ConfigurationObject object) {
        verwaltung = new EintragsVerwaltung(this, dav, object);
    }

    /**
     * liefert den Systemkalendereintrg, der durch das übergebene Systemobjekt
     * definiert wird.
     * 
     * @param object
     *            das Systemobjekt
     * @return den Systemkalendereintrag oder null, wenn kein entsprechender
     *         Eintrag gefunden verwaltet wird.
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht ermittelt werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist
     */
    public SystemKalenderEintrag getEintrag(SystemObject object) throws SystemKalenderException {
        return verwaltung.getSystemKalenderEintrag(object);
    }

    /**
     * liefert die Menge der verwalteten Systemkalendereinträge.
     * 
     * @return die Menge der Einträge
     * @throws SystemKalenderException
     *             die Systemkalendereinträge konnten nicht ermittelt werden,
     *             weil die Datenverteilerverbindung nicht mehr besteht
     */
    public Collection<SystemKalenderEintrag> getEintraege() throws SystemKalenderException {
        return verwaltung.getSystemKalenderEintraege();
    }

    /**
     * speichert den übergebenen Systemkalendereintrag.
     * 
     * In Datenverteilermodell wird der entsprechende Eintrag aktualisiert oder
     * ein neuer angelegt.
     * 
     * @param eintrag
     *            der Eintrag, der gesichert werden soll
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht gespeichert werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist oder ein
     *             Fehler beim Anlegen oder Aktualisieren des gewünschten
     *             dynamischen Systemobjekts aufgetreten ist
     */
    public void sichereEintrag(SystemKalenderEintrag eintrag) throws SystemKalenderException {
        verwaltung.sichereEintrag(eintrag);
    }

    /**
     * speichert den übergebenen Systemkalendereintrag.
     * 
     * In Datenverteilermodell wird der entsprechende Eintrag aktualisiert oder
     * ein neuer angelegt.
     * 
     * @param eintrag
     *            der Eintrag, der gesichert werden soll
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht gespeichert werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist oder ein
     *             Fehler beim Anlegen oder Aktualisieren des gewünschten
     *             dynamischen Systemobjekts aufgetreten ist
     */
    public void sichereEintragMitUrlasser(SystemKalenderEintrag eintrag, UrlasserInfo urlasser) throws SystemKalenderException {
        verwaltung.sichereEintrag(eintrag, urlasser);
    }

    
    /**
     * löscht den übergebenen Systemkalendereintrag.
     * 
     * In Datenverteilermodell wird der entsprechende Eintrag entfernt.
     * 
     * @param eintrag
     *            der Eintrag, der gelöscht werden soll
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht gelöscht werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist oder ein
     *             Fehler beim Löschen des entsprechenden dynamischen
     *             Systemobjekts aufgetreten ist
     */
    public void loescheEintrag(SystemKalenderEintrag eintrag) throws SystemKalenderException {
        verwaltung.loescheEintrag(eintrag);
    }

    /**
     * entfernt alle Systemkalendereinträge aus dem verwalteten Kalender.
     * 
     * @throws SystemKalenderException
     *             die Kalender konnte nicht geleert werden, weil die
     *             Datenverteilerverbindung nicht mehr besteht oder ein Fehler
     *             beim Löschen eines dynamischen Objekts aufgetreten ist
     */
    public void leereSystemKalender() throws SystemKalenderException {
        verwaltung.leereSystemKalender();
    }

    /**
     * entfernt alle Systemkalendereinträge, die in der
     * Datenverteilerkonfigration existieren und nicht im verwalteten Kalender
     * enthalten sind.
     * 
     * @throws SystemKalenderException
     *             die Kalender konnte nicht geleert werden, weil die
     *             Datenverteilerverbindung nicht mehr besteht oder ein Fehler
     *             beim Löschen eines dynamischen Objekts aufgetreten ist
     */
    public void bereinigeSystemKalender() throws SystemKalenderException {
        verwaltung.bereinigeSystemKalender();
    }

    /**
     * fügt einen Listener hinzu, der bei Änderungen im Systemkalender
     * benachrichtigt wird.
     * 
     * @param listener
     *            der Listener
     */
    public void addSystemKalenderListener(SystemKalenderListener listener) {
        verwaltung.addSystemKalenderListener(listener);
    }

    /**
     * entfernt einen Listener, der bei Änderungen im Systemkalender
     * benachrichtigt wurde.
     * 
     * @param listener
     *            der Listener
     */
    public void removeSystemKalenderListener(SystemKalenderListener listener) {
        verwaltung.removeSystemKalenderListener(listener);
    }

}

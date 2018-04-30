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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Objects;

import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.VorDefinierterEintrag;

/**
 * Repräsentation einen {@link SystemKalenderEintrag}s, der mit einem
 * entsprechenden Systemobjekt im verwendeten Datenverteiler-Modell verbunden
 * ist.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class SystemKalenderEintrag {

    /** ID für die Property Kalendereintrag. */
    public static final String PROP_KALENDEREINTRAG = SystemKalenderEintrag.class.getSimpleName() + ".kalenderEintrag";

    private KalenderEintrag kalenderEintrag = VorDefinierterEintrag.UNDEFINIERT;
    private KalenderEintragProvider provider;
    PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);

    private DynamicObject systemObject;

    private String name;
    private String pid;

    private String originalDefinition;

    /**
     * erzeugt einen Systemkalendereintrag mit dem Daten.
     * 
     * Das Objekt wird nicht Datenverteilermodell hinterlegt, das erfolgt erst
     * mit {@link SystemKalender#sichereEintrag(SystemKalenderEintrag)}!
     * 
     * @param name
     *            der Name des Eintrags
     * @param pid
     *            die PID des Eintrags
     * @param eintrag
     *            der verwendete Kalendereintrag
     * @return der {@link SystemKalenderEintrag}
     */
    public static SystemKalenderEintrag of(String name, String pid, KalenderEintrag eintrag) {
        SystemKalenderEintrag result = new SystemKalenderEintrag();
        result.name = name;
        result.pid = pid;
        result.setKalenderEintrag(eintrag);
        return result;
    }

    private SystemKalenderEintrag() {

    }

    /**
     * erzeugt eine Instanz eines {@link SystemKalenderEintrag} der das
     * übergebene Objekt aus der Datenverteiler-Konfiguration in der angegebenen
     * Kalenderverwaltung repräsentiert.
     * 
     * @param provider
     *            die Kalendereintragsverwaltung zur Verifizierung von
     *            Referenzen
     * @param obj
     *            das Objekt aus der Datenverteilerkonfiguration
     */
    public SystemKalenderEintrag(KalenderEintragProvider provider, DynamicObject obj) {
        this.provider = provider;
        this.systemObject = obj;
    }

    void bestimmeKalendereintrag() {
        name = systemObject.getName();
        pid = systemObject.getPid();
        if (originalDefinition == null) {
            setKalenderEintrag(VorDefinierterEintrag.UNDEFINIERT);
        } else {
            setKalenderEintrag(KalenderEintrag.parse(provider, name, originalDefinition));
        }
    }

    private void setKalenderEintrag(KalenderEintrag eintrag) {
        KalenderEintrag alterEintrag = kalenderEintrag;
        kalenderEintrag = eintrag;
        propertySupport.firePropertyChange(PROP_KALENDEREINTRAG, alterEintrag, kalenderEintrag);
    }

    /**
     * liefert den zugeordneten logischen {@link KalenderEintrag}.
     * 
     * @return den Eintrag
     */
    public KalenderEintrag getKalenderEintrag() {
        return kalenderEintrag;
    }

    /**
     * liefert das zugeordnete Systemobjekt aus der
     * Datenverteiler-Konfiguration.
     * 
     * Das Objekt kann null sein, wenn der {@link SystemKalenderEintrag} noch
     * nicht gesichert wurde.
     * 
     * @return das Systemobjekt oder null
     */
    public SystemObject getSystemObject() {
        return systemObject;
    }

    /**
     * setzt den Definitionstext des Kalendereintrags und erzeugt
     * gegegbenenfalls einen neuen logischen {@link KalenderEintrag}.
     * 
     * @param text
     *            der Definitionstext
     */
    public void setDefinition(String text) {
        if (!Objects.equals(originalDefinition, text)) {
            originalDefinition = text;
            bestimmeKalendereintrag();
        }
    }

    /**
     * liefert den Name des Eintrags.
     * 
     * Der Name entspricht dem Name des Systemobjekts, wenn ein entsprechendes
     * Objekt verknüpft ist.
     * 
     * @return den Name
     */
    public String getName() {
        if (systemObject == null) {
            return name;
        }
        return systemObject.getName();
    }

    /**
     * liefert die PID des Eintrags.
     * 
     * Die PID entspricht der PID des Systemobjekts, wenn ein entsprechendes
     * Objekt verknüpft ist.
     * 
     * @return die PID
     */
    public String getPid() {
        if (systemObject == null) {
            return pid;
        }
        return systemObject.getPid();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        builder.append(systemObject.getName());
        builder.append(':');

        if (kalenderEintrag.hasFehler()) {
            builder.append("FEHLER :");
        } else {
            builder.append("OK    :");
        }
        builder.append(kalenderEintrag);
        return builder.toString();
    }

    /**
     * aktualisiert den zugeordneten Kalendereintrag bei Bedarf.
     * 
     * Eine Aktualisierung ist erforderlich, wenn der Eintrag einen der
     * übergebenen Referenzeinträge benutzt.
     * 
     * @param referenzen
     *            die Referenzen, die potentiell eine Neubestimmung des
     *            Kalendereintrags erfordern
     */
    public void aktualisiereVonReferenzen(Collection<SystemKalenderEintrag> referenzen) {
        for (SystemKalenderEintrag referenz : referenzen) {
            if (referenz.equals(this)) {
                continue;
            }

            if (getKalenderEintrag().benutzt(referenz.getKalenderEintrag())) {
                bestimmeKalendereintrag();
                return;
            }
        }
    }

    /**
     * prüft, ob der Name für den Systemkalendereintrag zulässig ist.
     * 
     * Zuläassige Namen enthalten nur Buchstaben, Ziffern und den Unterstrich.
     * 
     * @param name
     *            der zu prüfende Name
     * @return die Zulässigkeit
     */
    public static boolean isNameGueltig(String name) {

        boolean result = true;
        String testName = name.trim();

        if (testName.length() <= 0) {
            result = false;
        } else {
            for (final char ch : testName.toCharArray()) {
                final int type = Character.getType(ch);
                if ((type != Character.UPPERCASE_LETTER) && (type != Character.LOWERCASE_LETTER)
                        && (type != Character.DECIMAL_DIGIT_NUMBER) && (ch != '_')) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * fügt einen Listener hinzu, der benachrichtigt werden soll, wenn sich der
     * logische Kalendereintrag ändert.
     * 
     * @param listener
     *            der Listener
     */
    public void addKalenderEintragChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * entfernt einen Listener hinzu, der benachrichtigt wurde, wenn sich der
     * logische Kalendereintrag ändert.
     * 
     * @param listener
     *            der Listener
     */
    public void removeKalenderEintragChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}

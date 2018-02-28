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

import java.util.Collection;
import java.util.Objects;

import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.SystemKalenderEintrag;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.VorDefinierterEintrag;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SystemKalenderEintrag {

    private final ObjectProperty<KalenderEintrag> kalenderEintragProperty = new SimpleObjectProperty<>(this,
            "kalendereintrag", VorDefinierterEintrag.UNDEFINIERT);
    private KalenderEintragProvider provider;
    
    private DynamicObject systemObject;

    private String name;
    private String pid;
    
    private String originalDefinition;

    public static SystemKalenderEintrag of(String name, String pid, KalenderEintrag eintrag) {
        SystemKalenderEintrag result = new SystemKalenderEintrag();
        result.name= name;
        result.pid = pid;
        result.kalenderEintragProperty.set(eintrag);
        return result;
    }
    
    private SystemKalenderEintrag() {
        
    }
    
    public SystemKalenderEintrag(KalenderEintragProvider provider, DynamicObject obj) {
        this.provider = provider;
        this.systemObject = obj;
    }

    void bestimmeKalendereintrag() {
        name = systemObject.getName();
        pid = systemObject.getPid();
        if (originalDefinition == null) {
            kalenderEintragProperty.set(VorDefinierterEintrag.UNDEFINIERT);
        } else {
            kalenderEintragProperty.set(KalenderEintrag.parse(provider, name, originalDefinition));
        }
    }

    public KalenderEintrag getKalenderEintrag() {
        return kalenderEintragProperty.get();
    }

    public SystemObject getSystemObject() {
        return systemObject;
    }

    public void setDefinition(String text) {
        if (!Objects.equals(originalDefinition, text)) {
            originalDefinition = text;
            bestimmeKalendereintrag();
        }
    }

    public String getName() {
        if( systemObject == null) {
            return name;
        }
        return systemObject.getName();
    }

    public String getPid() {
        if( systemObject == null) {
            return pid;
        }
        return systemObject.getPid();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        builder.append(systemObject.getName());
        builder.append(':');

        if (kalenderEintragProperty.get().isFehler()) {
            builder.append("FEHLER :");
        } else {
            builder.append("OK    :");
        }
        builder.append(kalenderEintragProperty.get());
        return builder.toString();
    }

    public ObjectProperty<KalenderEintrag> getKalenderEintragProperty() {
        return kalenderEintragProperty;
    }

    public void aktualisiereVonReferenzen(Collection<SystemKalenderEintrag> referenzen) {
        for (SystemKalenderEintrag referenz : referenzen) {
            if (referenz.equals(this)) {
                continue;
            }

            if (getKalenderEintrag().benutzt(referenz)) {
                bestimmeKalendereintrag();
                return;
            }
        }
    }
    
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
}

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
import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;

public final class TestKalenderEintragProvider implements KalenderEintragProvider {
    Map<String, KalenderEintrag> eintraege = new LinkedHashMap<>();

    public TestKalenderEintragProvider() {
        addEintrag(KalenderEintrag.parse(this, "Montag", "Montag"));
        addEintrag(KalenderEintrag.parse(this, "Dienstag", "Dienstag"));
        addEintrag(KalenderEintrag.parse(this, "Mittwoch", "Mittwoch"));
        addEintrag(KalenderEintrag.parse(this, "Donnerstag", "Donnerstag"));
        addEintrag(KalenderEintrag.parse(this, "Freitag", "Freitag"));
        addEintrag(KalenderEintrag.parse(this, "Samstag", "Samstag"));
        addEintrag(KalenderEintrag.parse(this, "Sonntag", "Sonntag"));
        addEintrag(KalenderEintrag.parse(this, "Ostersonntag", "Ostersonntag"));
    }

    @Override
    public KalenderEintrag getKalenderEintrag(String name) {
        return eintraege.get(name);
    }

    public void addEintrag(KalenderEintrag eintrag) {
        eintraege.put(eintrag.getName(), eintrag);
    }

    public KalenderEintrag parseAndAdd(TestKalenderEintragProvider provider, String name, String definition) {
        KalenderEintrag eintrag = KalenderEintrag.parse(provider, name, definition);
        addEintrag(eintrag);
        return eintrag;
    }

    public Collection<KalenderEintrag> getKalenderEintraege() {
        return eintraege.values();
    }
}
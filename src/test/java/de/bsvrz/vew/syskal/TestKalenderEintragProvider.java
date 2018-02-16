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

import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.vew.syskal.internal.KalenderEintragImpl;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;

public final class TestKalenderEintragProvider implements KalenderEintragProvider {
	Map<String, KalenderEintragImpl> eintraege = new LinkedHashMap<>();

	public TestKalenderEintragProvider() {
		addEintrag(KalenderEintragImpl.parse(this, "Montag", "Montag"));
		addEintrag(KalenderEintragImpl.parse(this, "Dienstag", "Dienstag"));
		addEintrag(KalenderEintragImpl.parse(this, "Mittwoch", "Mittwoch"));
		addEintrag(KalenderEintragImpl.parse(this, "Donnerstag", "Donnerstag"));
		addEintrag(KalenderEintragImpl.parse(this, "Freitag", "Freitag"));
		addEintrag(KalenderEintragImpl.parse(this, "Samstag", "Samstag"));
		addEintrag(KalenderEintragImpl.parse(this, "Sonntag", "Sonntag"));
		addEintrag(KalenderEintragImpl.parse(this, "Ostersonntag", "Ostersonntag"));
	}
	
	@Override
	public KalenderEintragImpl getKalenderEintrag(String name) {
		return eintraege.get(name);
	}

	public void addEintrag(KalenderEintragImpl eintrag) {
		eintraege.put(eintrag.getName(), eintrag);
	}

	public KalenderEintragImpl parseAndAdd(TestKalenderEintragProvider provider, String name, String definition) {
		KalenderEintragImpl eintrag = KalenderEintragImpl.parse(provider, name, definition);
		addEintrag(eintrag);
		return eintrag;
	}
}
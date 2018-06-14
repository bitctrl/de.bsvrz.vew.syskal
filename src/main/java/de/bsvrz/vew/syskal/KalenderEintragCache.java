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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Cache für die Gültigkeiten eines Kalendereintrags.
 * 
 * @author BitCtrl Systems GmbH. Uwe Peuker
 */
public class KalenderEintragCache {

	private static final int MAX_CACHE_SIZE = 200;

	private KalenderEintrag eintrag;
	private SortedMap<LocalDateTime, SystemkalenderGueltigkeit> daten = new TreeMap<>();

	public KalenderEintragCache(KalenderEintrag eintrag) {
		this.eintrag = eintrag;
	}

	public SystemkalenderGueltigkeit getGueltigkeitFuer(LocalDateTime zeitpunkt) {

		synchronized (daten) {
			SystemkalenderGueltigkeit result = daten.get(zeitpunkt);
			if (result != null) {
				return result;
			}

			SortedMap<LocalDateTime, SystemkalenderGueltigkeit> headMap = daten.headMap(zeitpunkt);
			if (!headMap.isEmpty()) {
				SystemkalenderGueltigkeit gueltigkeit = headMap.get(headMap.lastKey());
				if (!Duration.between(gueltigkeit.ersterWechsel.getZeitPunkt(), zeitpunkt).isNegative()) {
					if (Duration.between(gueltigkeit.naechsterWechsel.getZeitPunkt(), zeitpunkt).isNegative()) {
						return gueltigkeit;
					}
				}
			}

			SortedMap<LocalDateTime, SystemkalenderGueltigkeit> tailMap = daten.tailMap(zeitpunkt);
			if (!tailMap.isEmpty()) {
				SystemkalenderGueltigkeit gueltigkeit = tailMap.get(tailMap.firstKey());
				if (!Duration.between(gueltigkeit.ersterWechsel.getZeitPunkt(), zeitpunkt).isNegative()) {
					if (Duration.between(gueltigkeit.naechsterWechsel.getZeitPunkt(), zeitpunkt).isNegative()) {
						return gueltigkeit;
					}
				}
			}

			result = eintrag.berechneZeitlicheGueltigkeit(zeitpunkt);
			daten.put(result.ersterWechsel.getZeitPunkt(), result);

			bereinigeCache();
			return result;

		}
	}

	private void bereinigeCache() {

		while (daten.size() > MAX_CACHE_SIZE) {
			daten.remove(daten.firstKey());
		}
	}

	public void leeren() {
		synchronized (daten) {
			daten.clear();
		}
	}

}

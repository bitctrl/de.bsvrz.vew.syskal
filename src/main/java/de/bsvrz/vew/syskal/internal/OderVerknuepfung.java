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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentiert die Daten eines Eintrags, der mehrere andere
 * Systemkalendereinträge logisch per ODER verknüpft.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class OderVerknuepfung extends LogischerVerkuepfungsEintrag {

	/**
	 * Konstruktor.
	 * 
	 * @param provider
	 *            die Verwaltung aller bekannten Systemkalendereinträge zur
	 *            Verifizierung von Referenzen
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            der definierende Text des Eintrags
	 */
	public OderVerknuepfung(KalenderEintragProvider provider, final String name, final String definition) {
		super(provider, name, definition);
	}

	@Override
	public String getVerknuepfungsArt() {
		return "ODER";
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {

		boolean zustand = false;
		Map<KalenderEintragImpl, ZustandsWechsel> potentielleStartWechsel = new LinkedHashMap<>();
		Map<KalenderEintragImpl, ZustandsWechsel> potentielleEndWechsel = new LinkedHashMap<>();

		for (VerweisEintrag verweis : getVerweise()) {

			SystemkalenderGueltigkeit gueltigKeit = verweis.getZeitlicheGueltigkeit(zeitpunkt);
			if (gueltigKeit.isZeitlichGueltig()) {
				zustand = true;
			}
			potentielleStartWechsel.put(verweis, gueltigKeit.getErsterWechsel());
			potentielleEndWechsel.put(verweis, gueltigKeit.getNaechsterWechsel());
		}

		ZustandsWechsel beginn = berechneVorigenWechselAuf(zustand, potentielleStartWechsel);
		ZustandsWechsel wechsel = berechneNaechstenWechselAuf(!zustand, potentielleEndWechsel);

		return SystemkalenderGueltigkeit.of(beginn, wechsel);
	}

	@Override
	protected SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitpunkt) {

		boolean zustand = false;
		Map<KalenderEintragImpl, ZustandsWechsel> potentielleStartWechsel = new LinkedHashMap<>();
		Map<KalenderEintragImpl, ZustandsWechsel> potentielleEndWechsel = new LinkedHashMap<>();

		for (VerweisEintrag verweis : getVerweise()) {

			SystemkalenderGueltigkeit gueltigKeit = verweis.getZeitlicheGueltigkeitVor(zeitpunkt);
			if (gueltigKeit.isZeitlichGueltig()) {
				zustand = true;
			}
			potentielleStartWechsel.put(verweis, gueltigKeit.getErsterWechsel());
			potentielleEndWechsel.put(verweis, gueltigKeit.getNaechsterWechsel());
		}

		ZustandsWechsel beginn = berechneVorigenWechselAuf(zustand, potentielleStartWechsel);
		ZustandsWechsel wechsel = berechneNaechstenWechselAuf(!zustand, potentielleEndWechsel);

		return SystemkalenderGueltigkeit.of(beginn, wechsel);
	}

	private ZustandsWechsel berechneNaechstenWechselAuf(boolean zielZustand,
			Map<KalenderEintragImpl, ZustandsWechsel> potentielleWechsel) {

		ZustandsWechsel result = null;

		if (zielZustand) {
			result = berechneNaechstenWechselAufGueltig(potentielleWechsel);
		} else {
			result = berechneNaechstenWechselAufUngueltig(potentielleWechsel);
		}

		if (result == null) {
			return ZustandsWechsel.MAX;
		}
		return result;
	}

	private ZustandsWechsel berechneNaechstenWechselAufUngueltig(
			Map<KalenderEintragImpl, ZustandsWechsel> potentielleWechsel) {

		ZustandsWechsel result = null;
		Map<KalenderEintragImpl, ZustandsWechsel> zustandsWechsel = new LinkedHashMap<>(potentielleWechsel);

		do {
			LocalDateTime moeglicherZeitPunkt = naechsterWechselZeitPunktAufUngueltig(zustandsWechsel);
			if (!moeglicherZeitPunkt.isBefore(SystemKalender.MAX_DATETIME)) {
				return ZustandsWechsel.of(SystemKalender.MAX_DATETIME, true);
			}

			// Alle Einträge müssen ungültig sein
			for (KalenderEintragImpl eintrag : zustandsWechsel.keySet()) {
				if (!eintrag.getZeitlicheGueltigkeit(moeglicherZeitPunkt).isZeitlichGueltig()) {
					result = ZustandsWechsel.of(moeglicherZeitPunkt, false);
				} else {
					result = null;
					break;
				}
			}

			if (result == null) {
				Map<KalenderEintragImpl, ZustandsWechsel> korrigierteZustandsWechsel = new LinkedHashMap<>();
				for (Entry<KalenderEintragImpl, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
					ZustandsWechsel wechsel = entry.getValue();
					if (!wechsel.getZeitPunkt().isAfter(moeglicherZeitPunkt)) {
						do {
							wechsel = entry.getKey().getZeitlicheGueltigkeit(wechsel.getZeitPunkt())
									.getNaechsterWechsel();
						} while (wechsel.isWirdGueltig()
								&& wechsel.getZeitPunkt().isBefore(SystemKalender.MAX_DATETIME));
					}
					if (!wechsel.getZeitPunkt().isAfter(SystemKalender.MAX_DATETIME)) {
						korrigierteZustandsWechsel.put(entry.getKey(), wechsel);
					}
				}
				zustandsWechsel.clear();
				zustandsWechsel.putAll(korrigierteZustandsWechsel);
			}

		} while (result == null);

		return result;
	}

	private LocalDateTime naechsterWechselZeitPunktAufUngueltig(Map<KalenderEintragImpl, ZustandsWechsel> zustandsWechsel) {

		ZustandsWechsel wechsel = null;

		for (Entry<KalenderEintragImpl, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
			ZustandsWechsel aktuell = entry.getValue();
			while (aktuell.isWirdGueltig() && aktuell.getZeitPunkt().isBefore(SystemKalender.MAX_DATETIME)) {
				aktuell = entry.getKey().getZeitlicheGueltigkeit(aktuell.getZeitPunkt()).getNaechsterWechsel();
			}

			if (aktuell.isWirdGueltig()) {
				continue;
			}

			if (wechsel == null) {
				wechsel = aktuell;
			} else if (aktuell.getZeitPunkt().isBefore(wechsel.getZeitPunkt())) {
				wechsel = aktuell;
			}
		}

		if (wechsel == null) {
			return SystemKalender.MAX_DATETIME;
		}

		return wechsel.getZeitPunkt();
	}

	/**
	 * Berechnet den nächsten Wechsel auf den Zustand gültig.
	 * 
	 * Da eine ODER-Verknüpfung besteht muss einer der übergebenen Einträge auf
	 * diesen Zustand führen, der früheste bidet dann den nächsten Zustandswechsel
	 * für den ODER-Eintrag. Wird keine der beteligten Einträge jemals wieder
	 * gültig, wird der ZustandsWechsel mit dem Status "ungültig" auf den maximal
	 * möglichen Zeitpunkt terminiert.
	 * 
	 * @param potentielleWechsel
	 *            die Liste der nächsten Wechsel der beteiligten Verweiseinträge
	 * @return der berechnete Zustandswechsel
	 */
	private ZustandsWechsel berechneNaechstenWechselAufGueltig(
			Map<KalenderEintragImpl, ZustandsWechsel> potentielleWechsel) {

		ZustandsWechsel result = null;

		for (ZustandsWechsel wechsel : potentielleWechsel.values()) {
			if (!wechsel.isWirdGueltig()) {
				continue;
			}
			if (result == null) {
				result = wechsel;
			} else if (result.getZeitPunkt().isAfter(wechsel.getZeitPunkt())) {
				result = wechsel;
			}
		}

		if (result == null) {
			result = ZustandsWechsel.MAX;
		}

		return result;
	}

	private ZustandsWechsel berechneVorigenWechselAuf(boolean zielZustand,
			Map<KalenderEintragImpl, ZustandsWechsel> potentielleWechsel) {

		ZustandsWechsel result = null;

		if (zielZustand) {
			result = berechneVorigenWechselAufGueltig(potentielleWechsel);
		} else {
			for (ZustandsWechsel wechsel : potentielleWechsel.values()) {
				if (wechsel.isWirdGueltig()) {
					continue;
				}
				if (result == null) {
					result = wechsel;
				} else if (result.getZeitPunkt().isAfter(wechsel.getZeitPunkt())) {
					result = wechsel;
				}
			}
		}

		if (result == null) {
			return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, true);
		}
		return result;
	}

	private ZustandsWechsel berechneVorigenWechselAufGueltig(Map<KalenderEintragImpl, ZustandsWechsel> potentielleWechsel) {

		Map<KalenderEintragImpl, ZustandsWechsel> zustandsWechsel = new LinkedHashMap<>(potentielleWechsel);
		LocalDateTime wechselZeit = SystemKalender.MIN_DATETIME;
		LocalDateTime letzteWechselZeit = SystemKalender.MAX_DATETIME;

		do {
			Iterator<Entry<KalenderEintragImpl, ZustandsWechsel>> iterator = zustandsWechsel.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<KalenderEintragImpl, ZustandsWechsel> entry = iterator.next();
				ZustandsWechsel wechsel = entry.getValue();
				while (wechsel.isWirdGueltig() && wechsel.getZeitPunkt().isAfter(SystemKalender.MIN_DATETIME) || !wechsel.getZeitPunkt().isBefore(letzteWechselZeit)) {
					wechsel = entry.getKey().berechneZeitlicheGueltigkeitsVor(wechsel.getZeitPunkt())
							.getErsterWechsel();
				}
				entry.setValue(wechsel);
				if (wechsel.getZeitPunkt().isAfter(wechselZeit)) {
					wechselZeit = wechsel.getZeitPunkt();
				}
			}

			if (!wechselZeit.isBefore(SystemKalender.MAX_DATETIME)
					|| !wechselZeit.isAfter(SystemKalender.MIN_DATETIME)) {
				return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, true);
			}

			letzteWechselZeit = wechselZeit;
			
			for (Entry<KalenderEintragImpl, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
				if (entry.getKey().getZeitlicheGueltigkeit(wechselZeit).isZeitlichGueltig()) {
					wechselZeit = SystemKalender.MAX_DATETIME;
					break;
				}
			}
		} while (!wechselZeit.isBefore(SystemKalender.MAX_DATETIME));

		ZustandsWechsel result = null;
		for (Entry<KalenderEintragImpl, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
			ZustandsWechsel wechsel = entry.getKey().getZeitlicheGueltigkeit(wechselZeit).getNaechsterWechsel();
			if (wechsel.isWirdGueltig()) {
				if( result == null) {
					result = wechsel;
				} else if (wechsel.getZeitPunkt().isBefore(result.getZeitPunkt())) {
					result = wechsel;
				}
			}
		}
		
		if( result == null) {
			return ZustandsWechsel.of(SystemKalender.MIN_DATETIME, true);
		}
		
		return result;
	}
}

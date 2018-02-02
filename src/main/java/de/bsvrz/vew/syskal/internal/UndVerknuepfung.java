/*
 * Rahmenwerk-Plug-in "Systemkalender"
 * Copyright (C) 2009 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weissenfelser Strasse 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.vew.syskal.internal;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.bsvrz.vew.syskal.Gueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentation einer logischen Verknüpfung mehrere Systemkalendereinträge mit
 * UND.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class UndVerknuepfung extends LogischerVerkuepfungsEintrag {

	/**
	 * Konstruktor.
	 * 
	 * @param provider
	 *            die Verwaltung aller bekannten Systemkalendereinträge zur
	 *            Verifizierung von Referenzen
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            der definierende String des Eintrags
	 */
	public UndVerknuepfung(KalenderEintragProvider provider, final String name, final String definition) {
		super(provider, name, definition);
	}

	@Override
	public String getVerknuepfungsArt() {
		return "UND";
	}

	@Override
	public Gueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitPunkt) {

		boolean zustand = true;
		Map<KalenderEintrag, ZustandsWechsel> potentielleEndWechsel = new LinkedHashMap<>();

		for (Verweis verweis : getVerweise()) {
			KalenderEintrag eintrag = verweis.getReferenzEintrag();
			if ((eintrag == null) || eintrag.isFehler()) {
				return GueltigkeitImpl.NICHT_GUELTIG;
			}

			Gueltigkeit gueltigKeit = eintrag.getZeitlicheGueltigkeit(zeitPunkt);
			if (!gueltigKeit.isZeitlichGueltig()) {
				zustand = false;
			}
			potentielleEndWechsel.put(eintrag, gueltigKeit.getNaechsterWechsel());
		}

		ZustandsWechsel wechsel = berechneNaechstenWechselAuf(!zustand, potentielleEndWechsel);

		return GueltigkeitImpl.of(zustand, wechsel);
	}

	private ZustandsWechsel berechneNaechstenWechselAuf(boolean zielZustand,
			Map<KalenderEintrag, ZustandsWechsel> potentielleWechsel) {

		ZustandsWechsel result = null;

		if (zielZustand) {

			Map<KalenderEintrag, ZustandsWechsel> zustandsWechsel = new LinkedHashMap<>(potentielleWechsel);

			do {

				Entry<KalenderEintrag, ZustandsWechsel> fruehesterVerweis = null;

				for (Entry<KalenderEintrag, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
					if (!entry.getValue().isWirdGueltig()) {
						continue;
					}
					if (fruehesterVerweis == null) {
						fruehesterVerweis = entry;
					} else if (entry.getValue().getZeitPunkt().isBefore(fruehesterVerweis.getValue().getZeitPunkt())) {
						fruehesterVerweis = entry;
					}
				}

				if (fruehesterVerweis == null) {
					return ZustandsWechselImpl.MAX;
				}

				LocalDateTime moeglicherZeitPunkt = fruehesterVerweis.getValue().getZeitPunkt();
				for (KalenderEintrag eintrag : zustandsWechsel.keySet()) {
					if (eintrag.getZeitlicheGueltigkeit(moeglicherZeitPunkt).isZeitlichGueltig()) {
						if (result == null) {
							result = ZustandsWechselImpl.of(moeglicherZeitPunkt, true);
						}
					} else {
						result = null;
						break;
					}
				}

				if (result == null) {
					Map<KalenderEintrag, ZustandsWechsel> korrigierteZustandsWechsel = new LinkedHashMap<>();
					for (Entry<KalenderEintrag, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
						ZustandsWechsel wechsel = entry.getValue();
						if (!wechsel.getZeitPunkt().isAfter(moeglicherZeitPunkt)) {
							do {
								wechsel = entry.getKey().getZeitlicheGueltigkeit(wechsel.getZeitPunkt())
										.getNaechsterWechsel();
							} while (wechsel.isWirdGueltig() != zielZustand && !wechsel.equals(ZustandsWechselImpl.MAX));
						}
						korrigierteZustandsWechsel.put(entry.getKey(), wechsel);
					}
					zustandsWechsel.clear();
					zustandsWechsel.putAll(korrigierteZustandsWechsel);
				}
			} while (result == null);
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
		return result;
	}
}

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

package de.bsvrz.vew.syskal.syskal.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
	public Gueltigkeit getGueltigKeit(LocalDateTime zeitpunkt) {

		ZustandsWechsel beginn = null;
		boolean zustand = true;
		Map<KalenderEintrag, ZustandsWechsel> potentielleWechsel = new LinkedHashMap<>();

		for (Verweis verweis : getVerweise()) {
			KalenderEintrag eintrag = verweis.getReferenzEintrag();
			if (eintrag == null) {
				return Gueltigkeit.NICHT_GUELTIG;
			}

			Gueltigkeit gueltigKeit = eintrag.getGueltigKeit(zeitpunkt);
			if (beginn == null) {
				beginn = gueltigKeit.getBeginn();
				zustand = zustand & beginn.isWirdGueltig();
			} else if (beginn.getZeitPunkt().isBefore(gueltigKeit.getBeginn().getZeitPunkt())) {
				beginn = gueltigKeit.getBeginn();
				zustand = zustand & beginn.isWirdGueltig();
			}

			potentielleWechsel.put(eintrag, gueltigKeit.getNaechsteAenderung());
		}

		ZustandsWechsel ende = berechneNaechstenWechselAuf(!zustand, potentielleWechsel);

		return Gueltigkeit.of(beginn, ende);
	}

	private ZustandsWechsel berechneNaechstenWechselAuf(boolean zielZustand,
			Map<KalenderEintrag, ZustandsWechsel> potentielleWechsel) {

		ZustandsWechsel result = null;

		if (zielZustand) {
			Map<KalenderEintrag, ZustandsWechsel> zustandsWechsel = new LinkedHashMap<>(potentielleWechsel);

			do {
				Map<KalenderEintrag, ZustandsWechsel> korrigierteZustandsWechsel = new LinkedHashMap<>();
				for (Entry<KalenderEintrag, ZustandsWechsel> entry : zustandsWechsel.entrySet()) {
					if (entry.getValue().isWirdGueltig() == zielZustand) {
						korrigierteZustandsWechsel.put(entry.getKey(), entry.getValue());
					} else {
						ZustandsWechsel wechsel = entry.getKey().nachsterZustandswechselNach(
								entry.getValue().getZeitPunkt(), entry.getValue().getZeitPunkt().plusYears(10),
								zielZustand);
						if (wechsel != null) {
							korrigierteZustandsWechsel.put(entry.getKey(), wechsel);
						}
					}
				}

				Optional<ZustandsWechsel> first = korrigierteZustandsWechsel.values().stream()
						.sorted(new Comparator<ZustandsWechsel>() {

							@Override
							public int compare(ZustandsWechsel o1, ZustandsWechsel o2) {
								return o2.getZeitPunkt().compareTo(o1.getZeitPunkt());
							}
						}).findFirst();

				if (!first.isPresent()) {
					return ZustandsWechsel.MAX;
				}

				result = first.get();
				for (Entry<KalenderEintrag, ZustandsWechsel> entry : korrigierteZustandsWechsel.entrySet()) {
					KalenderEintrag eintrag = entry.getKey();
					if (entry.getValue().getZeitPunkt().isBefore(result.getZeitPunkt())) {
						zustandsWechsel.put(eintrag, eintrag.nachsterZustandswechselNach(result.getZeitPunkt(),
								result.getZeitPunkt().plusYears(10), zielZustand));
					}
					if (eintrag.isGueltig(result.getZeitPunkt()) != zielZustand) {
						result = null;
					}
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

	@Override
	public List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {
		// TODO Auto-generated method stub
		return Collections.emptyList();

	}
}

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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.vew.syskal.Gueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentation der Daten eines {@link KalenderEintrag}, der durch einen
 * Zeitbereich definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitBereichsEintrag extends KalenderEintrag {

	private LocalDateTime start;
	private LocalDateTime ende;

	/** das Datumsformat mit Zeitangabe. */
	private static String formatMitZeit = "dd.MM.yyyy HH:mm:ss,SSS";

	/** verkürztes Datumsformat für 0 Uhr. */
	private static String formatOhneZeit = "dd.MM.yyyy";

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            der definierende Textstring
	 */
	public ZeitBereichsEintrag(final String name, final String definition) {
		super(name, definition);
		if (definition == null) {
			start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
			ende = start.plusDays(1);
		} else {
			if (definition.contains("-")) {
				final String[] parts = definition.split("-");
				try {
					start = parseDatum(parts[0]);
					ende = parseDatum(parts[1]);
				} catch (final ParseException e) {
					setFehler(true);
				}
			} else {
				if (definition.length() > 0) {
					setFehler(true);
				}
				start = LocalDateTime.MIN;
				ende = LocalDateTime.MAX;
			}
		}
	}

	@Override
	public EintragsArt getEintragsArt() {
		return EintragsArt.DATUMSBEREICH;
	}

	/**
	 * liefert den Endzeitpunkt in Millisekunden.
	 * 
	 * @return den Zeitpunkt
	 */
	public LocalDateTime getEnde() {
		return ende;
	}

	/**
	 * liefert den Anfangszeitpunkt in Millisekunden.
	 * 
	 * @return den Zeitpunkt
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * liefert die zugewiesenen Zeitgrenzen als Textstring.
	 * 
	 * @return den Text
	 */
	String getZeitBereicheAlsString() {
		final StringBuffer buffer = new StringBuffer();

		if (getZeitGrenzen().size() > 0) {
			buffer.append('(');
			for (final ZeitGrenze bereich : getZeitGrenzen()) {
				buffer.append(bereich.toString());
			}
			buffer.append(')');
		}

		return buffer.toString();
	}

	/**
	 * interpretiert den übergebenen Text als Datumsangabe mit optionaler Zeit.
	 * 
	 * @param string
	 *            der Textstring
	 * @return die Zeit in Millisekunden
	 * @throws ParseException
	 *             der Text konnte nicht als Zeit interpretiert werden
	 */
	private LocalDateTime parseDatum(final String string) throws ParseException {

		final String[] parts = string.split("[.:, ]");
		if (parts.length < 3) {
			throw new ParseException("Text kann nicht als Zeiteintrag interpretiert werden", 0);
		}

		int tag = 1;
		int monat = 1;
		int jahr = 1970;
		int stunde = 0;
		int minute = 0;
		int sekunde = 0;
		int milliSekunde = 0;

		for (int idx = 0; idx < 7; idx++) {
			int value;
			if (parts.length > idx) {
				try {
					value = Integer.parseInt(parts[idx].trim());
				} catch (final NumberFormatException e) {
					throw new ParseException("Text kann nicht als Zeiteintrag interpretiert werden", 0);
				}
			} else {
				value = 0;
			}
			switch (idx) {
			case 0:
				tag = value;
				break;
			case 1:
				monat = value;
				break;
			case 2:
				jahr = value;
				break;
			case 3:
				stunde = value;
				break;
			case 4:
				minute = value;
				break;
			case 5:
				sekunde = value;
				break;
			case 6:
				milliSekunde = value;
				break;
			default:
				break;
			}
		}

		return LocalDateTime.of(jahr, monat, tag, stunde, minute, sekunde).plusNanos(milliSekunde * 1000);
	}

	@Override
	public String toString() {

		final StringBuffer buffer = new StringBuffer(getName());
		buffer.append(":=");
		if (start.isAfter(LocalDateTime.MIN) && ende.isBefore(LocalDateTime.MAX)) {
			buffer.append('<');
			start.format(verwendetesFormat(start));
			buffer.append(start.format(verwendetesFormat(start)));
			buffer.append('-');
			buffer.append(ende.format(verwendetesFormat(ende)));
			buffer.append('>');
		}

		buffer.append(getZeitBereicheAlsString());

		return buffer.toString();
	}

	/**
	 * ermittelt das Format für die Ausgabe des Datums. Wenn Stunde, Minute und
	 * Sekunde den Wert 0 haben, wird das verkürzte Format verwendet.
	 * 
	 * @param kalender
	 *            die auszugebende Zeit
	 * @return das Format
	 */
	private DateTimeFormatter verwendetesFormat(final LocalDateTime zeitstempel) {
		if (zeitstempel.getHour() == 0) {
			if (zeitstempel.getMinute() == 0) {
				if (zeitstempel.getSecond() == 0) {
					return DateTimeFormatter.ofPattern(ZeitBereichsEintrag.formatOhneZeit);
				}
			}
		}
		return DateTimeFormatter.ofPattern(ZeitBereichsEintrag.formatMitZeit);
	}

	@Override
	public Gueltigkeit isZeitlichGueltig(LocalDateTime zeitpunkt) {

		LocalDate startDate;

		if (zeitpunkt.isAfter(ende)) {
			return Gueltigkeit.NICHT_GUELTIG;
		}

		List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();

		if (zeitGrenzen.isEmpty()) {
			if (zeitpunkt.isBefore(start)) {
				return Gueltigkeit.of(false, ZustandsWechsel.of(start, true));
			}
			return Gueltigkeit.of(true, ZustandsWechsel.of(ende, false));
		}
		
		LocalDate datum = zeitpunkt.toLocalDate();
		LocalTime abfrageZeit = zeitpunkt.toLocalTime();

		ZeitGrenze letzteGrenze = null;
		for (ZeitGrenze grenze : zeitGrenzen) {
			
			if (abfrageZeit.equals(grenze.getStart())) {
				return Gueltigkeit.of(true, ZustandsWechsel.of(LocalDateTime.of(datum, grenze.getEnde()), false));
			}
			if (abfrageZeit.isBefore(grenze.getStart())) {
				return Gueltigkeit.of(false, ZustandsWechsel.of(LocalDateTime.of(datum, grenze.getStart()), true));
			}

			if (abfrageZeit.isBefore(grenze.getEnde())) {
				return Gueltigkeit.of(true, ZustandsWechsel.of(LocalDateTime.of(datum, grenze.getEnde()), false));
			}

			letzteGrenze = grenze;
		}

		LocalDateTime wechselZeit = LocalDateTime.of(zeitpunkt.toLocalDate().plusDays(1),
				zeitGrenzen.get(0).getStart());
		if (ende != null && wechselZeit.isAfter(ende)) {
			return Gueltigkeit.NICHT_GUELTIG;
		}
		return Gueltigkeit.of(false, ZustandsWechsel.of(wechselZeit, true));
	}

	@Override
	public List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {

		if (ende.isBefore(this.start) || start.isAfter(this.ende)) {
			return Collections.emptyList();
		}

		List<ZustandsWechsel> result = new ArrayList<>();

		List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
		if (zeitGrenzen.isEmpty()) {
			if (!start.isBefore(this.start)) {
				result.add(ZustandsWechsel.of(start, true));
				if (ende.isEqual(this.ende) || ende.isBefore(this.ende)) {
					result.add(ZustandsWechsel.of(this.ende, false));
				}
				return result;
			}

			result.add(ZustandsWechsel.of(start, true));
			if (!ende.isAfter(this.ende)) {
				result.add(ZustandsWechsel.of(this.ende, false));
			}
			return result;
		}

		LocalDateTime currentTime = start;

		boolean firstDay = true;
		while (!currentTime.isAfter(ende)) {
			for (ZeitGrenze grenze : zeitGrenzen) {
				if (firstDay) {
					if (currentTime.toLocalTime().isBefore(grenze.getStart())) {
						if (currentTime.toLocalTime().isBefore(grenze.getEnde())) {
							result.add(ZustandsWechsel.of(currentTime, false));
							firstDay = false;
						}
					} else if (currentTime.toLocalTime().isBefore(grenze.getEnde())) {
						result.add(ZustandsWechsel.of(currentTime, true));
						if (!LocalDateTime.of(currentTime.toLocalDate(), grenze.getEnde()).isAfter(ende)) {
							result.add(ZustandsWechsel.of(LocalDateTime.of(currentTime.toLocalDate(), grenze.getEnde()),
									false));
						}
						firstDay = false;
						continue;
					} else {
						continue;
					}
				}

				if (LocalDateTime.of(currentTime.toLocalDate(), grenze.getStart()).isBefore(ende)) {
					result.add(
							ZustandsWechsel.of(LocalDateTime.of(currentTime.toLocalDate(), grenze.getStart()), true));
				}
				if (!LocalDateTime.of(currentTime.toLocalDate(), grenze.getEnde()).isAfter(ende)) {
					result.add(
							ZustandsWechsel.of(LocalDateTime.of(currentTime.toLocalDate(), grenze.getEnde()), false));
				}
			}
			currentTime = LocalDateTime.of(currentTime.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
		}

		return result;
	}
}

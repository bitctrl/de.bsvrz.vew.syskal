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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Repräsentation der Daten eines {@link KalenderEintragDefinition}, der durch
 * einen Zeitbereich definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitBereichsEintrag extends KalenderEintragDefinition {

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
	public Gueltigkeit getGueltigKeit(LocalDateTime zeitpunkt) {

		LocalDate startDate;

		if (zeitpunkt.isAfter(ende)) {
			return Gueltigkeit.NICHT_GUELTIG;
		}

		List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();

		if (zeitGrenzen.isEmpty()) {
			if (zeitpunkt.isBefore(start)) {
				return Gueltigkeit.of(ZustandsWechsel.of(LocalDateTime.MIN, false), ZustandsWechsel.of(start, true));
			}
			return Gueltigkeit.of(ZustandsWechsel.of(start, true), ZustandsWechsel.of(ende, false));
		}

		LocalDate datum = zeitpunkt.toLocalDate();
		LocalTime abfrageZeit = zeitpunkt.toLocalTime();

		ZeitGrenze letzteGrenze = null;
		for (ZeitGrenze grenze : zeitGrenzen) {
			if (abfrageZeit.equals(grenze.getStart())) {
				return Gueltigkeit.of(ZustandsWechsel.of(LocalDateTime.of(datum, abfrageZeit), true),
						ZustandsWechsel.of(LocalDateTime.of(datum, grenze.getEnde()), false));
			}
			if (abfrageZeit.isBefore(grenze.getStart())) {
				if (letzteGrenze == null) {
					return Gueltigkeit.of(
							ZustandsWechsel.of(LocalDateTime.of(datum.minusDays(1),
									zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde()), false),
							ZustandsWechsel.of(LocalDateTime.of(datum, grenze.getStart()), true));
				}
				return Gueltigkeit.of(ZustandsWechsel.of(LocalDateTime.of(datum, letzteGrenze.getEnde()), false),
						ZustandsWechsel.of(LocalDateTime.of(datum, grenze.getStart()), true));
			}
			letzteGrenze = grenze;
		}

		return Gueltigkeit.of(ZustandsWechsel.of(LocalDateTime.of(datum, letzteGrenze.getEnde()), false),
				ZustandsWechsel.of(LocalDateTime.of(datum.plusDays(1), zeitGrenzen.get(0).getStart()), true));
	}

	@Override
	public List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {

		if (ende.isBefore(this.start) || start.isAfter(this.ende)) {
			return Collections.emptyList();
		}

		List<ZustandsWechsel> result = new ArrayList<>();

		LocalDate startDate = start.toLocalDate();
		LocalTime startZeit = start.toLocalTime();

		List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
		if (zeitGrenzen.isEmpty()) {
			if (start.isEqual(this.start) || start.isAfter(this.start)) {
				result.add(ZustandsWechsel.of(start, true));
				if (ende.isEqual(this.ende) || ende.isBefore(this.ende)) {
					result.add(ZustandsWechsel.of(this.ende, false));
				}
				return result;
			} else
				result.add(ZustandsWechsel.of(start, true));
			if (ende.isEqual(this.ende) || ende.isBefore(this.ende)) {
				result.add(ZustandsWechsel.of(this.ende, false));
			}
			return result;

		}

		// TODO Auto-generated method stub
		return Collections.emptyList();
	}
}

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

import java.text.ParseException;
import java.time.LocalTime;

/**
 * Repräsentation einer Zeitbegrenzung für einen Systemkalendereintrag.
 * Zeitgrenzen können mit aktuellen Stand nur zu Systemkalendereinträgen
 * hinzugefügt werden, die durch einen Datumsbereich definiert sind.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitGrenze implements Comparable<ZeitGrenze> {

	/** der Anfang der Zeitgrenze in Millisekunden innerhalb eines Tages. */
	private final LocalTime start;

	/** das Ende der Zeitgrenze in Millisekunden innerhalb eines Tages. */
	private final LocalTime ende;

	/**
	 * Konstruktor, der ein Zeitgrenzenobjekt aus den übergebenen Grenzen in
	 * Millisekunden innerhalb eines Tages erzeugt.
	 * 
	 * @param start
	 *            der Anfang des Grenzbereiches
	 * @param ende
	 *            das Ende des Grenzbereiches
	 */
	public ZeitGrenze(final LocalTime start, final LocalTime ende) {
		super();
		this.start = start;
		this.ende = ende;
	}

	/**
	 * Konstruktor, der den Zeitgrenzenbereich aus dem übergebenen
	 * Definitionsstring ermittelt.
	 * 
	 * @param zb
	 *            der Definitionsstring
	 * @throws ParseException
	 *             der String konnte nicht interpretiert werden
	 */
	public ZeitGrenze(final String zb) throws ParseException {
		final String[] parts = zb.split("-");
		if (parts.length < 2) {
			throw new ParseException("Der String \"" + zb
					+ "\" kann nicht als Zeitgrenzen interpretiert werden!", 0);
		}
		start = parseZeit(parts[0]);
		ende = parseZeit(parts[1]);
	}

	@Override
	public int compareTo(final ZeitGrenze o) {
		int result = start.compareTo(o.start);
		if (result == 0) {
			result = ende.compareTo(o.ende);
		}
		return result;
	}

	/**
	 * liefert den Endzeitpunkt in Millisekunden innerhalb eines Tages.
	 * 
	 * @return den Wert
	 */
	public LocalTime getEnde() {
		return ende;
	}

	/**
	 * liefert den Endzeitpunkt als Zeichenkette entsprechend dem erwarteten
	 * Datumsformat.
	 * 
	 * @return den Wert als Zeichenkette
	 */
	public String getEndeAlsString() {
		return longToStr(ende);
	}

	/**
	 * liefert den Startzeitpunkt in Millisekunden innerhalb eines Tages.
	 * 
	 * @return den Wert
	 */
	public LocalTime getStart() {
		return start;
	}

	/**
	 * liefert den Startzeitpunkt als Zeichenkette entsprechend dem erwarteten
	 * Datumsformat.
	 * 
	 * @return den Wert als Zeichenkette
	 */
	public String getStartAlsString() {
		return longToStr(start);
	}

	/**
	 * wandelt einen Zeitwert (Millisekunden innerhalb eines Tages) in eine
	 * Zeichenkette um.
	 * 
	 * @param value
	 *            der Wert
	 * @return die Zeichenkette
	 */
	private String longToStr(final LocalTime value) {

		final StringBuilder buffer = new StringBuilder(20);

		buffer.append(String.format("%02d", value.getHour()));
		buffer.append(':');

		buffer.append(String.format("%02d", value.getMinute()));
		buffer.append(':');

		buffer.append(String.format("%02d", value.getSecond()));
		buffer.append(',');

 		buffer.append(String.format("%03d", value.getNano() / 1000));
		return buffer.toString();
	}

	/**
	 * interpretiert den übergebenen Text als Zeitdefinition in Millisekunden.
	 * Das erwartete Format is HH:MM:SS,sss.
	 * 
	 * @param string
	 *            der Definitionstext
	 * @return die Zeit in Millisekunden
	 * @throws ParseException
	 *             der Text kann nicht als Zeiteintrag interpretiert werden
	 */
	private LocalTime parseZeit(final String string) throws ParseException {

		final String[] parts = string.split("[:,]");

		if (parts.length <= 0) {
			throw new ParseException("Der String \"" + string
					+ "\" kann nicht als Zeit interpretiert werden!", 0);
		}

		int stunde = 0;
		int minute = 0;
		int sekunde = 0;
		int milliSekunde = 0;
		for (int idx = 0; idx < 7; idx++) {
			int value;
			if (parts.length > idx) {
				value = Integer.parseInt(parts[idx].trim());
			} else {
				value = 0;
			}
			switch (idx) {
			case 0:
				stunde = value;
				break;
			case 1:
				minute = value;
				break;
			case 2:
				sekunde = value;
				break;
			case 3:
				milliSekunde = value;
				break;
			default:
				break;
			}
		}

		return LocalTime.of(stunde, minute, sekunde, milliSekunde * 1000);
	}

	@Override
	public String toString() {
		final StringBuffer result = new StringBuffer("{");
		result.append(longToStr(start));
		result.append('-');
		result.append(longToStr(ende));
		result.append('}');
		return result.toString();
	}
}

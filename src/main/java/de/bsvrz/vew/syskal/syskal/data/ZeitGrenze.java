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

/**
 * Repräsentation einer Zeitbegrenzung für einen Systemkalendereintrag.
 * Zeitgrenzen können mit aktuellen Stand nur zu Systemkalendereinträgen
 * hinzugefügt werden, die durch einen Datumsbereich definiert sind.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class ZeitGrenze implements Comparable<ZeitGrenze> {

	/** der Anfang der Zeitgrenze in Millisekunden innerhalb eines Tages. */
	private final long start;

	/** das Ende der Zeitgrenze in Millisekunden innerhalb eines Tages. */
	private final long ende;

	/**
	 * Konstruktor, der ein Zeitgrenzenobjekt aus den übergebenen Grenzen in
	 * Millisekunden innerhalb eines Tages erzeugt.
	 * 
	 * @param start
	 *            der Anfang des Grenzbereiches
	 * @param ende
	 *            das Ende des Grenzbereiches
	 */
	public ZeitGrenze(final long start, final long ende) {
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
		int result = Long.compare(start, o.start);
		if (result == 0) {
			result = Long.compare(ende, o.ende);
		}
		return result;
	}

	/**
	 * liefert den Endzeitpunkt in Millisekunden innerhalb eines Tages.
	 * 
	 * @return den Wert
	 */
	public long getEnde() {
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
	public long getStart() {
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
	private String longToStr(final long value) {
		long millis = value;
		final StringBuffer buffer = new StringBuffer();

		Long val = millis / (3600 * 1000);
		buffer.append(String.format("%02d", val));
		buffer.append(':');
		millis -= val * 3600 * 1000;

		val = millis / (60 * 1000);
		buffer.append(String.format("%02d", val));
		buffer.append(':');
		millis -= val * 60 * 1000;

		val = millis / 1000;
		buffer.append(String.format("%02d", val));
		buffer.append(',');
		millis -= val * 1000;
		buffer.append(String.format("%03d", millis));
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
	private long parseZeit(final String string) throws ParseException {
		long result = 0;
		final String[] parts = string.split("[:,]");

		if (parts.length <= 0) {
			throw new ParseException("Der String \"" + string
					+ "\" kann nicht als Zeit interpretiert werden!", 0);
		}

		for (int idx = 0; idx < 7; idx++) {
			int value;
			if (parts.length > idx) {
				value = Integer.parseInt(parts[idx].trim());
			} else {
				value = 0;
			}
			switch (idx) {
			case 0:
				result += 3600L * 1000L * value;
				break;
			case 1:
				result += 60L * 1000L * value;
				break;
			case 2:
				result += 1000L * value;
				break;
			case 3:
				result += value;
				break;
			default:
				break;
			}
		}

		return result;
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

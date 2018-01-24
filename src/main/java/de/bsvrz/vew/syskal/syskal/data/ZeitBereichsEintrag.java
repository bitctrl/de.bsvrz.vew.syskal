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
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Repräsentation der Daten eines {@link KalenderEintrag}, der durch einen
 * Zeitbereich definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitBereichsEintrag extends KalenderEintrag {

	/** der Anfangszeitpunkt in Millisekunden seit 1.1.1970 GMT. */
	private long start;

	/** der Endzeitpunkt in Millisekunden seit 1.1.1970 GMT. */
	private long ende;

	/** das Datumsformat mit Zeitangabe. */
	private static DateFormat formatMitZeit = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss,SSS");

	/** verkürztes Datumsformat für 0 Uhr. */
	private static DateFormat formatOhneZeit = new SimpleDateFormat(
			"dd.MM.yyyy");

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
			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			start = cal.getTimeInMillis();
			ende = start + (24 * 3600 * 1000);
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
				start = 0L;
				ende = 0L;
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
	public long getEnde() {
		return ende;
	}

	/**
	 * liefert den Anfangszeitpunkt in Millisekunden.
	 * 
	 * @return den Zeitpunkt
	 */
	public long getStart() {
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
	private long parseDatum(final String string) throws ParseException {
		final String[] parts = string.split("[.:, ]");
		final Calendar calender = Calendar.getInstance();

		if (parts.length < 3) {
			throw new ParseException(
					"Text kann nicht als Zeiteintrag interpretiert werden", 0);
		}

		for (int idx = 0; idx < 7; idx++) {
			int value;
			if (parts.length > idx) {
				try {
					value = Integer.parseInt(parts[idx].trim());
				} catch (final NumberFormatException e) {
					throw new ParseException(
							"Text kann nicht als Zeiteintrag interpretiert werden",
							0);
				}
			} else {
				value = 0;
			}
			switch (idx) {
			case 0:
				calender.set(Calendar.DAY_OF_MONTH, value);
				break;
			case 1:
				calender.set(Calendar.MONTH, value - 1);
				break;
			case 2:
				calender.set(Calendar.YEAR, value);
				break;
			case 3:
				calender.set(Calendar.HOUR_OF_DAY, value);
				break;
			case 4:
				calender.set(Calendar.MINUTE, value);
				break;
			case 5:
				calender.set(Calendar.SECOND, value);
				break;
			case 6:
				calender.set(Calendar.MILLISECOND, value);
				break;
			default:
				break;
			}
		}

		return calender.getTimeInMillis();
	}

	/**
	 * setzt den Endezeitpunkt des Eintrags.
	 * 
	 * @param ende
	 *            der Zeitpunkt in Millisekunden
	 */
	public void setEnde(final long ende) {
		this.ende = ende;
	}

	/**
	 * setzt den Anfangszeitpunkt des Eintrags.
	 * 
	 * @param start
	 *            der Zeitpunkt in Millisekunden
	 */
	public void setStart(final long start) {
		this.start = start;
	}

	@Override
	public String toString() {

		final Calendar startCal = Calendar.getInstance();
		startCal.setTimeInMillis(start);
		final Calendar endCal = Calendar.getInstance();
		endCal.setTimeInMillis(ende);

		final StringBuffer buffer = new StringBuffer(getName());
		buffer.append(":=");
		if ((start > 0) && (ende > 0)) {
			buffer.append('<');
			buffer.append(verwendetesFormat(startCal)
					.format(startCal.getTime()));
			buffer.append('-');
			buffer.append(verwendetesFormat(endCal).format(endCal.getTime()));
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
	private DateFormat verwendetesFormat(final Calendar kalender) {
		DateFormat format = ZeitBereichsEintrag.formatMitZeit;
		if (kalender.get(Calendar.HOUR_OF_DAY) == 0) {
			if (kalender.get(Calendar.MINUTE) == 0) {
				if (kalender.get(Calendar.SECOND) == 0) {
					format = ZeitBereichsEintrag.formatOhneZeit;
				}
			}
		}
		return format;
	}

	@Override
	public Gueltigkeit getGueltigKeit(LocalDateTime zeitpunkt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ZustandsWechsel getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {
		// TODO Auto-generated method stub
		return null;
	}
}

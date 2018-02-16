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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemKalenderEintrag;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentation der Daten eines {@link KalenderEintragImpl}, der durch einen
 * Zeitbereich definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitBereichsEintrag extends KalenderEintragImpl {

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
					ende = ende.plusDays(1);
				} catch (final ParseException e) {
					setFehler(true);
				}
			} else {
				if (definition.length() > 0) {
					setFehler(true);
				}
				start = SystemKalender.MIN_DATETIME;
				ende = SystemKalender.MAX_DATETIME;
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
		if (start.isAfter(SystemKalender.MIN_DATETIME) && ende.isBefore(SystemKalender.MAX_DATETIME)) {
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
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {

		List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();
		if (zeitGrenzen.isEmpty()) {
			return berechneZeitlicheGueltigkeitFuerReinenZeitbereich(zeitpunkt);
		}

		if (zeitpunkt.isBefore(start)) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(SystemKalender.MIN_DATETIME),
					ZustandsWechsel.zuGueltig(sucheFruehestMoeglichenIntervallStart()));
		}

		if (!zeitpunkt.isBefore(ende)) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(sucheSpaetestMoeglichesIntervallEnde()),
					ZustandsWechsel.MAX);
		}

		LocalDate datum = zeitpunkt.toLocalDate();
		LocalTime abfrageZeit = zeitpunkt.toLocalTime();

		ZeitGrenze letzteGrenze = null;

		for (ZeitGrenze grenze : zeitGrenzen) {

			if (abfrageZeit.equals(grenze.getStart())) {
				return SystemkalenderGueltigkeit.of(
						ZustandsWechsel.zuGueltig(LocalDateTime.of(datum, grenze.getStart())),
						ZustandsWechsel.zuUnGueltig(LocalDateTime.of(datum, grenze.getEnde())));
			}
			if (abfrageZeit.isBefore(grenze.getStart())) {
				LocalDateTime aktivierungsZeit;
				if (letzteGrenze != null) {
					aktivierungsZeit = LocalDateTime.of(datum, letzteGrenze.getEnde());
				} else {
					aktivierungsZeit = LocalDateTime.of(datum.minusDays(1),
							zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
				}

				if (aktivierungsZeit.isBefore(start)) {
					aktivierungsZeit = SystemKalender.MIN_DATETIME;
				}
				return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(aktivierungsZeit),
						ZustandsWechsel.zuGueltig(LocalDateTime.of(datum, grenze.getStart())));
			}

			if (abfrageZeit.isBefore(grenze.getEnde())) {
				return SystemkalenderGueltigkeit.of(
						ZustandsWechsel.zuGueltig(LocalDateTime.of(datum, grenze.getStart())),
						ZustandsWechsel.zuUnGueltig(LocalDateTime.of(datum, grenze.getEnde())));
			}

			letzteGrenze = grenze;
		}

		LocalDateTime aktivierungsZeit = LocalDateTime.of(zeitpunkt.toLocalDate(),
				zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
		LocalDateTime wechselZeit = LocalDateTime.of(zeitpunkt.toLocalDate().plusDays(1),
				zeitGrenzen.get(0).getStart());
		if (ende != null && wechselZeit.isAfter(ende)) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}
		return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(aktivierungsZeit),
				ZustandsWechsel.zuGueltig(wechselZeit));
	}

	private LocalDateTime sucheSpaetestMoeglichesIntervallEnde() {

		LocalDate endeDatum = ende.toLocalDate();
		LocalDateTime result = null;

		do {
			for (ZeitGrenze grenze : getZeitGrenzen()) {
				LocalDateTime pruefZeit = LocalDateTime.of(endeDatum, grenze.getEnde());
				if (pruefZeit.isBefore(ende)) {
					result = pruefZeit;
				} else {
					break;
				}
			}

			endeDatum = endeDatum.minusDays(1);
		} while (result == null);

		return result;
	}

	private LocalDateTime sucheFruehestMoeglichenIntervallStart() {

		LocalDate startDatum = start.toLocalDate();
		LocalDateTime result = null;

		do {
			for (ZeitGrenze grenze : getZeitGrenzen()) {
				result = LocalDateTime.of(startDatum, grenze.getStart());
				if (result.isBefore(start)) {
					result = null;
				} else {
					break;
				}
			}

			startDatum = startDatum.plusDays(1);
		} while (result == null);

		return result;
	}

	private SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitFuerReinenZeitbereich(LocalDateTime zeitpunkt) {

		if (zeitpunkt.isBefore(start)) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(SystemKalender.MIN_DATETIME),
					ZustandsWechsel.zuGueltig(start));
		}

		if (!zeitpunkt.isBefore(ende)) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(ende), ZustandsWechsel.MAX);
		}

		return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuGueltig(start), ZustandsWechsel.zuUnGueltig(ende));
	}

	@Override
	protected SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitpunkt) {

		List<ZeitGrenze> zeitGrenzen = getZeitGrenzen();

		if (zeitpunkt.isBefore(start)) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		if (zeitpunkt.isBefore(ende) && zeitGrenzen.isEmpty()) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.MIN, ZustandsWechsel.zuGueltig(start));
		}

		LocalDate datum = zeitpunkt.toLocalDate();
		LocalTime abfrageZeit = zeitpunkt.toLocalTime();

		ZeitGrenze letzteGrenze = null;

		for (ZeitGrenze grenze : zeitGrenzen) {

			if (abfrageZeit.equals(grenze.getStart())) {
				LocalDateTime aktivierungsZeit;
				if (letzteGrenze != null) {
					aktivierungsZeit = LocalDateTime.of(datum, letzteGrenze.getEnde());
				} else {
					aktivierungsZeit = LocalDateTime.of(datum.minusDays(1),
							zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
				}
				if (aktivierungsZeit.isBefore(start)) {
					aktivierungsZeit = SystemKalender.MIN_DATETIME;
				}
				LocalDateTime wechselZeit = LocalDateTime.of(datum, grenze.getStart());
				if (wechselZeit.isBefore(start)) {
					return SystemkalenderGueltigkeit.NICHT_GUELTIG;
				}
				return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(aktivierungsZeit),
						ZustandsWechsel.zuGueltig(wechselZeit));
			}

			if (abfrageZeit.isBefore(grenze.getStart())) {
				LocalDateTime wechselZeit;
				if (letzteGrenze != null) {
					wechselZeit = LocalDateTime.of(datum, letzteGrenze.getEnde());
				} else {
					wechselZeit = LocalDateTime.of(datum.minusDays(1),
							zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
				}
				LocalDateTime aktivierungsZeit;
				if (letzteGrenze != null) {
					aktivierungsZeit = LocalDateTime.of(datum, letzteGrenze.getStart());
				} else {
					aktivierungsZeit = LocalDateTime.of(datum.minusDays(1),
							zeitGrenzen.get(zeitGrenzen.size() - 1).getStart());
				}
				return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuGueltig(aktivierungsZeit),
						ZustandsWechsel.zuUnGueltig(wechselZeit));
			}

			if (abfrageZeit.isBefore(grenze.getEnde())) {
				LocalDateTime aktivierungsZeit;
				if (letzteGrenze != null) {
					aktivierungsZeit = LocalDateTime.of(datum, letzteGrenze.getEnde());
				} else {
					aktivierungsZeit = LocalDateTime.of(datum.minusDays(1),
							zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
				}
				return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuUnGueltig(aktivierungsZeit),
						ZustandsWechsel.zuGueltig(LocalDateTime.of(datum, grenze.getStart())));
			}

			letzteGrenze = grenze;
		}

		LocalDateTime aktivierungsZeit = SystemKalender.MIN_DATETIME;
		LocalDateTime wechselZeit = SystemKalender.MAX_DATETIME;
		
		if (abfrageZeit.isBefore(zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde())) {
			wechselZeit = LocalDateTime.of(zeitpunkt.toLocalDate().minusDays(1),
					zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
			aktivierungsZeit = LocalDateTime.of(zeitpunkt.toLocalDate().minusDays(1),
					zeitGrenzen.get(zeitGrenzen.size() - 1).getStart());
			if (ende != null && wechselZeit.isAfter(ende)) {
				return SystemkalenderGueltigkeit.NICHT_GUELTIG;
			}
		} else {
			wechselZeit = LocalDateTime.of(zeitpunkt.toLocalDate(),
					zeitGrenzen.get(zeitGrenzen.size() - 1).getEnde());
			aktivierungsZeit = LocalDateTime.of(zeitpunkt.toLocalDate(),
					zeitGrenzen.get(zeitGrenzen.size() - 1).getStart());
			if (ende != null && wechselZeit.isAfter(ende)) {
				return SystemkalenderGueltigkeit.NICHT_GUELTIG;
			}
		}
		
		return SystemkalenderGueltigkeit.of(ZustandsWechsel.zuGueltig(aktivierungsZeit),
				ZustandsWechsel.zuUnGueltig(wechselZeit));
	}

	@Override
	boolean benutzt(SystemKalenderEintrag referenz) {
		return false;
	}
}

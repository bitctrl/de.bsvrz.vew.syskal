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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;

import de.bsvrz.vew.syskal.Gueltigkeit;

/**
 * Spezielle Form eines Systemkalendereintrags für die Angabe eines konkreten
 * Datums. Der Eintrag wird in der Form <b><i>tag.monat.jahr,endjahr</i></b>
 * erwartet, wobei die Jahre durch ein Zeichen '*' als beliebig gekennzeichnet
 * werden können.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class DatumsEintrag extends KalenderEintrag  {

	/** das letzte Jahr für das der Eintrag gültig ist. */
	private int endJahr = Year.MAX_VALUE;

	/** das erste Jahr für das der Eintrag gültig ist. */
	private int jahr = Year.MIN_VALUE;

	/** der Monat des definierten Datums. */
	private int monat;

	/** der Tag innerhalb des Monats für das definierte Datum. */
	private int tag;

	/**
	 * Konstruktor, erzeugt einen Eintrag mit dem übergebenen Namen, der Inhalt wird
	 * durch den Definitionsstring beschrieben.
	 * 
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            die Definition des Eintrags als Zeichenkette
	 */
	public DatumsEintrag(final String name, final String definition) {
		super(name, definition);

		if (definition != null) {

			try {
				final String[] parts = definition.split(",");
				final String[] dateParts = parts[0].split("\\.");

				if (dateParts.length > 1) {
					tag = Integer.parseInt(dateParts[0].trim());
					monat = Integer.parseInt(dateParts[1].trim());
					if (dateParts.length > 2) {
						if (!"*".equalsIgnoreCase(dateParts[2].trim())) {
							jahr = Integer.parseInt(dateParts[2].trim());
						}
					}
				} else {
					setFehler(true);
				}

				if (parts.length > 1) {
					if (!"*".equalsIgnoreCase(parts[1].trim())) {
						endJahr = Integer.parseInt(parts[1].trim());
					}
				}
				
				if((tag == 29) && (monat == 2)) {
					while(!Year.isLeap(jahr)) {
						jahr++;
					}
					while(!Year.isLeap(endJahr)) {
						endJahr--;
					}
				}
				
			} catch (NumberFormatException e) {
				setFehler(true);
			}
		}
	}

	@Override
	public EintragsArt getEintragsArt() {
		return EintragsArt.NURDATUM;
	}

	public int getEndJahr() {
		return endJahr;
	}

	public int getJahr() {
		return jahr;
	}

	public int getMonat() {
		return monat;
	}

	public int getTag() {
		return tag;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(getName());
		buffer.append(":=");
		buffer.append(tag);
		buffer.append('.');
		buffer.append(monat);
		buffer.append('.');

		if (jahr == Year.MIN_VALUE) {
			buffer.append('*');
		} else {
			buffer.append(jahr);
		}
		buffer.append(',');
		if (endJahr == Year.MAX_VALUE) {
			buffer.append('*');
		} else {
			buffer.append(endJahr);
		}

		return buffer.toString();
	}

	@Override
	public Gueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {

		boolean gueltig = jahr <= zeitpunkt.getYear();
		gueltig &= endJahr >= zeitpunkt.getYear();
		gueltig &= monat == zeitpunkt.getMonthValue();
		gueltig &= tag == zeitpunkt.getDayOfMonth();

		if (gueltig) {
			return GueltigkeitImpl.of(gueltig, ZustandsWechselImpl
					.of(LocalDateTime.of(zeitpunkt.toLocalDate().plusDays(1), LocalTime.MIDNIGHT), !gueltig));
		}

		LocalDate fruehestesDatum = LocalDate.of(jahr, monat, tag);
		if (zeitpunkt.toLocalDate().isBefore(fruehestesDatum)) {
			return GueltigkeitImpl.of(gueltig,
					ZustandsWechselImpl.of(LocalDateTime.of(fruehestesDatum, LocalTime.MIDNIGHT), !gueltig));
		}

		
		
		LocalDate spaetestesDatum = LocalDate.of(endJahr, monat, tag).plusDays(1);
		if (zeitpunkt.toLocalDate().isBefore(spaetestesDatum)) {
			
			int checkJahr = zeitpunkt.getYear();
			if(!zeitpunkt.toLocalDate().withYear(2000).isBefore(spaetestesDatum.withYear(2000))) {
				checkJahr++;
			}
			
			if (tag == 29 && monat == 2) {
				while (!Year.isLeap(checkJahr)) {
					checkJahr++;
				}
			}

			LocalDateTime wechselZeit = LocalDateTime.of(LocalDate.of(checkJahr, monat, tag), LocalTime.MIDNIGHT);
			
			return GueltigkeitImpl.of(gueltig, ZustandsWechselImpl.of(
					wechselZeit, !gueltig));
		}

		return GueltigkeitImpl.of(gueltig, ZustandsWechselImpl.MAX);
	}
}

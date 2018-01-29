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

import java.security.GuardedObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Spezielle Form eines Systemkalendereintrags für die Angabe eines konkreten
 * Datums. Der Eintrag wird in der Form <b><i>tag.monat.jahr,endjahr</i></b>
 * erwartet, wobei die Jahre durch ein Zeichen '*' als beliebig gekennzeichnet
 * werden können.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class DatumsEintrag extends KalenderEintragDefinition {

	/** das letzte Jahr für das der Eintrag gültig ist. */
	private Integer endJahr;

	/** das erste Jahr für das der Eintrag gültig ist. */
	private Integer jahr;

	/** der Monat des definierten Datums. */
	private Integer monat;

	/** der Tag innerhalb des Monats für das definierte Datum. */
	private Integer tag;

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
				}

				if (parts.length > 1) {
					if (!"*".equalsIgnoreCase(parts[1].trim())) {
						endJahr = Integer.parseInt(parts[1].trim());
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

	/**
	 * liefert den Wert für das definierte Endjahr oder <code>null</code>, wenn
	 * keines festgelegt wurde.
	 * 
	 * @return das Jahr oder <code>null</code>
	 */
	public Integer getEndJahr() {
		return endJahr;
	}

	/**
	 * liefert den Wert für das definierte Anfangsjahr oder <code>null</code>, wenn
	 * keines festgelegt wurde.
	 * 
	 * @return das Jahr oder <code>null</code>
	 */
	public Integer getJahr() {
		return jahr;
	}

	/**
	 * liefert den Wert für den definierten Monat oder <code>null</code>, wenn
	 * keiner festgelegt wurde.
	 * 
	 * @return den Monat oder <code>null</code>
	 */
	public Integer getMonat() {
		return monat;
	}

	/**
	 * liefert den Wert für den definierten Tag oder <code>null</code>, wenn keiner
	 * festgelegt wurde.
	 * 
	 * @return den Tag das Monats oder <code>null</code>
	 */
	public Integer getTag() {
		return tag;
	}

	/**
	 * setzt den Wert für das Endjahr.
	 * 
	 * @param endJahr
	 *            das Jahr oder <code>null</code>, wenn es nicht beschränkt werden
	 *            soll.
	 */
	public void setEndJahr(final Integer endJahr) {
		this.endJahr = endJahr;
	}

	/**
	 * setzt den Wert für das Anfangsjahr.
	 * 
	 * @param jahr
	 *            das Jahr oder <code>null</code>, wenn es nicht beschränkt werden
	 *            soll.
	 */
	public void setJahr(final Integer jahr) {
		this.jahr = jahr;
	}

	/**
	 * setzt den Wert für den Monat.
	 * 
	 * @param monat
	 *            der Monat oder <code>null</code>, wenn er beliebig ist.
	 */
	public void setMonat(final Integer monat) {
		this.monat = monat;
	}

	/**
	 * setzt den Wert für den Tag des Monats.
	 * 
	 * @param tag
	 *            der Tag oder <code>null</code>, wenn er beliebig ist.
	 */
	public void setTag(final Integer tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(getName());
		buffer.append(":=");
		if ((tag == null) || (monat == null)) {
			buffer.append('*');
		} else {
			buffer.append(tag);
			buffer.append('.');
			buffer.append(monat);
			buffer.append('.');

			if (jahr == null) {
				buffer.append('*');
			} else {
				buffer.append(jahr);
			}
		}
		buffer.append(',');
		if (endJahr == null) {
			buffer.append('*');
		} else {
			buffer.append(endJahr);
		}

		return buffer.toString();
	}

	@Override
	public Gueltigkeit getGueltigKeit(LocalDateTime zeitpunkt) {
		
		boolean gueltig = jahr == null || jahr <= zeitpunkt.getYear();
		gueltig &= endJahr == null || endJahr >= zeitpunkt.getYear();
		gueltig &= monat == null || monat == zeitpunkt.getMonthValue();
		gueltig &= tag == null || tag == zeitpunkt.getDayOfMonth();
		
		if( gueltig) {
			return Gueltigkeit.of(ZustandsWechsel.of(LocalDateTime.of(LocalDate.of(zeitpunkt.getDayOfMonth(), zeitpunkt.getMonthValue(),  zeitpunkt.getYear()), LocalTime.MIDNIGHT), true),ZustandsWechsel.of(LocalDateTime.of(LocalDate.of(zeitpunkt.getDayOfMonth(), zeitpunkt.getMonthValue(),  zeitpunkt.getYear()), LocalTime.MIDNIGHT).plusDays(1), false) );
		}
		
		// TODO Auto-generated method stub
		return Gueltigkeit.of(ZustandsWechsel.of(LocalDateTime.MIN, false), ZustandsWechsel.of(LocalDateTime.MIN, false));
	}

	@Override
	public List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {
		// TODO Auto-generated method stub
		return Collections.emptyList();

	}
}

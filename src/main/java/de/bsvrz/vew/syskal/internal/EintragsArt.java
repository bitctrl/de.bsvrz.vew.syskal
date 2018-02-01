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

/**
 * Definition für die Arten der Daten mit denen der Inhalt eines
 * Systemkalendereintrags definiert werden kann.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum EintragsArt {

	/**
	 * Platzhalter für Einträge bei denen der Definitionsstring nicht ermittelt
	 * werden konnte.
	 */
	UNDEFINIERT("Undefiniert"),

	/** Typ für vordefinierte Einträge. */
	VORDEFINIERT("Vordefiniert"),

	/** Typ für Einträge die durch ein einzelnes Datun definiert werden. */
	NURDATUM("Datum"),

	/** Typ für Einträge die durch einen Zeitbereich definiert werden. */
	DATUMSBEREICH("Zeitbereich"),

	/**
	 * Typ für Einträge die durch Erweiterung eines bestehenden definiert
	 * werden.
	 */
	ABGELEITET("Referenz"),

	/**
	 * Typ für Einträge die durch logische Verknüpfung mehrerer anderer Einträge
	 * definiert werden.
	 */
	VERKNUEPFT("Verknüpfung");

	/** der Name der Eintragsart. */
	private final String name;

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            der Name des Typs
	 */
	private EintragsArt(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}

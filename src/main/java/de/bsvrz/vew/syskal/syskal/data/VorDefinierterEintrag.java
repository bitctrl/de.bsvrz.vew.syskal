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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repräsentation der vordefinierten Einträge des Systemkalender. Laut
 * Spezifikation sind folgende Einträge vorgegeben:
 * <ul>
 * <li>Montag</li>
 * <li>Dienstag</li>
 * <li>Mittwoch</li>
 * <li>Donnerstag</li>
 * <li>Freitag</li>
 * <li>Samstag</li>
 * <li>Sonntag</li>
 * <li>Ostersonntag</li>
 * </ul>
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class VorDefinierterEintrag extends KalenderEintragDefinition {

	/** Vordefinierter Eintrag MONTAG. */
	private static final VorDefinierterEintrag MONTAG = new VorDefinierterEintrag("Montag", DayOfWeek.MONDAY);

	/** Vordefinierter Eintrag DIENSTAG. */
	private static final VorDefinierterEintrag DIENSTAG = new VorDefinierterEintrag("Dienstag", DayOfWeek.TUESDAY);

	/** Vordefinierter Eintrag MITTWOCH. */
	private static final VorDefinierterEintrag MITTWOCH = new VorDefinierterEintrag("Mittwoch", DayOfWeek.WEDNESDAY);

	/** Vordefinierter Eintrag DONNERSTAG. */
	private static final VorDefinierterEintrag DONNERSTAG = new VorDefinierterEintrag("Donnerstag", DayOfWeek.THURSDAY);

	/** Vordefinierter Eintrag FREITAG. */
	private static final VorDefinierterEintrag FREITAG = new VorDefinierterEintrag("Freitag", DayOfWeek.FRIDAY);

	/** Vordefinierter Eintrag SAMSTAG. */
	private static final VorDefinierterEintrag SAMSTAG = new VorDefinierterEintrag("Samstag", DayOfWeek.SATURDAY);

	/** Vordefinierter Eintrag SONNTAG. */
	private static final VorDefinierterEintrag SONNTAG = new VorDefinierterEintrag("Sonntag", DayOfWeek.SUNDAY);

	/** Vordefinierter Eintrag OSTERSONNTAG. */
	private static final VorDefinierterEintrag OSTERSONNTAG = new Ostersonntag();

	/** die Menge der vordefinierten Einträge. */
	private static Map<String, VorDefinierterEintrag> eintraege;

	/**
	 * liefert die Menge der vordefinierten Einträge.
	 * 
	 * @return die Menge der Einträge
	 */
	public static Map<String, VorDefinierterEintrag> getEintraege() {
		VorDefinierterEintrag.initialisiereEintrage();
		return VorDefinierterEintrag.eintraege;
	}

	/**
	 * liefert den vordefinierten Eintrag mit dem übergebenen Name, Klein- und
	 * Großschreibung wird ignoriert. Wenn kein entsprechender Eintrag gefunden
	 * wurde, wird der Wert <code>null</code> geliefert.
	 * 
	 * @param name
	 *            der Name des gesuchten Eintrags
	 * @return den Eintrag oder <code>null</code>
	 */
	public static VorDefinierterEintrag getEintrag(final String name) {
		VorDefinierterEintrag.initialisiereEintrage();
		return VorDefinierterEintrag.eintraege.get(name.toLowerCase());
	}

	/** interne Funktion zur Initialisierung der vordefinierten Einträge. */
	private synchronized static void initialisiereEintrage() {
		if (VorDefinierterEintrag.eintraege == null) {
			VorDefinierterEintrag.eintraege = new HashMap<String, VorDefinierterEintrag>();
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.MONTAG.getName().toLowerCase(),
					VorDefinierterEintrag.MONTAG);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.DIENSTAG.getName().toLowerCase(),
					VorDefinierterEintrag.DIENSTAG);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.MITTWOCH.getName().toLowerCase(),
					VorDefinierterEintrag.MITTWOCH);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.DONNERSTAG.getName().toLowerCase(),
					VorDefinierterEintrag.DONNERSTAG);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.FREITAG.getName().toLowerCase(),
					VorDefinierterEintrag.FREITAG);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.SAMSTAG.getName().toLowerCase(),
					VorDefinierterEintrag.SAMSTAG);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.SONNTAG.getName().toLowerCase(),
					VorDefinierterEintrag.SONNTAG);
			VorDefinierterEintrag.eintraege.put(VorDefinierterEintrag.OSTERSONNTAG.getName().toLowerCase(),
					VorDefinierterEintrag.OSTERSONNTAG);
		}
	}

	private DayOfWeek dayOfWeek;

	/**
	 * privater Konstruktor, der einen Eintrag mit dem übergebenen Name erzeugt.
	 * 
	 * @param name
	 *            der Name
	 */
	VorDefinierterEintrag(final String name, DayOfWeek dayOfWeek) {
		super(name, name);
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public EintragsArt getEintragsArt() {
		return EintragsArt.VORDEFINIERT;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Gueltigkeit getGueltigKeit(LocalDateTime zeitPunkt) {

		DayOfWeek todayWeekday = zeitPunkt.getDayOfWeek();
		LocalDateTime beginn = LocalDateTime.of(zeitPunkt.toLocalDate(), LocalTime.MIDNIGHT);
		LocalDateTime ende = LocalDateTime.from(beginn).plusDays(1);

		if (todayWeekday.equals(dayOfWeek)) {
			return Gueltigkeit.of(ZustandsWechsel.of(beginn, true), ZustandsWechsel.of(ende, false));
		}

		while (beginn.getDayOfWeek() != dayOfWeek) {
			beginn = beginn.minusDays(1);
		}
		beginn = beginn.plusDays(1);

		while (ende.getDayOfWeek() != dayOfWeek) {
			ende = ende.plusDays(1);
		}
		return Gueltigkeit.of(ZustandsWechsel.of(beginn, false), ZustandsWechsel.of(ende, true));
	}

	@Override
	public List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {
		ArrayList<ZustandsWechsel> result = new ArrayList<>();

		LocalDate startDate = start.toLocalDate();
		LocalDate endDate = ende.toLocalDate();

		Gueltigkeit gueltigKeit = getGueltigKeit(start);
		result.add(ZustandsWechsel.of(start, gueltigKeit.getBeginn().isWirdGueltig()));
		if (gueltigKeit.getBeginn().isWirdGueltig()) {
			result.add(ZustandsWechsel.of(LocalDateTime.of(startDate, LocalTime.MIDNIGHT).plusDays(1), false));
		}

		startDate = startDate.plusDays(1);
		while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
			if (startDate.getDayOfWeek().equals(dayOfWeek)) {
				result.add(ZustandsWechsel.of(LocalDateTime.of(startDate, LocalTime.MIDNIGHT), true));
				if (startDate.isBefore(endDate)) {
					result.add(ZustandsWechsel.of(LocalDateTime.of(startDate, LocalTime.MIDNIGHT).plusDays(1), false));
				}
			}
			startDate = startDate.plusDays(1);
		}

		return result;
	}
}

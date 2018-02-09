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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

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
public class VorDefinierterEintrag extends KalenderEintrag {

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
	private static synchronized void initialisiereEintrage() {
		if (VorDefinierterEintrag.eintraege == null) {
			VorDefinierterEintrag.eintraege = new HashMap<>();
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
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitPunkt) {

		boolean gueltig = zeitPunkt.getDayOfWeek().equals(dayOfWeek);

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if (gueltig) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(checkDate, true),
					ZustandsWechsel.of(checkDate.plusDays(1), false));
		}

		while (checkDate.getDayOfWeek() != dayOfWeek) {
			checkDate = checkDate.plusDays(1);
		}

		return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(checkDate.minusDays(6), false),
				ZustandsWechsel.of(checkDate, true));
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitPunkt) {

		boolean gueltig = zeitPunkt.getDayOfWeek().equals(dayOfWeek);

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if (gueltig) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(checkDate.minusDays(6), false),
					ZustandsWechsel.of(checkDate, true));
		}

		while (checkDate.getDayOfWeek() != dayOfWeek) {
			checkDate = checkDate.minusDays(1);
		}

		return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(checkDate, true),
				ZustandsWechsel.of(checkDate.plusDays(1), false));
	}
}

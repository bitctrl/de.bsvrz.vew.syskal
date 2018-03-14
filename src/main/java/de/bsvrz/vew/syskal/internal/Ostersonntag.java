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

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;

public class Ostersonntag extends VorDefinierterEintrag {

	public static LocalDate getDatumImJahr(int jahr) {

		int i = jahr % 19;
		int j = jahr / 100;
		int k = jahr % 100;

		int l = (19 * i + j - (j / 4) - ((j - ((j + 8) / 25) + 1) / 3) + 15) % 30;
		int m = (32 + 2 * (j % 4) + 2 * (k / 4) - l - (k % 4)) % 7;
		int n = l + m - 7 * ((i + 11 * l + 22 * m) / 451) + 114;

		int monat = n / 31;
		int tag = (n % 31) + 1;

		return LocalDate.of(jahr, monat, tag);
	}

	Ostersonntag() {
		super("Ostersonntag", DayOfWeek.SUNDAY);
	}

	@Override
	public boolean bestimmeGueltigkeit(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if (checkDate.getYear() <= 0) {
			return false;
		}

		LocalDate osterDate = Ostersonntag.getDatumImJahr(checkDate.getYear());

		return osterDate.equals(checkDate);
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if (checkDate.getYear() <= 0) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		LocalDate osterDate = Ostersonntag.getDatumImJahr(checkDate.getYear());

		boolean gueltig = osterDate.equals(checkDate);

		if (gueltig) {
			return SystemkalenderGueltigkeit.gueltig(zeitPunkt.toLocalDate(), zeitPunkt.toLocalDate().plusDays(1));
		}

		if (checkDate.isBefore(osterDate)) {
			return SystemkalenderGueltigkeit.ungueltig(Ostersonntag.getDatumImJahr(checkDate.getYear() - 1).plusDays(1),
					osterDate);
		}

		return SystemkalenderGueltigkeit.ungueltig(osterDate.plusDays(1),
				Ostersonntag.getDatumImJahr(checkDate.getYear() + 1));
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitVor(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if (checkDate.getYear() <= 0) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		LocalDate osterDatum = Ostersonntag.getDatumImJahr(checkDate.getYear());
		LocalDate osterDatumVorjahr = Ostersonntag.getDatumImJahr(checkDate.getYear() - 1);

		boolean gueltig = osterDatum.equals(checkDate);

		if (gueltig) {
			return SystemkalenderGueltigkeit.ungueltig(osterDatumVorjahr.plusDays(1), osterDatum);
		}

		if (zeitPunkt.toLocalDate().isBefore(osterDatum)) {
			return SystemkalenderGueltigkeit.gueltig(osterDatumVorjahr, osterDatumVorjahr.plusDays(1));
		}

		return SystemkalenderGueltigkeit.gueltig(osterDatum, osterDatum.plusDays(1));
	}
}

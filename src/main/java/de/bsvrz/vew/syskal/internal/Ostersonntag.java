package de.bsvrz.vew.syskal.internal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

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
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if( checkDate.getYear() <= 0) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}
		
		LocalDate osterDate = Ostersonntag.getDatumImJahr(checkDate.getYear());

		boolean gueltig = osterDate.equals(checkDate);

		if (gueltig) {
			return SystemkalenderGueltigkeit.of(
					ZustandsWechsel.of(zeitPunkt.toLocalDate(), true),
					ZustandsWechsel.of(zeitPunkt.toLocalDate().plusDays(1), false));
		}

		
		if (checkDate.isBefore(osterDate)) {
			return SystemkalenderGueltigkeit.of(
					ZustandsWechsel.of(Ostersonntag.getDatumImJahr(checkDate.getYear() - 1).plusDays(1), false),
					ZustandsWechsel.of(osterDate, true));
		}
		
		return SystemkalenderGueltigkeit.of(
				ZustandsWechsel.of(osterDate.plusDays(1), false), 
				ZustandsWechsel.of(Ostersonntag.getDatumImJahr(checkDate.getYear() + 1), true));
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		if( checkDate.getYear() <= 0) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		LocalDate osterDate = Ostersonntag.getDatumImJahr(checkDate.getYear());

		boolean gueltig = osterDate.equals(checkDate);

		if (gueltig) {
			return SystemkalenderGueltigkeit.of(
					ZustandsWechsel.of(osterDate.minusYears(1).plusDays(1), false),
					ZustandsWechsel.of(osterDate, true));
		}

		if (zeitPunkt.toLocalDate().isBefore(osterDate)) {
			return SystemkalenderGueltigkeit.of(
					ZustandsWechsel.of(osterDate.minusYears(1), true),
					ZustandsWechsel.of(osterDate.minusYears(1).plusDays(1), false));
		}
		
		return SystemkalenderGueltigkeit.of(
				ZustandsWechsel.of(osterDate, true), 
				ZustandsWechsel.of(osterDate.plusDays(1), false));
	}
}

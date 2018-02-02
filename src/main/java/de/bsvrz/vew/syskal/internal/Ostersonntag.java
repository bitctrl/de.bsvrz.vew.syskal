package de.bsvrz.vew.syskal.internal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import de.bsvrz.vew.syskal.Gueltigkeit;

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
	public Gueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		LocalDate osterDate = Ostersonntag.getDatumImJahr(checkDate.getYear());

		boolean gueltig = osterDate.equals(checkDate);

		if (gueltig) {
			return GueltigkeitImpl.of(gueltig, ZustandsWechselImpl
					.of(LocalDateTime.of(zeitPunkt.toLocalDate().plusDays(1), LocalTime.MIDNIGHT), !gueltig));
		}

		if (zeitPunkt.toLocalDate().isBefore(osterDate)) {
			return GueltigkeitImpl.of(gueltig,
					ZustandsWechselImpl.of(LocalDateTime.of(osterDate, LocalTime.MIDNIGHT), !gueltig));
		}
		return GueltigkeitImpl.of(gueltig, ZustandsWechselImpl.of(
				LocalDateTime.of(Ostersonntag.getDatumImJahr(zeitPunkt.getYear() + 1), LocalTime.MIDNIGHT), !gueltig));

	}
}

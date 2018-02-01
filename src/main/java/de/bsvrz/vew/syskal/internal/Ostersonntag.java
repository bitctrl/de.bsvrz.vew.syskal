package de.bsvrz.vew.syskal.internal;

import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.Gueltigkeit;
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
	public Gueltigkeit isZeitlichGueltig(LocalDateTime zeitPunkt) {

		LocalDate checkDate = zeitPunkt.toLocalDate();
		LocalDate osterDate = Ostersonntag.getDatumImJahr(checkDate.getYear());

		boolean gueltig = osterDate.equals(checkDate);

		if (gueltig) {
			return Gueltigkeit.of(gueltig, ZustandsWechsel
					.of(LocalDateTime.of(zeitPunkt.toLocalDate().plusDays(1), LocalTime.MIDNIGHT), !gueltig));
		}

		if (zeitPunkt.toLocalDate().isBefore(osterDate)) {
			return Gueltigkeit.of(gueltig,
					ZustandsWechsel.of(LocalDateTime.of(osterDate, LocalTime.MIDNIGHT), !gueltig));
		}
		return Gueltigkeit.of(gueltig, ZustandsWechsel.of(
				LocalDateTime.of(Ostersonntag.getDatumImJahr(zeitPunkt.getYear() + 1), LocalTime.MIDNIGHT), !gueltig));

	}

	@Override
	public List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {

		ArrayList<ZustandsWechsel> result = new ArrayList<>();

		LocalDate startDate = start.toLocalDate();
		LocalDate endDate = ende.toLocalDate();
		LocalDate ostern = Ostersonntag.getDatumImJahr(start.getYear());

		int jahr = startDate.getYear();
		if (startDate.equals(ostern)) {
			result.add(ZustandsWechsel.of(start, true));
			jahr++;
		} else if (startDate.isBefore(ostern)) {
			result.add(ZustandsWechsel.of(start, false));
		} else {
			result.add(ZustandsWechsel.of(start, false));
			jahr++;
		}

		while (jahr <= endDate.getYear()) {
			ostern = Ostersonntag.getDatumImJahr(jahr);
			LocalDateTime jahrStartTime = LocalDateTime.of(ostern, LocalTime.MIDNIGHT);
			LocalDateTime jahrEndTime = LocalDateTime.of(ostern, LocalTime.MIDNIGHT).plusDays(1);

			if (ende.isEqual(jahrStartTime) || ende.isAfter(jahrStartTime)) {
				result.add(ZustandsWechsel.of(jahrStartTime, true));
			}
			if (ende.isEqual(jahrEndTime) || ende.isAfter(jahrEndTime)) {
				result.add(ZustandsWechsel.of(jahrEndTime, false));
			}
			jahr++;
		}

		return result;
	}
}

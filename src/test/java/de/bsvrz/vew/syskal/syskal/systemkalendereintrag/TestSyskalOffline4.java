package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline4 {

	@Test
	public void beispiele4() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test1", "Test1:=29.02.2001,2010"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test2", "Test2:=29.02.2004,2010"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test3",
				"Test3:=({13:00:00,000-15:00:00,000}{14:00:00,000-16:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test31",
				"Test31:=({13:00:00,000-14:00:00,000}{14:00:00,000-15:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test32",
				"Test32:=({13:00:00,000-14:00:00,000}{15:00:00,000-16:00:00,000})"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test4", "Test4:=Ostersonntag+1Tag"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test5", "Test5:=Ostersonntag-4Tage"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Karfreitag", "Karfreitag:=Ostermontag-3Tage"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Test6", "Test6:=UND{Tag,NICHT Ostersonntag}*,*"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Test7", "Test7:=UND{Test32,Tag}*,*"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Test1");
		LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test2");
		startTime = LocalDateTime.of(2004, 1, 1, 0, 0, 0);
		endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage2: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test3");
		startTime = LocalDateTime.of(2004, 3, 10, 13, 30, 0);
		endTime = LocalDateTime.of(2004, 3, 10, 15, 29, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage3: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test31");
		startTime = LocalDateTime.of(2004, 3, 10, 0, 0, 0);
		endTime = LocalDateTime.of(2004, 3, 11, 15, 29, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out.println(
				"Abfrage31: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Ostersonntag");
		startTime = LocalDateTime.of(2000, 1, 11, 0, 0, 0);
		endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage4: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test4");
		startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		endTime = LocalDateTime.of(2003, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage5: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test5");
		startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		endTime = LocalDateTime.of(2003, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage6: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Karfreitag");
		startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		endTime = LocalDateTime.of(2003, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage7: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test6");
		startTime = LocalDateTime.of(2001, 4, 1, 0, 0, 0);
		endTime = LocalDateTime.of(2001, 5, 1, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage8: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("Test7");
		startTime = LocalDateTime.of(2011, 9, 19, 0, 0, 0);
		endTime = LocalDateTime.of(2011, 9, 23, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage9: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}
}

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

public class SystemkalenderEintragTest {

	private static TestKalenderEintragProvider eintragsProvider;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Mittwoch", "Mittwoch"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Freitag", "Freitag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Samstag", "Samstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Sonntag", "Sonntag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Bereich1",
				"Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Bereich2",
				"Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "TDDEalt", "17.06.1963,1989"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "TDDEneu", "03.10.1990,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Tag der deutschen Einheit",
				"ODER{TDDEalt,TDDEneu}*,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Bereich5",
				"Bereich5:=<01.09.2009-30.09.2009>({08:00:00,000-16:00:00,000})"));
	}

	@Test
	public void isGueltigTest() throws Exception {

		LocalDateTime zeitpunkt = LocalDateTime.now();
		DayOfWeek dow = zeitpunkt.getDayOfWeek();

		KalenderEintragDefinition skeMo = eintragsProvider.getKalenderEintrag("Montag");
		KalenderEintragDefinition skeDi = eintragsProvider.getKalenderEintrag("Dienstag");
		KalenderEintragDefinition skeMi = eintragsProvider.getKalenderEintrag("Mittwoch");
		KalenderEintragDefinition skeDo = eintragsProvider.getKalenderEintrag("Donnerstag");
		KalenderEintragDefinition skeFr = eintragsProvider.getKalenderEintrag("Freitag");
		KalenderEintragDefinition skeSa = eintragsProvider.getKalenderEintrag("Samstag");
		KalenderEintragDefinition skeSo = eintragsProvider.getKalenderEintrag("Sonntag");

		switch (dow) {
		case SUNDAY:
			assertTrue("Test 0", skeSa.isGueltig(zeitpunkt));
			assertFalse("Test 1", skeSo.isGueltig(zeitpunkt));
			assertFalse("Test 2", skeMo.isGueltig(zeitpunkt));
			assertFalse("Test 3", skeDi.isGueltig(zeitpunkt));
			assertFalse("Test 4", skeMi.isGueltig(zeitpunkt));
			assertFalse("Test 5", skeDo.isGueltig(zeitpunkt));
			assertFalse("Test 6", skeFr.isGueltig(zeitpunkt));
			break;
		case MONDAY:
			assertFalse("Test 0", skeSa.isGueltig(zeitpunkt));
			assertTrue("Test 1", skeSo.isGueltig(zeitpunkt));
			assertFalse("Test 2", skeMo.isGueltig(zeitpunkt));
			assertFalse("Test 3", skeDi.isGueltig(zeitpunkt));
			assertFalse("Test 4", skeMi.isGueltig(zeitpunkt));
			assertFalse("Test 5", skeDo.isGueltig(zeitpunkt));
			assertFalse("Test 6", skeFr.isGueltig(zeitpunkt));
			break;
		case TUESDAY:
			assertFalse("Test 0", skeSa.isGueltig(zeitpunkt));
			assertFalse("Test 1", skeSo.isGueltig(zeitpunkt));
			assertTrue("Test 2", skeMo.isGueltig(zeitpunkt));
			assertFalse("Test 3", skeDi.isGueltig(zeitpunkt));
			assertFalse("Test 4", skeMi.isGueltig(zeitpunkt));
			assertFalse("Test 5", skeDo.isGueltig(zeitpunkt));
			assertFalse("Test 6", skeFr.isGueltig(zeitpunkt));
			break;
		case WEDNESDAY:
			assertFalse("Test 0", skeSa.isGueltig(zeitpunkt));
			assertFalse("Test 1", skeSo.isGueltig(zeitpunkt));
			assertFalse("Test 2", skeMo.isGueltig(zeitpunkt));
			assertTrue("Test 3", skeDi.isGueltig(zeitpunkt));
			assertFalse("Test 4", skeMi.isGueltig(zeitpunkt));
			assertFalse("Test 5", skeDo.isGueltig(zeitpunkt));
			assertFalse("Test 6", skeFr.isGueltig(zeitpunkt));
			break;
		case THURSDAY:
			assertFalse("Test 0", skeSa.isGueltig(zeitpunkt));
			assertFalse("Test 1", skeSo.isGueltig(zeitpunkt));
			assertFalse("Test 2", skeMo.isGueltig(zeitpunkt));
			assertFalse("Test 3", skeDi.isGueltig(zeitpunkt));
			assertTrue("Test 4", skeMi.isGueltig(zeitpunkt));
			assertFalse("Test 5", skeDo.isGueltig(zeitpunkt));
			assertFalse("Test 6", skeFr.isGueltig(zeitpunkt));
			break;
		case FRIDAY:
			assertFalse("Test 0", skeSa.isGueltig(zeitpunkt));
			assertFalse("Test 1", skeSo.isGueltig(zeitpunkt));
			assertFalse("Test 2", skeMo.isGueltig(zeitpunkt));
			assertFalse("Test 3", skeDi.isGueltig(zeitpunkt));
			assertFalse("Test 4", skeMi.isGueltig(zeitpunkt));
			assertTrue("Test 5", skeDo.isGueltig(zeitpunkt));
			assertFalse("Test 6", skeFr.isGueltig(zeitpunkt));
			break;
		case SATURDAY:
			assertFalse("Test 0", skeSa.isGueltig(zeitpunkt));
			assertFalse("Test 1", skeSo.isGueltig(zeitpunkt));
			assertFalse("Test 2", skeMo.isGueltig(zeitpunkt));
			assertFalse("Test 3", skeDi.isGueltig(zeitpunkt));
			assertFalse("Test 4", skeMi.isGueltig(zeitpunkt));
			assertFalse("Test 5", skeDo.isGueltig(zeitpunkt));
			assertTrue("Test 6", skeFr.isGueltig(zeitpunkt));
			break;

		default:
			fail("Test 0-6");

		}

	}

	@Test
	public void berechneZustandsWechselVonBisTest() throws Exception {

		KalenderEintragDefinition eintrag = eintragsProvider.getKalenderEintrag("Bereich4");

		LocalDateTime startTime = LocalDateTime.of(2008, 1, 15, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2008, 1, 16, 23, 59, 59);
		endTime = endTime.plusNanos(999000);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);

		assertTrue("Test 12", zustandsWechsel.size() == 8);
		assertTrue("Test 13", zustandsWechsel.get(0).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 15, 9, 0, 0))
				&& zustandsWechsel.get(0).isWirdGueltig());
		assertTrue("Test 14",
				zustandsWechsel.get(1).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 15, 11, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(1).isWirdGueltig());
		assertTrue("Test 15", zustandsWechsel.get(2).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 15, 15, 30, 00))
				&& zustandsWechsel.get(2).isWirdGueltig());
		assertTrue("Test 16",
				zustandsWechsel.get(3).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 15, 17, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(3).isWirdGueltig());
		assertTrue("Test 17", zustandsWechsel.get(4).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 16, 9, 0, 0))
				&& zustandsWechsel.get(4).isWirdGueltig());
		assertTrue("Test 18",
				zustandsWechsel.get(5).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 16, 11, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(5).isWirdGueltig());
		assertTrue("Test 19", zustandsWechsel.get(6).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 16, 15, 30, 00))
				&& zustandsWechsel.get(6).isWirdGueltig());
		assertTrue("Test 20",
				zustandsWechsel.get(7).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 16, 17, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(7).isWirdGueltig());

		eintrag = eintragsProvider.getKalenderEintrag("Tag der deutschen Einheit");

		startTime = LocalDateTime.of(1960, 6, 17, 23, 59, 59);
		endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(999000);
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);

		int offset = 0;
		assertTrue("Test 21", zustandsWechsel.size() == 96);
		assertTrue("Test 22", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1963, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 23",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1963, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 24", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1964, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 25",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1964, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 26", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1965, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 27",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1965, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 28", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1966, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 29",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1966, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 30", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1967, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 31",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1967, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 32", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1968, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 33",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1968, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 34", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1969, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 35",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1969, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 36", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1970, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 37",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1970, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 38", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1971, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 39",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1971, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 40", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1972, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 41",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1972, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 42", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1973, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 43",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1973, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 44", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1974, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 45",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1974, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 46", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1975, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 47",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1975, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 48", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1976, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 49",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1976, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 50", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1977, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 51",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1977, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 52", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1978, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 53",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1978, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 54", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1979, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 55",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1979, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 56", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1980, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 57",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1980, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 58", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1981, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 59",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1981, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 60", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1982, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 61",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1982, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 62", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1983, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 63",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1983, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 64", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1984, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 65",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1984, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 66", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1985, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 67",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1985, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 68", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1986, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 69",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1986, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 70", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1987, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 71",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1987, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 72", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1988, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 73",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1988, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 74", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1989, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 75",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1989, 6, 17, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 76", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1990, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 77",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1990, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 78", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1991, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 79",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1991, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 80", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1992, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 81",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1992, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 82", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1993, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 83",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1993, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 84", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1994, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 85",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1994, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 86", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1995, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 87",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1995, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 88", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1996, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 89",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1996, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 90", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1997, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 91",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1997, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 92", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1998, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 93",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1998, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 94", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1999, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 95",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(1999, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 96", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2000, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 97",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2000, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 98", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2001, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 99",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2001, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 100", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2002, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 101",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2002, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 102", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2003, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 103",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2003, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 104", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2004, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 105",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2004, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 106", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2005, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 107",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2005, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 108", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2006, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 109",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2006, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 110", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2007, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 111",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2007, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 112", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 113",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 114", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2009, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 115",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2009, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 116", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2010, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 117",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2010, 3, 10, 23, 59, 59).plusNanos(999000))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
	}
}

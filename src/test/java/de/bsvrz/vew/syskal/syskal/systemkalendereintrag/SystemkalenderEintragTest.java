package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class SystemkalenderEintragTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(20);
	
	private static TestKalenderEintragProvider eintragsProvider;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Mittwoch", "Mittwoch"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Freitag", "Freitag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Samstag", "Samstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Sonntag", "Sonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich1",
				"Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich2",
				"Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TDDEalt", "17.06.1963,1989"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TDDEneu", "03.10.1990,*"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Tag der deutschen Einheit", "ODER{TDDEalt,TDDEneu}*,*"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich5",
				"Bereich5:=<01.09.2009-30.09.2009>({08:00:00,000-16:00:00,000})"));
	}

	@Test
	public void isGueltigTest() throws Exception {

		LocalDateTime zeitpunkt = LocalDateTime.now();
		DayOfWeek dow = zeitpunkt.getDayOfWeek();

		KalenderEintrag skeMo = eintragsProvider.getKalenderEintrag("Montag");
		KalenderEintrag skeDi = eintragsProvider.getKalenderEintrag("Dienstag");
		KalenderEintrag skeMi = eintragsProvider.getKalenderEintrag("Mittwoch");
		KalenderEintrag skeDo = eintragsProvider.getKalenderEintrag("Donnerstag");
		KalenderEintrag skeFr = eintragsProvider.getKalenderEintrag("Freitag");
		KalenderEintrag skeSa = eintragsProvider.getKalenderEintrag("Samstag");
		KalenderEintrag skeSo = eintragsProvider.getKalenderEintrag("Sonntag");

		switch (dow) {
		case SATURDAY:
			assertTrue("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;
		case SUNDAY:
			assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertTrue("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;
		case MONDAY:
			assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertTrue("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;
		case TUESDAY:
			assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertTrue("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;
		case WEDNESDAY:
			assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertTrue("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;
		case THURSDAY:
			assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertTrue("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;
		case FRIDAY:
			assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			assertTrue("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
			break;

		default:
			fail("Test 0-6");

		}

	}

	@Test
	public void berechneZustandsWechselVonBisTest() throws Exception {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Bereich4");

		LocalDateTime startTime = LocalDateTime.of(2008, 1, 15, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2008, 1, 16, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);

		int offset = 0;

		assertEquals("Test 12", zustandsWechsel.size(), 9);

		assertTrue("Test 13", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 15, 0, 0, 0))
				&& !zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 13", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 15, 9, 0, 0))
				&& zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 14",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 15, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)))
						&& !zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 15",
				zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 15, 15, 30, 00))
						&& zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 16",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 15, 17, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)))
						&& !zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 17", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 16, 9, 0, 0))
				&& zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 18",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 16, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)))
						&& !zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 19",
				zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 1, 16, 15, 30, 00))
						&& zustandsWechsel.get(offset).isWirdGueltig());
		offset++;

		assertTrue("Test 20",
				zustandsWechsel.get(offset).getZeitPunkt()
						.equals(LocalDateTime.of(2008, 1, 16, 17, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)))
						&& !zustandsWechsel.get(offset).isWirdGueltig());

		eintrag = eintragsProvider.getKalenderEintrag("Tag der deutschen Einheit");

		startTime = LocalDateTime.of(1960, 6, 17, 23, 59, 59);
		endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);

		offset = 0;
		assertEquals("Test 21", 97, zustandsWechsel.size());
		assertTrue("Test 22.0",
				zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1960, 6, 17, 23, 59, 59))
						&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 22", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1963, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 23", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1963, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 24", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1964, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 25", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1964, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 26", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1965, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 27", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1965, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 28", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1966, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 29", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1966, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 30", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1967, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 31", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1967, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 32", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1968, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 33", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1968, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 34", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1969, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 35", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1969, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 36", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1970, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 37", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1970, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 38", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1971, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 39", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1971, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 40", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1972, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 41", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1972, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 42", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1973, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 43", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1973, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 44", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1974, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 45", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1974, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 46", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1975, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 47", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1975, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 48", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1976, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 49", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1976, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 50", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1977, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 51", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1977, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 52", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1978, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 53", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1978, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 54", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1979, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 55", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1979, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 56", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1980, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 57", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1980, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 58", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1981, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 59", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1981, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 60", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1982, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 61", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1982, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 62", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1983, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 63", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1983, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 64", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1984, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 65", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1984, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 66", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1985, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 67", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1985, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 68", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1986, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 69", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1986, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 70", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1987, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 71", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1987, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 72", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1988, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 73", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1988, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 74", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1989, 6, 17, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 75", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1989, 6, 18, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 76", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1990, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 77", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1990, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 78", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1991, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 79", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1991, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 80", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1992, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 81", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1992, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 82", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1993, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 83", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1993, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 84", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1994, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 85", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1994, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 86", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1995, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 87", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1995, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 88", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1996, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 89", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1996, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 90", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1997, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 91", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1997, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 92", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1998, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 93", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1998, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 94", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1999, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 95", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(1999, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 96", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2000, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 97", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2000, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 98", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2001, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 99", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2001, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 100", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2002, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 101", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2002, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 102", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2003, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 103", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2003, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 104", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2004, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 105", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2004, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 106", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2005, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 107", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2005, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 108", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2006, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 109", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2006, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 110", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2007, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 111", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2007, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 112", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 113", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2008, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 114", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2009, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 115", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2009, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 116", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2010, 10, 3, 0, 0, 0))
				&& zustandsWechsel.get(offset++).isWirdGueltig());
		assertTrue("Test 117", zustandsWechsel.get(offset).getZeitPunkt().equals(LocalDateTime.of(2010, 10, 4, 0, 0, 0))
				&& !zustandsWechsel.get(offset++).isWirdGueltig());
	}
}

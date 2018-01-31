package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;

public class ZeitBereichsEintragTest {

	@Test
	public void testeGueltigkeit() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		Gueltigkeit gueltigKeit = bereich4.getGueltigKeit(LocalDateTime.of(2008, 1, 30, 12, 10));
		assertEquals(LocalDateTime.of(2008, 1, 30, 11, 59, 59).plusNanos(999000),
				gueltigKeit.getBeginn().getZeitPunkt());
		assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 15, 30), gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitImBereich() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		Gueltigkeit gueltigKeit = bereich4.getGueltigKeit(LocalDateTime.of(2008, 1, 30, 10, 10));
		assertEquals(LocalDateTime.of(2008, 1, 30, 9, 0, 0), gueltigKeit.getBeginn().getZeitPunkt());
		assertTrue(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 11, 59, 59).plusNanos(999000),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeZustandswechsel() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		LocalDateTime start = LocalDateTime.of(2008, 1, 30, 12, 10);
		List<ZustandsWechsel> zustandsWechselListe = bereich4.getZustandsWechselImBereich(start,
				LocalDateTime.of(2008, 2, 1, 11, 11));

		assertEquals("Erwartete Zustandswechsel", 8, zustandsWechselListe.size());
		for (int index = 0; index < zustandsWechselListe.size(); index++) {

			ZustandsWechsel zustandsWechsel = zustandsWechselListe.get(index);

			switch (index) {
			case 0:
				assertEquals(start, zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 1:
				assertEquals(LocalDateTime.of(2008, 1, 30, 15, 30), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 2:
				assertEquals(LocalDateTime.of(2008, 1, 30, 17, 59, 59).plusNanos(999000),
						zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 3:
				assertEquals(LocalDateTime.of(2008, 1, 31, 9, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 4:
				assertEquals(LocalDateTime.of(2008, 1, 31, 11, 59, 59).plusNanos(999000),
						zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 5:
				assertEquals(LocalDateTime.of(2008, 1, 31, 15, 30), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 6:
				assertEquals(LocalDateTime.of(2008, 1, 31, 17, 59, 59).plusNanos(999000),
						zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 7:
				assertEquals(LocalDateTime.of(2008, 2, 1, 9, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			}
		}
	}
}

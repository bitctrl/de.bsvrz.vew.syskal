package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.ZeitBereichsEintrag;

public class ZeitBereichsEintragTest {
	
	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	@Test
	public void testeGueltigkeit() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		SystemkalenderGueltigkeit gueltigKeit = bereich4.getZeitlicheGueltigkeit(LocalDateTime.of(2008, 1, 30, 12, 10));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 15, 30), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitImBereich() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		SystemkalenderGueltigkeit gueltigKeit = bereich4.getZeitlicheGueltigkeit(LocalDateTime.of(2008, 1, 30, 10, 10));

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
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
				assertEquals(LocalDateTime.of(2008, 1, 30, 17, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
						zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 3:
				assertEquals(LocalDateTime.of(2008, 1, 31, 9, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 4:
				assertEquals(LocalDateTime.of(2008, 1, 31, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
						zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 5:
				assertEquals(LocalDateTime.of(2008, 1, 31, 15, 30), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 6:
				assertEquals(LocalDateTime.of(2008, 1, 31, 17, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
						zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 7:
				assertEquals(LocalDateTime.of(2008, 2, 1, 9, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			default:
				break;
			}
		}
	}
}

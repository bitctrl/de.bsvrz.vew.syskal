package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;
import de.bsvrz.vew.syskal.internal.VorDefinierterEintrag;

public class VordefinierterEintragTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);
	
	@Test
	public void testeGueltigkeit() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		VorDefinierterEintrag mittwoch = (VorDefinierterEintrag) KalenderEintrag.parse(provider, "Mittwoch",
				"Mittwoch");

		LocalDateTime now = LocalDateTime.now();

		for (int offset = 0; offset < 20; offset++) {
			SystemkalenderGueltigkeit gueltigKeit = mittwoch.getZeitlicheGueltigkeit(now);

			LocalDateTime startCheck;
			LocalDateTime endeCheck;

			if (now.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
				assertTrue(gueltigKeit.isZeitlichGueltig());
				startCheck = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
				endeCheck = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT).plusDays(1);
			} else {
				assertFalse(gueltigKeit.isZeitlichGueltig());
				endeCheck = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT).plusDays(1);
				while (!endeCheck.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
					endeCheck = endeCheck.plusDays(1);
				}
				startCheck = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
				while (!startCheck.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
					startCheck = startCheck.minusDays(1);
				}
				startCheck = startCheck.plusDays(1);
			}
			assertEquals(gueltigKeit.getNaechsterWechsel().getZeitPunkt(), endeCheck);

			now = now.plusDays(1);
		}
	}

	@Test
	public void testeZustandswechsel() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		VorDefinierterEintrag mittwoch = (VorDefinierterEintrag) KalenderEintrag.parse(provider, "Mittwoch",
				"Mittwoch");

		LocalDateTime start = LocalDateTime.of(2018, 1, 24, 12, 10);
		LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 10);

		List<ZustandsWechsel> zustandsWechselImBereich = mittwoch.getZustandsWechselImBereich(start, ende);

		assertEquals("Erwartete Zustandswechsel", 11, zustandsWechselImBereich.size());
		for (int index = 0; index < zustandsWechselImBereich.size(); index++) {

			ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);

			switch (index) {
			case 0:
				assertEquals(start, zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 1:
				assertEquals(LocalDateTime.of(2018, 1, 25, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 2:
				assertEquals(LocalDateTime.of(2018, 1, 31, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 3:
				assertEquals(LocalDateTime.of(2018, 2, 1, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 4:
				assertEquals(LocalDateTime.of(2018, 2, 7, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 5:
				assertEquals(LocalDateTime.of(2018, 2, 8, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 6:
				assertEquals(LocalDateTime.of(2018, 2, 14, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 7:
				assertEquals(LocalDateTime.of(2018, 2, 15, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 8:
				assertEquals(LocalDateTime.of(2018, 2, 21, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 9:
				assertEquals(LocalDateTime.of(2018, 2, 22, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 10:
				assertEquals(LocalDateTime.of(2018, 2, 28, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			}
		}
	}
}

package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.DatumsEintrag;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class DatumsEintragsTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);
	
	@Test
	public void testeTagDerArbeit() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		KalenderEintrag datumsEintrag = KalenderEintrag.parse(provider, "Mai1", "1.5.*,*");

		assertTrue(datumsEintrag instanceof DatumsEintrag);

		LocalDateTime checkDate = LocalDateTime.of(2018, 4, 15, 12, 0);
		SystemkalenderGueltigkeit gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018,  5, 1), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());

		checkDate = LocalDateTime.of(2018, 5, 1, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018,  5, 2), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());

		checkDate = LocalDateTime.of(2018, 5, 1, 11, 46);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018,  5, 2), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());

		checkDate = LocalDateTime.of(2018, 5, 2, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2019,  5, 1), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
	}

	@Test
	public void testeFebruar29() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		KalenderEintrag datumsEintrag = KalenderEintrag.parse(provider, "Februar29", "29.2.*,*");

		assertTrue(datumsEintrag instanceof DatumsEintrag);

		LocalDateTime checkDate = LocalDateTime.of(2018, 1, 15, 12, 0);
		SystemkalenderGueltigkeit gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020,  2, 29), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());

		checkDate = LocalDateTime.of(2020, 2, 29, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020,  3, 1), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());

		checkDate = LocalDateTime.of(2018, 3, 13, 11, 46);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020,  2, 29), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		
		checkDate = LocalDateTime.of(2020, 3, 1, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2024,  2, 29), LocalTime.MIDNIGHT), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
	}

	
	@Test
	public void testeZustandswechsel() {

//		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
//		Ostersonntag osterSonntag = (Ostersonntag) KalenderEintrag.parse(provider, "Ostersonntag", "Ostersonntag");
//
//		LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
//		LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);
//
//		List<ZustandsWechsel> zustandsWechselImBereich = osterSonntag.getZustandsWechselImBereich(start, ende);
//
//		assertEquals("Erwartete Zustandswechsel", 7, zustandsWechselImBereich.size());
//		for (int index = 0; index < zustandsWechselImBereich.size(); index++) {
//
//			ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);
//
//			switch (index) {
//			case 0:
//				assertEquals(start, zustandsWechsel.getZeitPunkt());
//				assertFalse(zustandsWechsel.isWirdGueltig());
//				break;
//			case 1:
//				assertEquals(LocalDateTime.of(2015, 4, 5, 0, 0), zustandsWechsel.getZeitPunkt());
//				assertTrue(zustandsWechsel.isWirdGueltig());
//				break;
//			case 2:
//				assertEquals(LocalDateTime.of(2015, 4, 6, 0, 0), zustandsWechsel.getZeitPunkt());
//				assertFalse(zustandsWechsel.isWirdGueltig());
//				break;
//			case 3:
//				assertEquals(LocalDateTime.of(2016, 3, 27, 0, 0), zustandsWechsel.getZeitPunkt());
//				assertTrue(zustandsWechsel.isWirdGueltig());
//				break;
//			case 4:
//				assertEquals(LocalDateTime.of(2016, 3, 28, 0, 0), zustandsWechsel.getZeitPunkt());
//				assertFalse(zustandsWechsel.isWirdGueltig());
//				break;
//			case 5:
//				assertEquals(LocalDateTime.of(2017, 4, 16, 0, 0), zustandsWechsel.getZeitPunkt());
//				assertTrue(zustandsWechsel.isWirdGueltig());
//				break;
//			case 6:
//				assertEquals(LocalDateTime.of(2017, 4, 17, 0, 0), zustandsWechsel.getZeitPunkt());
//				assertFalse(zustandsWechsel.isWirdGueltig());
//				break;
//			}
//		}
	}

}

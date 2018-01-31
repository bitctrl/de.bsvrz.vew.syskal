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

import org.junit.BeforeClass;
import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;

public class UndVerknuepfungTest {

	private static TestKalenderEintragProvider provider;
	private static UndVerknuepfung verknuepfung;
	private static LocalDate testMontag;

	@BeforeClass
	public static void init() {
		provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Montag", "Montag");
		provider.parseAndAdd(provider, "Mittags", "Mittags:=({11:30:00,000-13:30:00,000})");
		verknuepfung = (UndVerknuepfung) provider.parseAndAdd(provider, "MontagMittag", "UND{Montag, Mittags}");

		testMontag = LocalDate.of(2018, 12, 24);
	}

	@Test
	public void testeGueltigkeitMontagMittag() {

		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag, LocalTime.NOON));

		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertTrue(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitMontagVorMittag() {

		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag, LocalTime.of(10,0)));

		assertEquals(LocalDateTime.of(testMontag.minusDays(7), LocalTime.of(13, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitMontagBeginnMittag() {

		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag, LocalTime.of(11,30)));

		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertTrue(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitMontagNachMittag() {

		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag, LocalTime.of(14,0)));

		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.of(11, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitMontagEndeMittag() {

		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag, LocalTime.of(13,30)));

		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.of(11, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	
	
	
	@Test
	public void testeGueltigkeitDienstagMittag() {
		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag.plusDays(1), LocalTime.NOON));

		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.of(11, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitSonntagMittag() {
		Gueltigkeit gueltigKeit = verknuepfung.getGueltigKeit(LocalDateTime.of(testMontag.minusDays(1), LocalTime.NOON));

		assertEquals(LocalDateTime.of(testMontag.minusDays(7), LocalTime.of(13, 30)), gueltigKeit.getBeginn().getZeitPunkt());
		assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)),
				gueltigKeit.getNaechsteAenderung().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	}

	
	// @Test
	// public void testeZustandswechselKarfreitag() {
	//
	// TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
	// provider.parseAndAdd(provider, "Ostersonntag", "Ostersonntag");
	// VerweisEintrag karfreitag = (VerweisEintrag) provider.parseAndAdd(provider,
	// "Karfreitag",
	// "Ostersonntag - 2 Tage");
	//
	// LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
	// LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);
	//
	// List<ZustandsWechsel> zustandsWechselImBereich =
	// karfreitag.getZustandsWechselImBereich(start, ende);
	//
	// assertEquals("Erwartete Zustandswechsel", 7,
	// zustandsWechselImBereich.size());
	// for (int index = 0; index < zustandsWechselImBereich.size(); index++) {
	//
	// ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);
	//
	// switch (index) {
	// case 0:
	// assertEquals(start, zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// case 1:
	// assertEquals(LocalDateTime.of(2015, 4, 3, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertTrue(zustandsWechsel.isWirdGueltig());
	// break;
	// case 2:
	// assertEquals(LocalDateTime.of(2015, 4, 4, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// case 3:
	// assertEquals(LocalDateTime.of(2016, 3, 25, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertTrue(zustandsWechsel.isWirdGueltig());
	// break;
	// case 4:
	// assertEquals(LocalDateTime.of(2016, 3, 26, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// case 5:
	// assertEquals(LocalDateTime.of(2017, 4, 14, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertTrue(zustandsWechsel.isWirdGueltig());
	// break;
	// case 6:
	// assertEquals(LocalDateTime.of(2017, 4, 15, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// }
	// }
	// }
	//
	// @Test
	// public void testeGueltigkeitKarfreitag() {
	//
	// TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
	// provider.parseAndAdd(provider, "Ostersonntag", "Ostersonntag");
	// VerweisEintrag karFreitag = (VerweisEintrag) provider.parseAndAdd(provider,
	// "Karfreitag", "Ostersonntag-2Tage");
	//
	// LocalDate now = LocalDate.now();
	//
	// for (int jahr = 2000; jahr < 2020; jahr++) {
	// LocalDate checkDate = LocalDate.of(jahr, now.getMonth(),
	// now.getDayOfMonth());
	// Gueltigkeit gueltigKeit =
	// karFreitag.getGueltigKeit(LocalDateTime.of(checkDate, LocalTime.NOON));
	// LocalDate karfreitagDatum = Ostersonntag.getDatumImJahr(jahr).minusDays(2);
	//
	// if (checkDate.equals(karfreitagDatum)) {
	// assertEquals(LocalDateTime.of(karfreitagDatum, LocalTime.MIDNIGHT),
	// gueltigKeit.getBeginn().getZeitPunkt());
	// assertTrue(gueltigKeit.getBeginn().isWirdGueltig());
	// assertEquals(LocalDateTime.of(karfreitagDatum,
	// LocalTime.MIDNIGHT).plusDays(1),
	// gueltigKeit.getNaechsteAenderung().getZeitPunkt());
	// assertFalse(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	// } else if (checkDate.isBefore(karfreitagDatum)) {
	// assertEquals(LocalDateTime.of(Ostersonntag.getDatumImJahr(jahr -
	// 1).minusDays(2), LocalTime.MIDNIGHT),
	// gueltigKeit.getBeginn().getZeitPunkt());
	// assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
	// assertEquals(LocalDateTime.of(karfreitagDatum, LocalTime.MIDNIGHT),
	// gueltigKeit.getNaechsteAenderung().getZeitPunkt());
	// assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	// } else {
	// assertEquals(LocalDateTime.of(karfreitagDatum,
	// LocalTime.MIDNIGHT).plusDays(1),
	// gueltigKeit.getBeginn().getZeitPunkt());
	// assertFalse(gueltigKeit.getBeginn().isWirdGueltig());
	// assertEquals(LocalDateTime.of(Ostersonntag.getDatumImJahr(jahr +
	// 1).minusDays(2), LocalTime.MIDNIGHT),
	// gueltigKeit.getNaechsteAenderung().getZeitPunkt());
	// assertTrue(gueltigKeit.getNaechsteAenderung().isWirdGueltig());
	// }
	// }
	// }
	//
	// @Test
	// public void testeZustandswechselOstermontag() {
	//
	// TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
	// provider.parseAndAdd(provider, "Ostersonntag", "Ostersonntag");
	// VerweisEintrag osterMontag = (VerweisEintrag) provider.parseAndAdd(provider,
	// "Ostermontag",
	// "Ostersonntag+1Tag");
	//
	// LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
	// LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);
	//
	// List<ZustandsWechsel> zustandsWechselImBereich =
	// osterMontag.getZustandsWechselImBereich(start, ende);
	//
	// assertEquals("Erwartete Zustandswechsel", 7,
	// zustandsWechselImBereich.size());
	// for (int index = 0; index < zustandsWechselImBereich.size(); index++) {
	//
	// ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);
	//
	// switch (index) {
	// case 0:
	// assertEquals(start, zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// case 1:
	// assertEquals(LocalDateTime.of(2015, 4, 6, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertTrue(zustandsWechsel.isWirdGueltig());
	// break;
	// case 2:
	// assertEquals(LocalDateTime.of(2015, 4, 7, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// case 3:
	// assertEquals(LocalDateTime.of(2016, 3, 28, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertTrue(zustandsWechsel.isWirdGueltig());
	// break;
	// case 4:
	// assertEquals(LocalDateTime.of(2016, 3, 29, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// case 5:
	// assertEquals(LocalDateTime.of(2017, 4, 17, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertTrue(zustandsWechsel.isWirdGueltig());
	// break;
	// case 6:
	// assertEquals(LocalDateTime.of(2017, 4, 18, 0, 0),
	// zustandsWechsel.getZeitPunkt());
	// assertFalse(zustandsWechsel.isWirdGueltig());
	// break;
	// }
	// }
	// }

}

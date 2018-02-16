/*
 * SWE Systemkalender - Version 2
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49-341-49067-0
 * Fax: +49-341-49067-15
 * mailto: info@bitctrl.de
 */

package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.OderVerknuepfung;

public class OderVerknuepfungTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	private static TestKalenderEintragProvider provider;
	private static OderVerknuepfung verknuepfung;
	private static LocalDate testMontag;

	@BeforeClass
	public static void init() {
		provider = new TestKalenderEintragProvider();
		verknuepfung = (OderVerknuepfung) provider.parseAndAdd(provider, "MontagOderMittwoch", "ODER{Montag, Mittwoch}");

		testMontag = LocalDate.of(2018, 12, 24);
	}

	//------------------------------------------------------------------------------
	// Tests zur Bestimmung der aktuellen Gültigkeit
	//------------------------------------------------------------------------------
	
	@Test
	public void testeGueltigkeitSonntag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.minusDays(1), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(4), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMontagBeginn() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag, LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMontagMittag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag, LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMontagEnde() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitDienstag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(1), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	
	@Test
	public void testeGueltigkeitMittwochBeginn() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMittwochMittag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(2), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMittwochEnde() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitDonnerstag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(3), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	
	//------------------------------------------------------------------------------
	// Tests zur Bestimmung des vorherigen Gültigkeitswechsels
	//------------------------------------------------------------------------------
	
	@Test
	public void testeGueltigkeitVorSonntag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.minusDays(1), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(5), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(4), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorMontagBeginn() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag, LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(4), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorMontagMittag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag, LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(4), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorMontagEnde() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorDienstag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(1), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	
	@Test
	public void testeGueltigkeitVorMittwochBeginn() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorMittwochMittag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(2), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorMittwochEnde() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}


	@Test
	public void testeGueltigkeitVorDonnerstag() {

		LocalDateTime testDatum = LocalDateTime.of(testMontag.plusDays(3), LocalTime.NOON);
		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeitVor(testDatum);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(2), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(3), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	//------------------------------------------------------------------------------
	// Tests zur Bestimmung von Zustandswechseln
	//------------------------------------------------------------------------------

	@Test
	public void testeZustandswechsel() {

		LocalDateTime start = LocalDateTime.of(2018, 5, 1, 0, 0);
		LocalDateTime ende = LocalDateTime.of(2018, 6, 1, 0, 0);

		TestWechsel[] erwarteteWechsel = {
			TestWechsel.of("1.5.2018 00:00", false),
			TestWechsel.of("2.5.2018 00:00", true),
			TestWechsel.of("3.5.2018 00:00", false),
			TestWechsel.of("7.5.2018 00:00", true),
			TestWechsel.of("8.5.2018 00:00", false),
			TestWechsel.of("9.5.2018 00:00", true),
			TestWechsel.of("10.5.2018 00:00", false),
			TestWechsel.of("14.5.2018 00:00", true),
			TestWechsel.of("15.5.2018 00:00", false),
			TestWechsel.of("16.5.2018 00:00", true),
			TestWechsel.of("17.5.2018 00:00", false),
			TestWechsel.of("21.5.2018 00:00", true),
			TestWechsel.of("22.5.2018 00:00", false),
			TestWechsel.of("23.5.2018 00:00", true),
			TestWechsel.of("24.5.2018 00:00", false),
			TestWechsel.of("28.5.2018 00:00", true),
			TestWechsel.of("29.5.2018 00:00", false),
			TestWechsel.of("30.5.2018 00:00", true),
			TestWechsel.of("31.5.2018 00:00", false)
		};
		
		List<ZustandsWechsel> zustandsWechselImBereich = verknuepfung.getZustandsWechsel(start, ende);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechselImBereich);
	}
}

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
import de.bsvrz.vew.syskal.internal.KalenderEintragImpl;
import de.bsvrz.vew.syskal.internal.UndVerknuepfung;

public class UndVerknuepfungTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

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

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag, LocalTime.NOON));
		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMontagVorMittag() {

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung
				.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag, LocalTime.of(10, 0)));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(7), LocalTime.of(13,30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMontagBeginnMittag() {

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung
				.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag, LocalTime.of(11, 30)));

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitMontagNachMittag() {

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung
				.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag, LocalTime.of(14, 0)));
		
		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.of(11, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitMontagEndeMittag() {

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung
				.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag, LocalTime.of(13, 30)));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13, 30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());

		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.of(11, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
	}

	@Test
	public void testeGueltigkeitDienstagMittag() {

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung
				.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag.plusDays(1), LocalTime.NOON));

		assertFalse(gueltigKeit.getErsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(13,30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(testMontag.plusDays(7), LocalTime.of(11, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitSonntagMittag() {

		SystemkalenderGueltigkeit gueltigKeit = verknuepfung
				.getZeitlicheGueltigkeit(LocalDateTime.of(testMontag.minusDays(1), LocalTime.NOON));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(testMontag.minusDays(7), LocalTime.of(13, 30)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertEquals(LocalDateTime.of(testMontag, LocalTime.of(11, 30)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
	}

	@Test
	public void testeZustandswechsel() {

		LocalDateTime start = LocalDateTime.of(2018, 5, 1, 0, 0);
		LocalDateTime ende = LocalDateTime.of(2018, 6, 1, 0, 0);

		TestWechsel[] erwarteteWechsel = {
			TestWechsel.of("1.5.2018 00:00", false),
			TestWechsel.of("7.5.2018 11:30", true),
			TestWechsel.of("7.5.2018 13:30", false),
			TestWechsel.of("14.5.2018 11:30", true),
			TestWechsel.of("14.5.2018 13:30", false),
			TestWechsel.of("21.5.2018 11:30", true),
			TestWechsel.of("21.5.2018 13:30", false),
			TestWechsel.of("28.5.2018 11:30", true),
			TestWechsel.of("28.5.2018 13:30", false),
		};
		
		List<ZustandsWechsel> zustandsWechselImBereich = verknuepfung.getZustandsWechsel(start, ende);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechselImBereich);
	}
	
	@Test
	public void testeGueltigkeitVor() {

		LocalDateTime abfrageZeitpunkt = LocalDateTime.of(testMontag, LocalTime.MIDNIGHT);
		SystemkalenderGueltigkeit gueltigkeit = verknuepfung.getZeitlicheGueltigkeitVor(abfrageZeitpunkt);
		assertTrue(gueltigkeit.getErsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 17), LocalTime.of(11,30)), gueltigkeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 17), LocalTime.of(13,30)), gueltigkeit.getNaechsterWechsel().getZeitPunkt());

		abfrageZeitpunkt = LocalDateTime.of(testMontag, LocalTime.of(11,30));
		gueltigkeit = verknuepfung.getZeitlicheGueltigkeitVor(abfrageZeitpunkt);
		assertFalse(gueltigkeit.getErsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 17), LocalTime.of(13,30)), gueltigkeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 24), LocalTime.of(11,30)), gueltigkeit.getNaechsterWechsel().getZeitPunkt());

		abfrageZeitpunkt = LocalDateTime.of(testMontag, LocalTime.NOON);
		gueltigkeit = verknuepfung.getZeitlicheGueltigkeitVor(abfrageZeitpunkt);
		assertFalse(gueltigkeit.getErsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 17), LocalTime.of(13,30)), gueltigkeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 24), LocalTime.of(11,30)), gueltigkeit.getNaechsterWechsel().getZeitPunkt());

		abfrageZeitpunkt = LocalDateTime.of(testMontag, LocalTime.of(13,30));
		gueltigkeit = verknuepfung.getZeitlicheGueltigkeitVor(abfrageZeitpunkt);
		assertTrue(gueltigkeit.getErsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 24), LocalTime.of(11,30)), gueltigkeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 24), LocalTime.of(13,30)), gueltigkeit.getNaechsterWechsel().getZeitPunkt());

		abfrageZeitpunkt = LocalDateTime.of(2018, 12, 24, 15, 0);
		gueltigkeit = verknuepfung.getZeitlicheGueltigkeitVor(abfrageZeitpunkt);
		assertTrue(gueltigkeit.getErsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 24), LocalTime.of(11,30)), gueltigkeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 12, 24), LocalTime.of(13,30)), gueltigkeit.getNaechsterWechsel().getZeitPunkt());
	}

	
}

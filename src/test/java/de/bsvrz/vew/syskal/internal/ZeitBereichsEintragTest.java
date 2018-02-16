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

package de.bsvrz.vew.syskal.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

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
		assertEquals(LocalDateTime.of(2008, 1, 30, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 15, 30), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitImBereich() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		SystemkalenderGueltigkeit gueltigKeit = bereich4.getZeitlicheGueltigkeit(LocalDateTime.of(2008, 1, 30, 10, 10));

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 9, 0), gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 30, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorDemBereich() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		SystemkalenderGueltigkeit gueltigKeit = bereich4.getZeitlicheGueltigkeit(LocalDateTime.of(2008, 1, 1, 14, 10));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(SystemKalender.MIN_DATETIME, gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 15, 9, 0), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitVorDemBereichAmAnfangsTag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		SystemkalenderGueltigkeit gueltigKeit = bereich4.getZeitlicheGueltigkeit(LocalDateTime.of(2008, 1, 15, 8, 0));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(SystemKalender.MIN_DATETIME, gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 15, 9, 0), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeGueltigkeitAmAnfangsTagZwischenBereichen() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		SystemkalenderGueltigkeit gueltigKeit = bereich4.getZeitlicheGueltigkeit(LocalDateTime.of(2008, 1, 15, 13, 0));

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 15, 11, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999)),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(2008, 1, 15, 15, 30), gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeZustandswechsel() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		LocalDateTime start = LocalDateTime.of(2008, 1, 30, 12, 10);
		List<ZustandsWechsel> zustandsWechselListe = bereich4.getZustandsWechsel(start,
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

	@Test
	public void testZustandsWechselMitUeberlappungen() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag eintrag = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Aller5Minuten",
				"Aller5Minuten:=<23.12.2000 00:00:00,000-25.12.2000 01:00:00,000>({11:05:00,000-11:10:00,000}{11:15:00,000-11:20:00,000}{11:25:00,000-11:30:00,000}{11:35:00,000-11:40:00,000}{11:45:00,000-11:49:00,000}{14:01:00,000-15:00:00,000}{14:35:00,000-14:40:00,000}{14:45:00,000-15:09:00,000}{14:55:00,000-15:00:00,000}{15:05:00,000-15:10:00,000}{15:15:00,000-15:20:00,000}{15:25:00,000-15:30:00,000})");

		LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 11, 0);
		LocalDateTime endTime = LocalDateTime.of(2000, 12, 24, 23, 0);

		TestWechsel[] erwarteteWechsel = { TestWechsel.of("24.12.2000 11:00", false),
				TestWechsel.of("24.12.2000 11:05", true), TestWechsel.of("24.12.2000 11:10", false),
				TestWechsel.of("24.12.2000 11:15", true), TestWechsel.of("24.12.2000 11:20", false),
				TestWechsel.of("24.12.2000 11:25", true), TestWechsel.of("24.12.2000 11:30", false),
				TestWechsel.of("24.12.2000 11:35", true), TestWechsel.of("24.12.2000 11:40", false),
				TestWechsel.of("24.12.2000 11:45", true), TestWechsel.of("24.12.2000 11:49", false),
				TestWechsel.of("24.12.2000 14:01", true), TestWechsel.of("24.12.2000 15:10", false),
				TestWechsel.of("24.12.2000 15:15", true), TestWechsel.of("24.12.2000 15:20", false),
				TestWechsel.of("24.12.2000 15:25", true), TestWechsel.of("24.12.2000 15:30", false) };

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);

	}
}

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
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class OstersonntagTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	private static TestKalenderEintragProvider provider;
	private static Ostersonntag osterSonntag;

	@BeforeClass
	public static void init() {
		provider = new TestKalenderEintragProvider();
		osterSonntag = (Ostersonntag) provider.getKalenderEintrag("Ostersonntag");
	}

	@Test
	public void testeGetDatumImJahr() {

		for (int jahr = 2000; jahr < 2020; jahr++) {
			LocalDate date = Ostersonntag.getDatumImJahr(jahr);
			assertEquals(date.getYear(), jahr);
			switch (jahr) {
			case 2000:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 23);
				break;
			case 2001:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 15);
				break;
			case 2002:
				assertEquals(date.getMonth(), Month.MARCH);
				assertEquals(date.getDayOfMonth(), 31);
				break;
			case 2003:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 20);
				break;
			case 2004:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 11);
				break;
			case 2005:
				assertEquals(date.getMonth(), Month.MARCH);
				assertEquals(date.getDayOfMonth(), 27);
				break;
			case 2006:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 16);
				break;
			case 2007:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 8);
				break;
			case 2008:
				assertEquals(date.getMonth(), Month.MARCH);
				assertEquals(date.getDayOfMonth(), 23);
				break;
			case 2009:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 12);
				break;
			case 2010:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 4);
				break;
			case 2011:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 24);
				break;
			case 2012:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 8);
				break;
			case 2013:
				assertEquals(date.getMonth(), Month.MARCH);
				assertEquals(date.getDayOfMonth(), 31);
				break;
			case 2014:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 20);
				break;
			case 2015:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 5);
				break;
			case 2016:
				assertEquals(date.getMonth(), Month.MARCH);
				assertEquals(date.getDayOfMonth(), 27);
				break;
			case 2017:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 16);
				break;
			case 2018:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 1);
				break;
			case 2019:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 21);
				break;
			case 2020:
				assertEquals(date.getMonth(), Month.APRIL);
				assertEquals(date.getDayOfMonth(), 12);
				break;
			default:
				fail("Unerwartetes Testjahr: " + jahr);
				break;
			}
		}
	}

	@Test
	public void testeZustandswechsel() {

		LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);

		List<ZustandsWechsel> zustandsWechselImBereich = osterSonntag.getZustandsWechsel(start, ende);

		assertEquals("Erwartete Zustandswechsel", 7, zustandsWechselImBereich.size());
		for (int index = 0; index < zustandsWechselImBereich.size(); index++) {

			ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);

			switch (index) {
			case 0:
				assertEquals(start, zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 1:
				assertEquals(LocalDateTime.of(2015, 4, 5, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 2:
				assertEquals(LocalDateTime.of(2015, 4, 6, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 3:
				assertEquals(LocalDateTime.of(2016, 3, 27, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 4:
				assertEquals(LocalDateTime.of(2016, 3, 28, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 5:
				assertEquals(LocalDateTime.of(2017, 4, 16, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 6:
				assertEquals(LocalDateTime.of(2017, 4, 17, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			default:
				break;
			}
		}
	}
}

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.Ostersonntag;
import de.bsvrz.vew.syskal.internal.VerweisEintrag;

public class VerweisEintragTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);
	
	@Test
	public void testeGueltigkeitOsterMontag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag osterMontag = (VerweisEintrag) provider.parseAndAdd(provider, "Ostermontag",
				"Ostersonntag+1Tag");

		LocalDate now = LocalDate.now();

		for (int jahr = 2000; jahr < 2020; jahr++) {
			LocalDate checkDate = LocalDate.of(jahr, now.getMonth(), now.getDayOfMonth());
			SystemkalenderGueltigkeit gueltigKeit = osterMontag.getZeitlicheGueltigkeit(LocalDateTime.of(checkDate, LocalTime.NOON));
			LocalDate osterMontagDate = Ostersonntag.getDatumImJahr(jahr).plusDays(1);

			if (checkDate.equals(osterMontagDate)) {
				assertTrue(gueltigKeit.isZeitlichGueltig());
				assertEquals(LocalDateTime.of(osterMontagDate, LocalTime.MIDNIGHT).plusDays(1),
						gueltigKeit.getNaechsterWechsel().getZeitPunkt());
				assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
			} else if (checkDate.isBefore(osterMontagDate)) {
				assertFalse(gueltigKeit.isZeitlichGueltig());
				assertEquals(LocalDateTime.of(osterMontagDate, LocalTime.MIDNIGHT),
						gueltigKeit.getNaechsterWechsel().getZeitPunkt());
				assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
			} else {
				assertFalse(gueltigKeit.isZeitlichGueltig());
				assertEquals(LocalDateTime.of(Ostersonntag.getDatumImJahr(jahr + 1).plusDays(1), LocalTime.MIDNIGHT),
						gueltigKeit.getNaechsterWechsel().getZeitPunkt());
				assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
			}
		}
	}

	@Test
	public void testeZustandswechselKarfreitag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag karfreitag = (VerweisEintrag) provider.parseAndAdd(provider, "Karfreitag",
				"Ostersonntag - 2 Tage");

		LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);

		List<ZustandsWechsel> zustandsWechselImBereich = karfreitag.getZustandsWechsel(start, ende);

		assertEquals("Erwartete Zustandswechsel", 7, zustandsWechselImBereich.size());
		for (int index = 0; index < zustandsWechselImBereich.size(); index++) {

			ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);

			switch (index) {
			case 0:
				assertEquals(start, zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 1:
				assertEquals(LocalDateTime.of(2015, 4, 3, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 2:
				assertEquals(LocalDateTime.of(2015, 4, 4, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 3:
				assertEquals(LocalDateTime.of(2016, 3, 25, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 4:
				assertEquals(LocalDateTime.of(2016, 3, 26, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 5:
				assertEquals(LocalDateTime.of(2017, 4, 14, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 6:
				assertEquals(LocalDateTime.of(2017, 4, 15, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			default:
				break;
			}
		}
	}


	@Test
	public void testeGueltigkeitKarfreitag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag karFreitag = (VerweisEintrag) provider.parseAndAdd(provider, "Karfreitag",
				"Ostersonntag-2Tage");

		LocalDate now = LocalDate.now();

		for (int jahr = 2000; jahr < 2020; jahr++) {
			LocalDate checkDate = LocalDate.of(jahr, now.getMonth(), now.getDayOfMonth());
			SystemkalenderGueltigkeit gueltigKeit = karFreitag.getZeitlicheGueltigkeit(LocalDateTime.of(checkDate, LocalTime.NOON));
			LocalDate karfreitagDatum = Ostersonntag.getDatumImJahr(jahr).minusDays(2);

			if (checkDate.equals(karfreitagDatum)) {
				assertTrue(gueltigKeit.isZeitlichGueltig());
				assertEquals(LocalDateTime.of(karfreitagDatum, LocalTime.MIDNIGHT).plusDays(1),
						gueltigKeit.getNaechsterWechsel().getZeitPunkt());
				assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
			} else if (checkDate.isBefore(karfreitagDatum)) {
				assertFalse(gueltigKeit.isZeitlichGueltig());
				assertEquals(LocalDateTime.of(karfreitagDatum, LocalTime.MIDNIGHT),
						gueltigKeit.getNaechsterWechsel().getZeitPunkt());
				assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
			} else {
				assertFalse(gueltigKeit.isZeitlichGueltig());
				assertEquals(LocalDateTime.of(Ostersonntag.getDatumImJahr(jahr + 1).minusDays(2), LocalTime.MIDNIGHT),
						gueltigKeit.getNaechsterWechsel().getZeitPunkt());
				assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
			}
		}
	}

	@Test
	public void testeZustandswechselOstermontag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag osterMontag = (VerweisEintrag) provider.parseAndAdd(provider, "Ostermontag",
				"Ostersonntag+1Tag");

		LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);

		List<ZustandsWechsel> zustandsWechselImBereich = osterMontag.getZustandsWechsel(start, ende);

		assertEquals("Erwartete Zustandswechsel", 7, zustandsWechselImBereich.size());
		for (int index = 0; index < zustandsWechselImBereich.size(); index++) {

			ZustandsWechsel zustandsWechsel = zustandsWechselImBereich.get(index);

			switch (index) {
			case 0:
				assertEquals(start, zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 1:
				assertEquals(LocalDateTime.of(2015, 4, 6, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 2:
				assertEquals(LocalDateTime.of(2015, 4, 7, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 3:
				assertEquals(LocalDateTime.of(2016, 3, 28, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 4:
				assertEquals(LocalDateTime.of(2016, 3, 29, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			case 5:
				assertEquals(LocalDateTime.of(2017, 4, 17, 0, 0), zustandsWechsel.getZeitPunkt());
				assertTrue(zustandsWechsel.isWirdGueltig());
				break;
			case 6:
				assertEquals(LocalDateTime.of(2017, 4, 18, 0, 0), zustandsWechsel.getZeitPunkt());
				assertFalse(zustandsWechsel.isWirdGueltig());
				break;
			default:
				break;
			}
		}
	}


}

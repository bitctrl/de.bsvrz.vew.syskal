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

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class ZeitBereichsEintragTest {
	
//	@Rule
//	public Timeout globalTimeout = Timeout.seconds(5);

	@Test
	public void testeZustandswechsel() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		LocalDateTime startTime = LocalDateTime.of(2008, 1, 30, 12, 10);
		LocalDateTime endTime = LocalDateTime.of(2008, 2, 1, 11, 11);
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("30.01.2008 11:59:59.999", false),
				TestWechsel.of("30.01.2008 15:30", true), 
				TestWechsel.of("30.01.2008 17:59:59.999", false),
				TestWechsel.of("31.01.2008 09:00", true), 
				TestWechsel.of("31.01.2008 11:59:59.999", false),
				TestWechsel.of("31.01.2008 15:30", true), 
				TestWechsel.of("31.01.2008 17:59:59.999", false),
				TestWechsel.of("01.02.2008 09:00", true)
			};

		List<ZustandsWechsel> zustandsWechsel = bereich4.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void testZustandsWechselMitUeberlappungen() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag eintrag = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Aller5Minuten",
				"Aller5Minuten:=<23.12.2000 00:00:00,000-25.12.2000 01:00:00,000>({11:05:00,000-11:10:00,000}{11:15:00,000-11:20:00,000}{11:25:00,000-11:30:00,000}{11:35:00,000-11:40:00,000}{11:45:00,000-11:49:00,000}{14:01:00,000-15:00:00,000}{14:35:00,000-14:40:00,000}{14:45:00,000-15:09:00,000}{14:55:00,000-15:00:00,000}{15:05:00,000-15:10:00,000}{15:15:00,000-15:20:00,000}{15:25:00,000-15:30:00,000})");

		LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 11, 0);
		LocalDateTime endTime = LocalDateTime.of(2000, 12, 24, 23, 0);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("23.12.2000 15:30", false),
				TestWechsel.of("24.12.2000 11:05", true), 
				TestWechsel.of("24.12.2000 11:10", false),
				TestWechsel.of("24.12.2000 11:15", true), 
				TestWechsel.of("24.12.2000 11:20", false),
				TestWechsel.of("24.12.2000 11:25", true), 
				TestWechsel.of("24.12.2000 11:30", false),
				TestWechsel.of("24.12.2000 11:35", true), 
				TestWechsel.of("24.12.2000 11:40", false),
				TestWechsel.of("24.12.2000 11:45", true), 
				TestWechsel.of("24.12.2000 11:49", false),
				TestWechsel.of("24.12.2000 14:01", true), 
				TestWechsel.of("24.12.2000 15:10", false),
				TestWechsel.of("24.12.2000 15:15", true), 
				TestWechsel.of("24.12.2000 15:20", false),
				TestWechsel.of("24.12.2000 15:25", true), 
				TestWechsel.of("24.12.2000 15:30", false) };

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);

	}

	@Test
	public void testeIntervalle() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		ZeitBereichsEintrag bereich4 = (ZeitBereichsEintrag) provider.parseAndAdd(provider, "Bereich4",
				"Bereich4:=<15.01.2008 12:00:30 - 15.02.2008 14:12:45.555>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

		LocalDateTime startTime = LocalDateTime.of(2008, 1, 14, 12, 10);
		LocalDateTime endTime = LocalDateTime.of(2008, 1, 18, 11, 11);
		
		Intervall[] erwarteteIntervalle = { 
				TestIntervall.of("15.01.2008 15:30", "15.01.2008 17:59:59.999"),
				TestIntervall.of("16.01.2008 09:00", "16.01.2008 11:59:59.999"),
				TestIntervall.of("16.01.2008 15:30", "16.01.2008 17:59:59.999"),
				TestIntervall.of("17.01.2008 09:00", "17.01.2008 11:59:59.999"),
				TestIntervall.of("17.01.2008 15:30", "17.01.2008 17:59:59.999"),
				TestIntervall.of("18.01.2008 09:00", "18.01.2008 11:11")
			};

		List<Intervall> intervalle = bereich4.getIntervalle(startTime, endTime);
		TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
	}
}

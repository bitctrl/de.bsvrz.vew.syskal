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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class VerweisEintragTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	@Test
	public void testeZustandswechselKarfreitag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag karfreitag = (VerweisEintrag) provider.parseAndAdd(provider, "Karfreitag",
				"Ostersonntag - 2 Tage");

		LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2018, 2, 28, 12, 0);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("19.04.2014 00:00", false),
				TestWechsel.of("03.04.2015 00:00", true), 
				TestWechsel.of("04.04.2015 00:00", false),
				TestWechsel.of("25.03.2016 00:00", true), 
				TestWechsel.of("26.03.2016 00:00", false),
				TestWechsel.of("14.04.2017 00:00", true), 
				TestWechsel.of("15.04.2017 00:00", false)
			};

		List<ZustandsWechsel> zustandsWechsel = karfreitag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void testeIntervalleKarfreitag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag karfreitag = (VerweisEintrag) provider.parseAndAdd(provider, "Karfreitag",
				"Ostersonntag - 2 Tage");

		LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2018, 2, 28, 12, 0);

		Intervall[] erwarteteIntervalle = { 
				TestIntervall.of("03.04.2015 00:00", "04.04.2015 00:00"),
				TestIntervall.of("25.03.2016 00:00", "26.03.2016 00:00"),
				TestIntervall.of("14.04.2017 00:00", "15.04.2017 00:00")
			};

		List<Intervall> intervalle = karfreitag.getIntervalle(startTime, endTime);
		TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
	}
	
	@Test
	public void testeZustandswechselOstermontag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag osterMontag = (VerweisEintrag) provider.parseAndAdd(provider, "Ostermontag",
				"Ostersonntag+1Tag");

		LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2018, 2, 28, 12, 0);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("22.04.2014 00:00", false),
				TestWechsel.of("06.04.2015 00:00", true), 
				TestWechsel.of("07.04.2015 00:00", false),
				TestWechsel.of("28.03.2016 00:00", true), 
				TestWechsel.of("29.03.2016 00:00", false),
				TestWechsel.of("17.04.2017 00:00", true), 
				TestWechsel.of("18.04.2017 00:00", false)
			};

		List<ZustandsWechsel> zustandsWechsel = osterMontag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void testeIntervalleOstermontag() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		provider.parseAndAdd(provider, "Ostersonntag",
				"Ostersonntag");
		VerweisEintrag osterMontag = (VerweisEintrag) provider.parseAndAdd(provider, "Ostermontag",
				"Ostersonntag+1Tag");

		LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2018, 2, 28, 12, 0);

		Intervall[] erwarteteIntervalle = { 
				TestIntervall.of("06.04.2015 00:00", "07.04.2015 00:00"),
				TestIntervall.of("28.03.2016 00:00", "29.03.2016 00:00"),
				TestIntervall.of("17.04.2017 00:00", "18.04.2017 00:00")
			};

		List<Intervall> intervalle = osterMontag.getIntervalle(startTime, endTime);
		TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
	}

}

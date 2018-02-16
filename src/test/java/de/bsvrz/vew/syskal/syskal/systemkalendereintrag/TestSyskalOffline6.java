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

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintragImpl;

public class TestSyskalOffline6 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	@Test
	public void beispiel6() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "TestSKE", "TestSKE:=Montag+1Tag"));

		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("TestSKE");
		LocalDateTime startTime = LocalDateTime.of(2014, 1, 13, 14, 27, 17);
		LocalDateTime endTime = LocalDateTime.of(2014, 3, 14, 14, 28, 17);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("13.1.2014 14:27:17", false),
				TestWechsel.of("14.1.2014 00:00", true), 
				TestWechsel.of("15.1.2014 00:00", false),
				TestWechsel.of("21.1.2014 00:00", true), 
				TestWechsel.of("22.1.2014 00:00", false),
				TestWechsel.of("28.1.2014 00:00", true), 
				TestWechsel.of("29.1.2014 00:00", false),
				TestWechsel.of("4.2.2014 00:00", true), 
				TestWechsel.of("5.2.2014 00:00", false),
				TestWechsel.of("11.2.2014 00:00", true), 
				TestWechsel.of("12.2.2014 00:00", false),
				TestWechsel.of("18.2.2014 00:00", true), 
				TestWechsel.of("19.2.2014 00:00", false),
				TestWechsel.of("25.2.2014 00:00", true), 
				TestWechsel.of("26.2.2014 00:00", false),
				TestWechsel.of("4.3.2014 00:00", true), 
				TestWechsel.of("5.3.2014 00:00", false),
				TestWechsel.of("11.3.2014 00:00", true), 
				TestWechsel.of("12.3.2014 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

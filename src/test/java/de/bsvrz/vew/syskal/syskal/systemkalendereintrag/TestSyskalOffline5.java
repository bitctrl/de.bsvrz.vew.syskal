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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline5 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);
	
	private static TestKalenderEintragProvider eintragsProvider;

	@BeforeClass
	public static void init() {

		eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "WF", "WF:=<24.12.2012-04.01.2013>"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "WFD", "WFD:=UND{Donnerstag,WF}*,*"));
	}

	@Test
	public void testeZustandsWechsel() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("WFD");
		LocalDateTime startTime = LocalDateTime.of(2012, 12, 20, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2012, 12, 31, 0, 0, 0);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("20.12.2012 00:00", false),
				TestWechsel.of("27.12.2012 00:00", true), 
				TestWechsel.of("28.12.2012 00:00", false) 
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void testeGueltigkeit() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("WFD");
		LocalDateTime startTime = LocalDateTime.of(2012, 12, 27, 10, 0, 0);
		SystemkalenderGueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(startTime);

		assertTrue("Gültigkeit", gueltigkeit.isZeitlichGueltig());
		assertFalse("Statuswechsel", gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals("Wechselzeitpunkt", LocalDateTime.of(2012, 12, 28, 0, 0),
				gueltigkeit.getNaechsterWechsel().getZeitPunkt());
	}
}

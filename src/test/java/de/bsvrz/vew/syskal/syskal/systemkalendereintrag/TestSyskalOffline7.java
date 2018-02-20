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

public class TestSyskalOffline7 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);
	
	@Test
	public void beispiele7() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Neujahr", "Neujahr:=01.01.*,*"));
		eintragsProvider.addEintrag(
				KalenderEintragImpl.parse(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2Tage"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Einheit", "Einheit:=03.10.*,*"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Arbeit", "Arbeit:=01.05.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Allerheil", "Allerheil:=01.11.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Weihnacht1", "Weihnacht1:=25.12.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Weihnacht2", "Weihnacht2:=26.12.*,*"));
		eintragsProvider.addEintrag(
				KalenderEintragImpl.parse(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Himmel", "Himmel:=Ostersonntag+39Tage"));
		eintragsProvider.addEintrag(
				KalenderEintragImpl.parse(eintragsProvider, "PfingstMo", "PfingstMo:=Ostersonntag+50Tage"));
		eintragsProvider.addEintrag(
				KalenderEintragImpl.parse(eintragsProvider, "Fronleich", "Fronleich:=Ostersonntag+60Tage"));

		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Feiertag",
				"Feiertag:=ODER{Neujahr,Karfreitag,Ostersonntag,Ostermontag,Arbeit,Himmel,PfingstMo,Fronleich,Einheit,Allerheil,Weihnacht1,Weihnacht2}*,*"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "MoBisSa",
				"MoBisSa:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag}*,*"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "MoBisFr",
				"MoBisFr:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag}*,*"));

		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Werktag",
				"Werktag:=UND{MoBisFr,NICHT Feiertag}*,*"));

		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Werktag");
		LocalDateTime startTime = LocalDateTime.of(2014, 8, 1, 14, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2014, 8, 31, 0, 0, 0);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("28.7.2014 00:00", true),
				TestWechsel.of("2.8.2014 00:00", false), 
				TestWechsel.of("4.8.2014 00:00", true),
				TestWechsel.of("9.8.2014 00:00", false), 
				TestWechsel.of("11.8.2014 00:00", true),
				TestWechsel.of("16.8.2014 00:00", false),
				TestWechsel.of("18.8.2014 00:00", true), 
				TestWechsel.of("23.8.2014 00:00", false),
				TestWechsel.of("25.8.2014 00:00", true), 
				TestWechsel.of("30.8.2014 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

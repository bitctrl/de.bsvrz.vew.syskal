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
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintragImpl;

public class TestSyskalOffline4 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	private static TestKalenderEintragProvider eintragsProvider;

	@BeforeClass
	public static void init() {

		eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test1", "Test1:=29.02.2001,2010"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test2", "Test2:=29.02.2004,2010"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test3",
				"Test3:=({13:00:00,000-15:00:00,000}{14:00:00,000-16:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test31",
				"Test31:=({13:00:00,000-14:00:00,000}{14:00:00,000-15:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test32",
				"Test32:=({13:00:00,000-14:00:00,000}{15:00:00,000-16:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test4", "Test4:=Ostersonntag+1Tag"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test5", "Test5:=Ostersonntag-4Tage"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Karfreitag", "Karfreitag:=Ostermontag-3Tage"));
		eintragsProvider
				.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test6", "Test6:=UND{NICHT Ostersonntag}*,*"));
		eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Test7", "Test7:=UND{Test32}*,*"));
	}

	@Test
	public void test1() {

		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test1");
		LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.1.1000 00:00", false), 
				TestWechsel.of("29.2.2004 00:00", true), 
				TestWechsel.of("1.3.2004 00:00", false), 
				TestWechsel.of("29.2.2008 00:00", true), 
				TestWechsel.of("1.3.2008 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test2() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test2");
		LocalDateTime startTime = LocalDateTime.of(2004, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.1.1000 00:00", false), 
				TestWechsel.of("29.2.2004 00:00", true), 
				TestWechsel.of("1.3.2004 00:00", false), 
				TestWechsel.of("29.2.2008 00:00", true), 
				TestWechsel.of("1.3.2008 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test3() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test3");
		LocalDateTime startTime = LocalDateTime.of(2004, 3, 10, 13, 30, 0);
		LocalDateTime endTime = LocalDateTime.of(2004, 3, 10, 15, 29, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("10.3.2004 13:00", true)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test31() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test31");
		LocalDateTime startTime = LocalDateTime.of(2004, 3, 10, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2004, 3, 11, 15, 29, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("09.3.2004 15:00", false),
				TestWechsel.of("10.3.2004 13:00", true),
				TestWechsel.of("10.3.2004 15:00", false),
				TestWechsel.of("11.3.2004 13:00", true),
				TestWechsel.of("11.3.2004 15:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void osterSonntag() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Ostersonntag");
		LocalDateTime startTime = LocalDateTime.of(2000, 1, 11, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("5.4.1999 00:00", false), 
				TestWechsel.of("23.4.2000 00:00", true), 
				TestWechsel.of("24.4.2000 00:00", false), 
				TestWechsel.of("15.4.2001 00:00", true), 
				TestWechsel.of("16.4.2001 00:00", false), 
				TestWechsel.of("31.3.2002 00:00", true), 
				TestWechsel.of("1.4.2002 00:00", false), 
				TestWechsel.of("20.4.2003 00:00", true), 
				TestWechsel.of("21.4.2003 00:00", false), 
				TestWechsel.of("11.4.2004 00:00", true), 
				TestWechsel.of("12.4.2004 00:00", false), 
				TestWechsel.of("27.3.2005 00:00", true), 
				TestWechsel.of("28.3.2005 00:00", false), 
				TestWechsel.of("16.4.2006 00:00", true), 
				TestWechsel.of("17.4.2006 00:00", false), 
				TestWechsel.of("8.4.2007 00:00", true), 
				TestWechsel.of("9.4.2007 00:00", false), 
				TestWechsel.of("23.3.2008 00:00", true), 
				TestWechsel.of("24.3.2008 00:00", false), 
				TestWechsel.of("12.4.2009 00:00", true), 
				TestWechsel.of("13.4.2009 00:00", false), 
				TestWechsel.of("4.4.2010 00:00", true), 
				TestWechsel.of("5.4.2010 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test4() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test4");
		LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2003, 12, 31, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("6.4.1999 00:00", false), 
				TestWechsel.of("24.4.2000 00:00", true), 
				TestWechsel.of("25.4.2000 00:00", false), 
				TestWechsel.of("16.4.2001 00:00", true), 
				TestWechsel.of("17.4.2001 00:00", false), 
				TestWechsel.of("1.4.2002 00:00", true), 
				TestWechsel.of("2.4.2002 00:00", false), 
				TestWechsel.of("21.4.2003 00:00", true), 
				TestWechsel.of("22.4.2003 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test5() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test5");
		LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2003, 12, 31, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.4.1999 00:00", false), 
				TestWechsel.of("19.4.2000 00:00", true), 
				TestWechsel.of("20.4.2000 00:00", false), 
				TestWechsel.of("11.4.2001 00:00", true), 
				TestWechsel.of("12.4.2001 00:00", false), 
				TestWechsel.of("27.3.2002 00:00", true), 
				TestWechsel.of("28.3.2002 00:00", false), 
				TestWechsel.of("16.4.2003 00:00", true), 
				TestWechsel.of("17.4.2003 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void karfreitag() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Karfreitag");
		LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2003, 12, 31, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("3.4.1999 00:00", false), 
				TestWechsel.of("21.4.2000 00:00", true), 
				TestWechsel.of("22.4.2000 00:00", false), 
				TestWechsel.of("13.4.2001 00:00", true), 
				TestWechsel.of("14.4.2001 00:00", false), 
				TestWechsel.of("29.3.2002 00:00", true), 
				TestWechsel.of("30.3.2002 00:00", false), 
				TestWechsel.of("18.4.2003 00:00", true), 
				TestWechsel.of("19.4.2003 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test6() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test6");
		LocalDateTime startTime = LocalDateTime.of(2001, 4, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2001, 5, 1, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("24.4.2000 00:00", true), 
				TestWechsel.of("15.4.2001 00:00", false), 
				TestWechsel.of("16.4.2001 00:00", true), 
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void test7() {
		KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Test7");
		LocalDateTime startTime = LocalDateTime.of(2011, 9, 19, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2011, 9, 23, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
	
		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("18.9.2011 16:00", false), 
				TestWechsel.of("19.9.2011 13:00", true), 
				TestWechsel.of("19.9.2011 14:00", false), 
				TestWechsel.of("19.9.2011 15:00", true), 
				TestWechsel.of("19.9.2011 16:00", false), 
				TestWechsel.of("20.9.2011 13:00", true), 
				TestWechsel.of("20.9.2011 14:00", false), 
				TestWechsel.of("20.9.2011 15:00", true), 
				TestWechsel.of("20.9.2011 16:00", false), 
				TestWechsel.of("21.9.2011 13:00", true), 
				TestWechsel.of("21.9.2011 14:00", false), 
				TestWechsel.of("21.9.2011 15:00", true), 
				TestWechsel.of("21.9.2011 16:00", false), 
				TestWechsel.of("22.9.2011 13:00", true), 
				TestWechsel.of("22.9.2011 14:00", false), 
				TestWechsel.of("22.9.2011 15:00", true), 
				TestWechsel.of("22.9.2011 16:00", false), 
				TestWechsel.of("23.9.2011 13:00", true), 
				TestWechsel.of("23.9.2011 14:00", false), 
				TestWechsel.of("23.9.2011 15:00", true), 
				TestWechsel.of("23.9.2011 16:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

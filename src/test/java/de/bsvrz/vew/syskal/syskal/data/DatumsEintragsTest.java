package de.bsvrz.vew.syskal.syskal.data;

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
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.DatumsEintrag;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class DatumsEintragsTest {

//	@Rule
//	public Timeout globalTimeout = Timeout.seconds(5);

	@Test
	public void testeTagDerArbeit() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		KalenderEintrag datumsEintrag = KalenderEintrag.parse(provider, "Mai1", "1.5.*,*");

		assertTrue(datumsEintrag instanceof DatumsEintrag);

		LocalDateTime checkDate = LocalDateTime.of(2018, 4, 15, 12, 0);
		SystemkalenderGueltigkeit gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2017, 5, 2), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 5, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());

		checkDate = LocalDateTime.of(2018, 5, 1, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 5, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 5, 2), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());

		checkDate = LocalDateTime.of(2018, 5, 1, 11, 46);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 5, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 5, 2), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());

		checkDate = LocalDateTime.of(2018, 5, 2, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2018, 5, 2), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2019, 5, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeFebruar29() {

		TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
		KalenderEintrag datumsEintrag = KalenderEintrag.parse(provider, "Februar29", "29.2.*,*");

		assertTrue(datumsEintrag instanceof DatumsEintrag);

		LocalDateTime checkDate = LocalDateTime.of(2018, 1, 15, 12, 0);
		SystemkalenderGueltigkeit gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2016, 3, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020, 2, 29), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());

		checkDate = LocalDateTime.of(2020, 2, 29, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertTrue(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020, 2, 29), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertFalse(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020, 3, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());

		checkDate = LocalDateTime.of(2018, 3, 13, 11, 46);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2016, 3, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020, 2, 29), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());

		checkDate = LocalDateTime.of(2020, 3, 1, 0, 0);
		gueltigKeit = datumsEintrag.getZeitlicheGueltigkeit(checkDate);

		assertFalse(gueltigKeit.isZeitlichGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2020, 3, 1), LocalTime.MIDNIGHT),
				gueltigKeit.getErsterWechsel().getZeitPunkt());
		assertTrue(gueltigKeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals(LocalDateTime.of(LocalDate.of(2024, 2, 29), LocalTime.MIDNIGHT),
				gueltigKeit.getNaechsterWechsel().getZeitPunkt());
	}

	@Test
	public void testeZustandswechselTagDerArbeit() {
		
		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
		KalenderEintrag datumsEintrag = KalenderEintrag.parse(eintragsProvider, "Mai1", "1.5.*,*");

		LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 14, 27, 17);
		LocalDateTime endTime = LocalDateTime.of(2020, 3, 14, 14, 28, 17);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("24.12.2000 14:27:17", false),
				TestWechsel.of("1.5.2001 00:00", true), 
				TestWechsel.of("2.5.2001 00:00", false),
				TestWechsel.of("1.5.2002 00:00", true), 
				TestWechsel.of("2.5.2002 00:00", false),
				TestWechsel.of("1.5.2003 00:00", true), 
				TestWechsel.of("2.5.2003 00:00", false),
				TestWechsel.of("1.5.2004 00:00", true), 
				TestWechsel.of("2.5.2004 00:00", false),
				TestWechsel.of("1.5.2005 00:00", true), 
				TestWechsel.of("2.5.2005 00:00", false),
				TestWechsel.of("1.5.2006 00:00", true), 
				TestWechsel.of("2.5.2006 00:00", false),
				TestWechsel.of("1.5.2007 00:00", true), 
				TestWechsel.of("2.5.2007 00:00", false),
				TestWechsel.of("1.5.2008 00:00", true), 
				TestWechsel.of("2.5.2008 00:00", false),
				TestWechsel.of("1.5.2009 00:00", true), 
				TestWechsel.of("2.5.2009 00:00", false),
				TestWechsel.of("1.5.2010 00:00", true), 
				TestWechsel.of("2.5.2010 00:00", false),
				TestWechsel.of("1.5.2011 00:00", true), 
				TestWechsel.of("2.5.2011 00:00", false),
				TestWechsel.of("1.5.2012 00:00", true), 
				TestWechsel.of("2.5.2012 00:00", false),
				TestWechsel.of("1.5.2013 00:00", true), 
				TestWechsel.of("2.5.2013 00:00", false),
				TestWechsel.of("1.5.2014 00:00", true), 
				TestWechsel.of("2.5.2014 00:00", false),
				TestWechsel.of("1.5.2015 00:00", true), 
				TestWechsel.of("2.5.2015 00:00", false),
				TestWechsel.of("1.5.2016 00:00", true), 
				TestWechsel.of("2.5.2016 00:00", false),
				TestWechsel.of("1.5.2017 00:00", true), 
				TestWechsel.of("2.5.2017 00:00", false),
				TestWechsel.of("1.5.2018 00:00", true), 
				TestWechsel.of("2.5.2018 00:00", false),
				TestWechsel.of("1.5.2019 00:00", true), 
				TestWechsel.of("2.5.2019 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = datumsEintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void testeZustandswechselFebruar29() {
		
		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
		KalenderEintrag datumsEintrag = KalenderEintrag.parse(eintragsProvider, "Februar29", "29.2.*,*");

		LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 14, 27, 17);
		LocalDateTime endTime = LocalDateTime.of(2020, 3, 14, 14, 28, 17);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("24.12.2000 14:27:17", false),
				TestWechsel.of("29.2.2004 00:00", true), 
				TestWechsel.of("1.3.2004 00:00", false),
				TestWechsel.of("29.2.2008 00:00", true), 
				TestWechsel.of("1.3.2008 00:00", false),
				TestWechsel.of("29.2.2012 00:00", true), 
				TestWechsel.of("1.3.2012 00:00", false),
				TestWechsel.of("29.2.2016 00:00", true), 
				TestWechsel.of("1.3.2016 00:00", false),
				TestWechsel.of("29.2.2020 00:00", true), 
				TestWechsel.of("1.3.2020 00:00", false)
		};

		List<ZustandsWechsel> zustandsWechsel = datumsEintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

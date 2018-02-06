package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Gueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline5 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(20);
	
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
		Gueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(startTime);

		assertTrue("Gültigkeit", gueltigkeit.isZeitlichGueltig());
		assertFalse("Statuswechsel", gueltigkeit.getNaechsterWechsel().isWirdGueltig());
		assertEquals("Wechselzeitpunkt", LocalDateTime.of(2012, 12, 28, 0, 0),
				gueltigkeit.getNaechsterWechsel().getZeitPunkt());
	}
}

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline6 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	@Test
	public void beispiel6() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TestSKE", "TestSKE:=Montag+1Tag"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("TestSKE");
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

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

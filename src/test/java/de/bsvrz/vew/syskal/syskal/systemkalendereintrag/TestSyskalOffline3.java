package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline3 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);
	
	@Test
	public void beispiele3() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TestSKE",
					"TestSKE:=<01.09.2010-30.09.2010>({08:00:00,000-11:59:59,999}({15:30:00,000-17:59:59,999}))"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("TestSKE");
		LocalDateTime startTime = LocalDateTime.of(2010, 9, 2, 16, 30, 0);
		LocalDateTime endTime = LocalDateTime.of(2010, 9, 2, 17, 29, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		
		TestWechsel[] erwarteteWechsel = { TestWechsel.of("2.9.2010 16:30", true) };

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

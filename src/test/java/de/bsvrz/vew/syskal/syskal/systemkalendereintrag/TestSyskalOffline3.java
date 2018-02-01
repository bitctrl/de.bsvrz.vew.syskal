package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;

public class TestSyskalOffline3 {

	@Test
	public void beispiele3() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TestSKE",
					"TestSKE:=<01.09.2010-30.09.2010>({08:00:00,000-11:59:59,999}({15:30:00,000-17:59:59,999}))"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("TestSKE");
		LocalDateTime startTime = LocalDateTime.of(2010, 9, 2, 16, 30, 0);
		LocalDateTime endTime = LocalDateTime.of(2010, 9, 2, 17, 29, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}
}

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

public class TestSyskalOffline2 {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(20);

	@Test
	public void beispiele2() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Tag", "Tag"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Tag");
		LocalDateTime startTime = LocalDateTime.of(2009, 10, 1, 14, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2009, 10, 5, 14, 0, 0);

		TestWechsel[] erwarteteWechsel = { TestWechsel.of("1.10.2009 14:00", false) };

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.Gueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline5 {

	@Test
	public void beispiele5() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "WF", "WF:=<24.12.2012-04.01.2013>"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "WFD", "WFD:=UND{Donnerstag,WF}*,*"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("WFD");
		LocalDateTime startTime = LocalDateTime.of(2012, 12, 20, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2012, 12, 31, 0, 0, 0);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		startTime = LocalDateTime.of(2012, 12, 27, 10, 0, 0);
		Gueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(startTime);
		System.out.println("Abfrage2: " + eintrag.getName() + " " + startTime + ":" + gueltigkeit);
	}

}

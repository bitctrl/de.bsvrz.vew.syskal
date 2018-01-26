package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

public class TestSyskalOffline6 {

	@Test
	public void beispiel6() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider
				.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "TestSKE", "TestSKE:=Montag+1Tag"));

		KalenderEintragDefinition eintrag = eintragsProvider.getKalenderEintrag("TestSKE");
		LocalDateTime startTime = LocalDateTime.of(2014, 1, 13, 14, 27, 17);
		LocalDateTime endTime = LocalDateTime.of(2014, 3, 14, 14, 28, 17);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}
}

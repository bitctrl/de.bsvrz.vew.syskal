package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

public class TestSyskalOffline8 {

	@Test
	public void beispiele8() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Mittwoch", "Mittwoch"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Freitag", "Freitag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Samstag", "Samstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Sonntag", "Sonntag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Ferien1", "Ferien1:=<30.07.2015-12.09.2015>"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Ferien2", "Ferien2:=<02.11.2015-06.11.2015>"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Ferien", "Ferien:=ODER{Ferien1,Ferien2}*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "FerienPlus", "FerienPlus:=Ferien+1Tag"));

		KalenderEintragDefinition eintrag = eintragsProvider.getKalenderEintrag("FerienPlus");
		LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2015, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(999000);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}

}

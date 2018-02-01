package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline8 {

	@Test
	public void beispiele8() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Mittwoch", "Mittwoch"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Freitag", "Freitag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Samstag", "Samstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Sonntag", "Sonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Ferien1", "Ferien1:=<30.07.2015-12.09.2015>"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Ferien2", "Ferien2:=<02.11.2015-06.11.2015>"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Ferien", "Ferien:=ODER{Ferien1,Ferien2}*,*"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "FerienPlus", "FerienPlus:=Ferien+1Tag"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("FerienPlus");
		LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2015, 12, 31, 23, 59, 59);
		endTime = endTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(999));
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}

}

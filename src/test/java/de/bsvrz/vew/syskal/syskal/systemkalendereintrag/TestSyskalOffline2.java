package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintrag;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

public class TestSyskalOffline2 {

	private LocalDateTime endTime;

	@Test
	public void beispiele2() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Berufsverkehr",
				"Berufsverkehr:=({07:00:00,000-11:00:00,000}{15:00:00,000-18:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "KeinBerufsverkehr",
				"Berufsverkehr:=({00:00:00,000-08:00:00,000}{11:00:00,000-15:00:00,000}{18:00:00,000-23:59:59,999})"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "SuperDienstag",
				"SuperDienstag:=UND{Dienstag,Berufsverkehr}*,*"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "SuperMittwoch",
				"SuperMittwoch:=SuperDienstag+1Tag"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Osterdienstag", "Osterdienstag:=Ostermontag+1Tag"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2Tage"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Pfingstsonntag",
				"Pfingstsonntag:=Ostersonntag+49Tage"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Pfingstmontag",
				"Pfingstmontag:=Pfingstsonntag+1Tag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Sonntag", "Sonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "KeinSonntag",
				"KeinSonntag:=UND{Tag,NICHT Sonntag}*,*"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Tag");
		LocalDateTime startTime = LocalDateTime.of(2009, 10, 1, 14, 0, 0);
		endTime = LocalDateTime.of(2009, 10, 5, 14, 0, 0);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

	}
}

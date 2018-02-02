package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline7 {

	@Test
	public void beispiele7() {

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
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Neujahr", "Neujahr:=01.01.*,*"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2Tage"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Einheit", "Einheit:=03.10.*,*"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Arbeit", "Arbeit:=01.05.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Allerheil", "Allerheil:=01.11.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Weihnacht1", "Weihnacht1:=25.12.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Weihnacht2", "Weihnacht2:=26.12.*,*"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "Himmel", "Himmel:=Ostersonntag+39Tage"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "PfingstMo", "PfingstMo:=Ostersonntag+50Tage"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Fronleich", "Fronleich:=Ostersonntag+60Tage"));

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Feiertag",
				"Feiertag:=ODER{Neujahr,Karfreitag,Ostersonntag,Ostermontag,Arbeit,Himmel,PfingstMo,Fronleich,Einheit,Allerheil,Weihnacht1,Weihnacht2}*,*"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "MoBisSa",
				"MoBisSa:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag}*,*"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "MoBisFr",
				"MoBisFr:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag}*,*"));

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Werktag",
				"Werktag:=UND{MoBisFr,NICHT Feiertag}*,*"));

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Werktag");
		LocalDateTime startTime = LocalDateTime.of(2014, 8, 1, 14, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2014, 8, 31, 0, 0, 0);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}
}

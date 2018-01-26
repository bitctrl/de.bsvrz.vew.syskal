package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

public class TestSyskalOffline7 {

	@Test
	public void beispiele7() {

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
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Neujahr", "Neujahr:=01.01.*,*"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2Tage"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Einheit", "Einheit:=03.10.*,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Arbeit", "Arbeit:=01.05.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Allerheil", "Allerheil:=01.11.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Weihnacht1", "Weihnacht1:=25.12.*,*"));
		eintragsProvider
				.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Weihnacht2", "Weihnacht2:=26.12.*,*"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag"));
		eintragsProvider
				.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Himmel", "Himmel:=Ostersonntag+39Tage"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "PfingstMo", "PfingstMo:=Ostersonntag+50Tage"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "Fronleich", "Fronleich:=Ostersonntag+60Tage"));

		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Feiertag",
				"Feiertag:=ODER{Neujahr,Karfreitag,Ostersonntag,Ostermontag,Arbeit,Himmel,PfingstMo,Fronleich,Einheit,Allerheil,Weihnacht1,Weihnacht2}*,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "MoBisSa",
				"MoBisSa:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag}*,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "MoBisFr",
				"MoBisFr:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag}*,*"));

		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Werktag",
				"Werktag:=UND{MoBisFr,NICHT Feiertag}*,*"));

		KalenderEintragDefinition eintrag = eintragsProvider.getKalenderEintrag("Werktag");
		LocalDateTime startTime = LocalDateTime.of(2014, 8, 1, 14, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2014, 8, 31, 0, 0, 0);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}
}

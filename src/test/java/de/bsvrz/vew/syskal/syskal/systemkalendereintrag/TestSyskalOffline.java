package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragProvider;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

public class TestSyskalOffline {

	@Test
	public void beispiele1() {

		TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
		
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Berufsverkehr",
				"Berufsverkehr:=({07:00:00,000-11:00:00,000}{15:00:00,000-18:00:00,000})"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Montag_Berufsverkehr",
				"Montag_Berufsverkehr:=UND{Montag,Berufsverkehr}*,*"));
		eintragsProvider.addEintrag(
				KalenderEintragDefinition.parse(eintragsProvider, "GeburtstagHCK", "GeburtstagHCK:=27.11.1963,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "SuperDienstag",
				"SuperDienstag:=UND{Dienstag,Berufsverkehr}*,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "DienstagAlsVerknüpfung",
				"DienstagAlsVerknüpfung:=UND{Dienstag}*,*"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "GeburtstagHCKFeierKopie",
				"GeburtstagHCKFeierKopie:=GeburtstagHCK-3Tage"));
		eintragsProvider.addEintrag(KalenderEintragDefinition.parse(eintragsProvider, "SuperMittwoch",
				"SuperMittwoch:=SuperDienstag-1Tag"));

		KalenderEintragDefinition eintrag = eintragsProvider.getKalenderEintrag("Montag_Berufsverkehr");
		LocalDateTime startTime = LocalDateTime.of(2009, 8, 1, 10, 40, 35);
		LocalDateTime endTime = LocalDateTime.of(2009, 9, 18, 10, 40, 35);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("GeburtstagHCK");
		startTime = LocalDateTime.of(1970, 9, 25, 14, 59, 9);
		endTime = LocalDateTime.of(1975, 9, 25, 14, 59, 9);
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage2: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		startTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
		endTime = LocalDateTime.of(1970, 12, 31, 23, 59, 59);
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage3: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("DienstagAlsVerknüpfung");
		startTime = LocalDateTime.of(2009, 1, 1, 15, 15, 37);
		endTime = LocalDateTime.of(2009, 12, 21, 15, 15, 37);
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage4: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = eintragsProvider.getKalenderEintrag("GeburtstagHCKFeierKopie");
		startTime = LocalDateTime.of(2009, 1, 1, 15, 15, 37);
		endTime = LocalDateTime.of(2009, 12, 21, 15, 15, 37);
		zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		System.out
				.println("Abfrage5: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}
}

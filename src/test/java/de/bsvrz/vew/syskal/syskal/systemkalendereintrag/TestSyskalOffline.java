package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class TestSyskalOffline {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(20);
	private static TestKalenderEintragProvider eintragsProvider;

	@BeforeClass
	public static void init() {

		eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Berufsverkehr",
				"Berufsverkehr:=({07:00:00,000-11:00:00,000}{15:00:00,000-18:00:00,000})"));

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Montag_Berufsverkehr",
				"Montag_Berufsverkehr:=UND{Montag,Berufsverkehr}*,*"));
		eintragsProvider
				.addEintrag(KalenderEintrag.parse(eintragsProvider, "GeburtstagHCK", "GeburtstagHCK:=27.11.1963,*"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "GeburtstagHCKFeierKopie",
				"GeburtstagHCKFeierKopie:=GeburtstagHCK-3Tage"));

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "DienstagAlsVerknüpfung",
				"DienstagAlsVerknüpfung:=UND{Dienstag}*,*"));
	}

	@Test
	public void montagBerufsVerkehr() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Montag_Berufsverkehr");
		LocalDateTime startTime = LocalDateTime.of(2009, 8, 1, 10, 40, 35);
		LocalDateTime endTime = LocalDateTime.of(2009, 9, 18, 10, 40, 35);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.8.2009 10:40:35", false),
				TestWechsel.of("3.8.2009 07:00", true), 
				TestWechsel.of("3.8.2009 11:00", false),
				TestWechsel.of("3.8.2009 15:00", true), 
				TestWechsel.of("3.8.2009 18:00", false),
				TestWechsel.of("10.8.2009 07:00", true), 
				TestWechsel.of("10.8.2009 11:00", false),
				TestWechsel.of("10.8.2009 15:00", true), 
				TestWechsel.of("10.8.2009 18:00", false),
				TestWechsel.of("17.8.2009 07:00", true), 
				TestWechsel.of("17.8.2009 11:00", false),
				TestWechsel.of("17.8.2009 15:00", true), 
				TestWechsel.of("17.8.2009 18:00", false),
				TestWechsel.of("24.8.2009 07:00", true), 
				TestWechsel.of("24.8.2009 11:00", false),
				TestWechsel.of("24.8.2009 15:00", true), 
				TestWechsel.of("24.8.2009 18:00", false),
				TestWechsel.of("31.8.2009 07:00", true), 
				TestWechsel.of("31.8.2009 11:00", false),
				TestWechsel.of("31.8.2009 15:00", true), 
				TestWechsel.of("31.8.2009 18:00", false),
				TestWechsel.of("7.9.2009 07:00", true), 
				TestWechsel.of("7.9.2009 11:00", false),
				TestWechsel.of("7.9.2009 15:00", true), 
				TestWechsel.of("7.9.2009 18:00", false),
				TestWechsel.of("14.9.2009 07:00", true), 
				TestWechsel.of("14.9.2009 11:00", false),
				TestWechsel.of("14.9.2009 15:00", true), 
				TestWechsel.of("14.9.2009 18:00", false)
			};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void geburtstagHCK() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("GeburtstagHCK");
		LocalDateTime startTime = LocalDateTime.of(1970, 9, 25, 14, 59, 9);
		LocalDateTime endTime = LocalDateTime.of(1975, 9, 25, 14, 59, 9);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("25.9.1970 14:59:09", false),
				TestWechsel.of("27.11.1970 00:00", true), 
				TestWechsel.of("28.11.1970 00:00", false),
				TestWechsel.of("27.11.1971 00:00", true), 
				TestWechsel.of("28.11.1971 00:00", false),
				TestWechsel.of("27.11.1972 00:00", true), 
				TestWechsel.of("28.11.1972 00:00", false),
				TestWechsel.of("27.11.1973 00:00", true), 
				TestWechsel.of("28.11.1973 00:00", false),
				TestWechsel.of("27.11.1974 00:00", true), 
				TestWechsel.of("28.11.1974 00:00", false)
			};

		
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void geburtstagHCK2() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("GeburtstagHCK");
		LocalDateTime startTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(1970, 12, 31, 23, 59, 59);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.1.1970 00:00", false),
				TestWechsel.of("27.11.1970 00:00", true), 
				TestWechsel.of("28.11.1970 00:00", false)
			};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	
	@Test
	public void dienstagAlsVerknuepfung() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("DienstagAlsVerknüpfung");
		LocalDateTime startTime = LocalDateTime.of(2009, 1, 1, 15, 15, 37);
		LocalDateTime endTime = LocalDateTime.of(2009, 12, 21, 15, 15, 37);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.1.2009 15:15:37", false),
				TestWechsel.of("6.1.2009 00:00", true), 
				TestWechsel.of("7.1.2009 00:00", false),
				TestWechsel.of("13.1.2009 00:00", true), 
				TestWechsel.of("14.1.2009 00:00", false),
				TestWechsel.of("20.1.2009 00:00", true), 
				TestWechsel.of("21.1.2009 00:00", false),
				TestWechsel.of("27.1.2009 00:00", true), 
				TestWechsel.of("28.1.2009 00:00", false),
				TestWechsel.of("3.2.2009 00:00", true), 
				TestWechsel.of("4.2.2009 00:00", false),
				TestWechsel.of("10.2.2009 00:00", true), 
				TestWechsel.of("11.2.2009 00:00", false),
				TestWechsel.of("17.2.2009 00:00", true), 
				TestWechsel.of("18.2.2009 00:00", false),
				TestWechsel.of("24.2.2009 00:00", true), 
				TestWechsel.of("25.2.2009 00:00", false),
				TestWechsel.of("3.3.2009 00:00", true), 
				TestWechsel.of("4.3.2009 00:00", false),
				TestWechsel.of("10.3.2009 00:00", true), 
				TestWechsel.of("11.3.2009 00:00", false),
				TestWechsel.of("17.3.2009 00:00", true), 
				TestWechsel.of("18.3.2009 00:00", false),
				TestWechsel.of("24.3.2009 00:00", true), 
				TestWechsel.of("25.3.2009 00:00", false),
				TestWechsel.of("31.3.2009 00:00", true), 
				TestWechsel.of("1.4.2009 00:00", false),
				TestWechsel.of("7.4.2009 00:00", true), 
				TestWechsel.of("8.4.2009 00:00", false),
				TestWechsel.of("14.4.2009 00:00", true), 
				TestWechsel.of("15.4.2009 00:00", false),
				TestWechsel.of("21.4.2009 00:00", true), 
				TestWechsel.of("22.4.2009 00:00", false),
				TestWechsel.of("28.4.2009 00:00", true), 
				TestWechsel.of("29.4.2009 00:00", false),
				TestWechsel.of("5.5.2009 00:00", true), 
				TestWechsel.of("6.5.2009 00:00", false),
				TestWechsel.of("12.5.2009 00:00", true), 
				TestWechsel.of("13.5.2009 00:00", false),
				TestWechsel.of("19.5.2009 00:00", true), 
				TestWechsel.of("20.5.2009 00:00", false),
				TestWechsel.of("26.5.2009 00:00", true), 
				TestWechsel.of("27.5.2009 00:00", false),
				TestWechsel.of("2.6.2009 00:00", true), 
				TestWechsel.of("3.6.2009 00:00", false),
				TestWechsel.of("9.6.2009 00:00", true), 
				TestWechsel.of("10.6.2009 00:00", false),
				TestWechsel.of("16.6.2009 00:00", true), 
				TestWechsel.of("17.6.2009 00:00", false),
				TestWechsel.of("23.6.2009 00:00", true), 
				TestWechsel.of("24.6.2009 00:00", false),
				TestWechsel.of("30.6.2009 00:00", true), 
				TestWechsel.of("1.7.2009 00:00", false),
				TestWechsel.of("7.7.2009 00:00", true), 
				TestWechsel.of("8.7.2009 00:00", false),
				TestWechsel.of("14.7.2009 00:00", true), 
				TestWechsel.of("15.7.2009 00:00", false),
				TestWechsel.of("21.7.2009 00:00", true), 
				TestWechsel.of("22.7.2009 00:00", false),
				TestWechsel.of("28.7.2009 00:00", true), 
				TestWechsel.of("29.7.2009 00:00", false),
				TestWechsel.of("4.8.2009 00:00", true), 
				TestWechsel.of("5.8.2009 00:00", false),
				TestWechsel.of("11.8.2009 00:00", true), 
				TestWechsel.of("12.8.2009 00:00", false),
				TestWechsel.of("18.8.2009 00:00", true), 
				TestWechsel.of("19.8.2009 00:00", false),
				TestWechsel.of("25.8.2009 00:00", true), 
				TestWechsel.of("26.8.2009 00:00", false),
				TestWechsel.of("1.9.2009 00:00", true), 
				TestWechsel.of("2.9.2009 00:00", false),
				TestWechsel.of("8.9.2009 00:00", true), 
				TestWechsel.of("9.9.2009 00:00", false),
				TestWechsel.of("15.9.2009 00:00", true), 
				TestWechsel.of("16.9.2009 00:00", false),
				TestWechsel.of("22.9.2009 00:00", true), 
				TestWechsel.of("23.9.2009 00:00", false),
				TestWechsel.of("29.9.2009 00:00", true), 
				TestWechsel.of("30.9.2009 00:00", false),
				TestWechsel.of("6.10.2009 00:00", true), 
				TestWechsel.of("7.10.2009 00:00", false),
				TestWechsel.of("13.10.2009 00:00", true), 
				TestWechsel.of("14.10.2009 00:00", false),
				TestWechsel.of("20.10.2009 00:00", true), 
				TestWechsel.of("21.10.2009 00:00", false),
				TestWechsel.of("27.10.2009 00:00", true), 
				TestWechsel.of("28.10.2009 00:00", false),
				TestWechsel.of("3.11.2009 00:00", true), 
				TestWechsel.of("4.11.2009 00:00", false),
				TestWechsel.of("10.11.2009 00:00", true), 
				TestWechsel.of("11.11.2009 00:00", false),
				TestWechsel.of("17.11.2009 00:00", true), 
				TestWechsel.of("18.11.2009 00:00", false),
				TestWechsel.of("24.11.2009 00:00", true), 
				TestWechsel.of("25.11.2009 00:00", false),
				TestWechsel.of("1.12.2009 00:00", true), 
				TestWechsel.of("2.12.2009 00:00", false),
				TestWechsel.of("8.12.2009 00:00", true), 
				TestWechsel.of("9.12.2009 00:00", false),
				TestWechsel.of("15.12.2009 00:00", true), 
				TestWechsel.of("16.12.2009 00:00", false)
			};

		
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}

	@Test
	public void geburtstagsFeierHCK() {

		KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("GeburtstagHCKFeierKopie");
		LocalDateTime startTime = LocalDateTime.of(2009, 1, 1, 15, 15, 37);
		LocalDateTime endTime = LocalDateTime.of(2009, 12, 21, 15, 15, 37);

		TestWechsel[] erwarteteWechsel = { 
				TestWechsel.of("1.1.2009 15:15:37", false),
				TestWechsel.of("24.11.2009 00:00", true), 
				TestWechsel.of("25.11.2009 00:00", false)

			};

		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechselImBereich(startTime, endTime);
		TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
	}
}

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;

public class TestSyskalOffline7 {
	/**
	 * Das Format der Ergebnisausgabe
	 */
	private static DateFormat sdf;

	public static void main(String[] args) {
		SystemkalenderArbeiter systemKalenderArbeiter = new SystemkalenderArbeiter(null, null);

		try {

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.montag", "Montag", "Montag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.dienstag", "Dienstag", "Dienstag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.mittwoch", "Mittwoch", "Mittwoch");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.donnerstag", "Donnerstag", "Donnerstag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.freitag", "Freitag", "Freitag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.samstag", "Samstag", "Samstag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.sonntag", "Sonntag", "Sonntag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ostersonntag", "Ostersonntag", "Ostersonntag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.tag", "Tag", "Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.neujahr", "Neujahr", "Neujahr:=01.01.*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.karfreitag", "Karfreitag",
					"Karfreitag:=Ostersonntag-2Tage");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.tdde", "Einheit", "Einheit:=03.10.*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.tda", "Arbeit", "Arbeit:=01.05.*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.allerheil", "Allerheil", "Allerheil:=01.11.*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.weihnacht1", "Weihnacht1", "Weihnacht1:=25.12.*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.weihnacht2", "Weihnacht2", "Weihnacht2:=26.12.*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ostermontag", "Ostermontag",
					"Ostermontag:=Ostersonntag+1Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.himmel", "Himmel", "Himmel:=Ostersonntag+39Tage");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.pfingstmo", "PfingstMo",
					"PfingstMo:=Ostersonntag+50Tage");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.fronleich", "Fronleich",
					"Fronleich:=Ostersonntag+60Tage");

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.feiertag", "Feiertag",
					"Feiertag:=ODER{Neujahr,Karfreitag,Ostersonntag,Ostermontag,Arbeit,Himmel,PfingstMo,Fronleich,Einheit,Allerheil,Weihnacht1,Weihnacht2}*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.mobissa", "MoBisSa",
					"MoBisSa:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag}*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.mobisfr", "MoBisFr",
					"MoBisFr:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag}*,*");

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.werktag", "Werktag",
					"Werktag:=UND{MoBisFr,NICHT Feiertag}*,*");
			sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

			AlterSystemkalenderEintrag ske1 = systemKalenderArbeiter.getSkeList().get("ske.werktag");

			Date d1 = sdf.parse("01.08.2014 14:00:00,000");
			Date d2 = sdf.parse("31.08.2014 00:00:00,000");

			System.out.println("Abfrage: " + ske1.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Erstellt eine Abfrage der manuellen Zeitbereiche durch Benutzung der <br>
	 * vom Systemkalender bereitgestellten Methode <br>
	 * {@link SystemkalenderEintrag#berecheneIntervallVonBis(Long, Long)} <br>
	 * Diese Methode liefert das Ergebnis in der Form: <br>
	 * {@link SortedMap} mit dem Wertepaar <{@link Long}, {@link Long}>
	 * 
	 * @param ske
	 *            der Systemkalendereintrag
	 * @param von
	 *            Anfangsdatum
	 * @param bis
	 *            Enddatum
	 */
	private static void erstelleAbfrageUndAusgabeErgebnisTyp2(AlterSystemkalenderEintrag ske, Date von, Date bis) {
		SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(von.getTime(), bis.getTime());

		if (sm != null) {
			Date d1 = new Date();
			Date d2 = new Date();
			for (Map.Entry<Long, Long> me : sm.entrySet()) {
				d1.setTime(me.getKey());
				d2.setTime(me.getValue());
				System.out.println("Ergebnistyp 2: " + sdf.format(d1) + " " + sdf.format(d2));

			}
		} else
			System.out.println("Abfrage liefert kein Ergebnis!");
	}
}

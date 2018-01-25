package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;

public class TestSyskalOffline2 {
	/**
	 * Das Format der Ergebnisausgabe
	 */
	private static DateFormat sdf;

	public static void main(String[] args) {

		SystemkalenderArbeiter systemKalenderArbeiter = new SystemkalenderArbeiter(null, null);

		try {
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ostersonntag", "Ostersonntag", "Ostersonntag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.berufsverkehr", "Berufsverkehr",
					"Berufsverkehr:=({07:00:00,000-11:00:00,000}{15:00:00,000-18:00:00,000})");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.keinberufsverkehr", "KeinBerufsverkehr",
					"Berufsverkehr:=({00:00:00,000-08:00:00,000}{11:00:00,000-15:00:00,000}{18:00:00,000-23:59:59,999})");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.dienstag", "Dienstag", "Dienstag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.superdienstag", "SuperDienstag",
					"SuperDienstag:=UND{Dienstag,Berufsverkehr}*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.supermittwoch", "SuperMittwoch",
					"SuperMittwoch:=SuperDienstag+1Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ostermontag", "Ostermontag",
					"Ostermontag:=Ostersonntag+1Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.osterdienstag", "Osterdienstag",
					"Osterdienstag:=Ostermontag+1Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.karfreitag", "Karfreitag",
					"Karfreitag:=Ostersonntag-2Tage");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.pfingsonntag", "Pfingstsonntag",
					"Pfingstsonntag:=Ostersonntag+49Tage");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.pfingstmontag", "Pfingstmontag",
					"Pfingstmontag:=Pfingstsonntag+1Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.tag", "Tag", "Tag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.sonntag", "Sonntag", "Sonntag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.keinsonntag", "KeinSonntag",
					"KeinSonntag:=UND{Tag,NICHT Sonntag}*,*");

			sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

			AlterSystemkalenderEintrag ske1 = systemKalenderArbeiter.getSkeList().get("ske.tag");

			Date d1 = sdf.parse("01.10.2009 14:00:00,000");
			Date d2 = sdf.parse("05.10.2009 14:00:00,000");

			System.out.println("Abfrage1: " + ske1.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

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

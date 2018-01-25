package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;

public class TestSyskalOffline8 {
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

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ferien1", "Ferien1",
					"Ferien1:=<30.07.2015-12.09.2015>");
			// systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ende1", "Ende1",
			// "Ende1:=12.09.2015,2015");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ferien2", "Ferien2",
					"Ferien2:=<02.11.2015-06.11.2015>");
			// systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ende2", "Ende2",
			// "Ende2:=06.11.2015,2015");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ferien", "Ferien",
					"Ferien:=ODER{Ferien1,Ferien2}*,*");

			// systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ferien11", "Ferien11",
			// "Ferien11:=Ferien1+1Tag");
			// systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ferien21", "Ferien21",
			// "Ferien21:=Ferien2+1Tag");

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ferienplus", "FerienPlus",
					"FerienPlus:=Ferien+1Tag");
			// systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ende11", "Ende11",
			// "Ende11:=Ende1+1Tag");
			// systemKalenderArbeiter.parseSystemkalenderEintrag("ske.ende21", "Ende21",
			// "Ende21:=Ende2+1Tag");

			sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

			AlterSystemkalenderEintrag ske1 = systemKalenderArbeiter.getSkeList().get("ske.ferienplus");
			// SystemkalenderEintrag ske1 =
			// systemKalenderArbeiter.getSkeList().get("ske.ferien");

			Date d1 = sdf.parse("01.01.2015 00:00:00,000");
			Date d2 = sdf.parse("31.12.2015 23:59:59,999");

			System.out.println("Abfrage: " + ske1.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
			// erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
			// erstelleAbfrageUndAusgabeErgebnisTyp3(ske1, 2015);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Erstellt eine Abfrage der manuellen Zeitbereiche durch Benutzung der <br>
	 * vom Systemkalender bereitgestellten Methode <br>
	 * {@link SystemkalenderEintrag#berecheneZustandsWechselVonBis(Long, Long)} <br>
	 * Diese Methode liefert das Ergebnis in der Form: <br>
	 * {@link SortedMap} mit dem Wertepaar <{@link Long}, {@link Boolean}>
	 * 
	 * @param ske
	 *            der Systemkalendereintrag
	 * @param von
	 *            Anfangsdatum
	 * @param bis
	 *            Enddatum
	 */
	private static void erstelleAbfrageUndAusgabeErgebnisTyp1(AlterSystemkalenderEintrag ske, Date von, Date bis) {
		SortedMap<Long, Boolean> sm = ske.berecheneZustandsWechselVonBis(von.getTime(), bis.getTime());

		if (sm != null) {
			Date d = new Date();
			for (Map.Entry<Long, Boolean> me : sm.entrySet()) {
				d.setTime(me.getKey());
				System.out.println("Ergebnistyp 1: " + sdf.format(d) + " " + me.getValue());

			}
		} else
			System.out.println("Abfrage liefert kein Ergebnis!");
	}
}

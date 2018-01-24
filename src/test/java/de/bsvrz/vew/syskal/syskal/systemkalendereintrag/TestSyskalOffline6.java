package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

public class TestSyskalOffline6 {
	/**
	 * Das Format der Ergebnisausgabe
	 */
	private static DateFormat sdf;

	public static void main(String[] args) {

		SystemkalenderArbeiter systemKalenderArbeiter = new SystemkalenderArbeiter(null, null);

		try {

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.montag", "Montag", "Montag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.test", "TestSKE", "TestSKE:=Montag+1Tag");

			sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

			SystemkalenderEintrag ske1 = systemKalenderArbeiter.getSkeList().get("ske.test");

			Date d1 = sdf.parse("13.01.2014 14:27:17,000");
			Date d2 = sdf.parse("14.03.2014 14:28:17,000");

			System.out.println("Abfrage1: " + ske1.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			// erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
			// erstelleAbfrageUndAusgabeErgebnisTyp3(ske1, 2010);

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
	private static void erstelleAbfrageUndAusgabeErgebnisTyp2(SystemkalenderEintrag ske, Date von, Date bis) {
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

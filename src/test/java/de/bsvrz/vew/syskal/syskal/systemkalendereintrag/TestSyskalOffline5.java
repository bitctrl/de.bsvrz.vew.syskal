package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;

public class TestSyskalOffline5 {
	/**
	 * Das Format der Ergebnisausgabe
	 */
	private static DateFormat sdf;

	public static void main(String[] args) {

		SystemkalenderArbeiter systemKalenderArbeiter = new SystemkalenderArbeiter(null, null);

		try {

			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.donnerstag", "Donnerstag", "Donnerstag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.wf", "WF", "WF:=<24.12.2012-04.01.2013>");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.wfd", "WFD", "WFD:=UND{Donnerstag,WF}*,*");

			sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

			AlterSystemkalenderEintrag ske1 = systemKalenderArbeiter.getSkeList().get("ske.wfd");

			Date d1 = sdf.parse("20.12.2012 00:00:00,000");
			Date d2 = sdf.parse("31.12.2012 00:00:00,000");
			Date jetzt = sdf.parse("27.12.2012 10:00:00,000");

			System.out.println();
			System.out.println("Abfrage1: " + ske1.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp4(ske1, jetzt);
			erstelleAbfrageUndAusgabeErgebnisTyp5(ske1, d1, d2);

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

	/**
	 * Erstellt eine Abfrage der Zeitbereiche für das Jahr des Anfangszeitpunktes
	 * <br>
	 * durch Benutzung der vom Systemkalender bereitgestellten Methode <br>
	 * {@link SystemkalenderEintrag#berechneZustandsWechsel(int)} <br>
	 * Diese Methode liefert das Ergebnis in der Form: <br>
	 * {@link SortedMap} mit dem Wertepaar <{@link Long}, {@link Boolean}>
	 * 
	 * @param ske
	 *            der Systemkalendereintrag
	 * @param jetzt
	 *            Anfangsdatum
	 */
	private static void erstelleAbfrageUndAusgabeErgebnisTyp4(AlterSystemkalenderEintrag ske, Date jetzt) {

		boolean gueltig = ske.isGueltig(jetzt.getTime());
		System.out.println("Ergebnistyp 4: " + sdf.format(jetzt) + " " + gueltig);
	}

	/**
	 * Erstellt eine Abfrage der Zeitbereiche für das Jahr des Anfangszeitpunktes
	 * <br>
	 * durch Benutzung der vom Systemkalender bereitgestellten Methode <br>
	 * {@link SystemkalenderEintrag#berechneZustandsWechsel(int)} <br>
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
	private static void erstelleAbfrageUndAusgabeErgebnisTyp5(AlterSystemkalenderEintrag ske, Date von, Date bis) {

		boolean gueltig = ske.isGueltigVonBis(von.getTime(), bis.getTime());
		System.out.println("Ergebnistyp 5: " + sdf.format(von) + " " + sdf.format(bis) + " " + gueltig);
	}
}

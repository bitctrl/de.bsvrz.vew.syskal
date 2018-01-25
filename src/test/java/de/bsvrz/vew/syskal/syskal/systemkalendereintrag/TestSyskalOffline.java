package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;

public class TestSyskalOffline {
	/**
	 * Das Format der Ergebnisausgabe
	 */
	private static DateFormat sdf;

	public static void main(String[] args) {

		SystemkalenderArbeiter systemKalenderArbeiter = new SystemkalenderArbeiter(null, null);

		try {
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.montag", "Montag", "Montag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.berufsverkehr", "Berufsverkehr",
					"Berufsverkehr:=({07:00:00,000-11:00:00,000}{15:00:00,000-18:00:00,000})");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.montag_berufsverkehr", "Montag_Berufsverkehr",
					"Montag_Berufsverkehr:=UND{Montag,Berufsverkehr}*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.geburtstag_hck", "GeburtstagHCK",
					"GeburtstagHCK:=27.11.1963,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.dienstag", "Dienstag", "Dienstag");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.superdienstag", "SuperDienstag",
					"SuperDienstag:=UND{Dienstag,Berufsverkehr}*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.dienstagalsverknüpfung", "DienstagAlsVerknüpfung",
					"DienstagAlsVerknüpfung:=UND{Dienstag}*,*");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.geburtstag_hck_kopie", "GeburtstagHCKFeierKopie",
					"GeburtstagHCKFeierKopie:=GeburtstagHCK-3Tage");
			systemKalenderArbeiter.parseSystemkalenderEintrag("ske.supermittwoch", "SuperMittwoch",
					"SuperMittwoch:=SuperDienstag-1Tag");

			sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

			AlterSystemkalenderEintrag ske1 = systemKalenderArbeiter.getSkeList().get("ske.montag_berufsverkehr");

			Date d1 = sdf.parse("01.08.2009 10:40:35,000");
			Date d2 = sdf.parse("18.09.2009 10:40:35,000");

			System.out.println("Abfrage1: " + ske1.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp3(ske1, 2009);

			AlterSystemkalenderEintrag ske2 = systemKalenderArbeiter.getSkeList().get("ske.geburtstag_hck");

			d1 = sdf.parse("25.09.1970 14:59:09,000");
			d2 = sdf.parse("25.09.1975 14:59:09,000");

			System.out.println("Abfrage2: " + ske2.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske2, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske2, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp3(ske2, 1970);

			d1 = sdf.parse("01.01.1970 00:00:00,000");
			d2 = sdf.parse("31.12.1970 23:59:59,000");

			System.out.println("Abfrage3: " + ske2.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske2, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske2, d1, d2);

			AlterSystemkalenderEintrag ske3 = systemKalenderArbeiter.getSkeList().get("ske.dienstagalsverknüpfung");

			d1 = sdf.parse("01.01.2009 15:15:37,000");
			d2 = sdf.parse("21.12.2009 15:15:37,000");

			System.out.println("Abfrage4: " + ske3.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske3, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske3, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp3(ske3, 2009);

			AlterSystemkalenderEintrag ske4 = systemKalenderArbeiter.getSkeList().get("ske.geburtstag_hck_kopie");

			d1 = sdf.parse("01.01.2009 15:15:37,000");
			d2 = sdf.parse("21.12.2009 15:15:37,000");

			System.out.println("Abfrage5: " + ske4.getPid() + " " + sdf.format(d1) + " - " + sdf.format(d2));

			erstelleAbfrageUndAusgabeErgebnisTyp1(ske4, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp2(ske4, d1, d2);
			erstelleAbfrageUndAusgabeErgebnisTyp3(ske4, 2009);
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
	 * @param jahr
	 *            das Jahr
	 */
	private static void erstelleAbfrageUndAusgabeErgebnisTyp3(AlterSystemkalenderEintrag ske, int jahr) {
		SortedMap<Long, Boolean> sm = ske.berechneZustandsWechsel(jahr);

		if (sm != null) {
			Date d = new Date();
			for (Map.Entry<Long, Boolean> me : sm.entrySet()) {
				d.setTime(me.getKey());
				System.out.println("Ergebnistyp 3: " + sdf.format(d) + " " + me.getValue());

			}
		} else
			System.out.println("Abfrage liefert kein Ergebnis!");
	}
}

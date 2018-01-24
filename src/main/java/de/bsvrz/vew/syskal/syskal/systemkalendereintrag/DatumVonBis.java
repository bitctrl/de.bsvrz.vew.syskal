/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contact Information:
 * Dambach-Werke GmbH
 * Elektronische Leitsysteme
 * Fritz-Minhardt-Str. 1
 * 76456 Kuppenheim
 * Phone: +49-7222-402-0
 * Fax: +49-7222-402-200
 * mailto: info@els.dambach.de
 */

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Die Klasse erzeugt SystemKalenderEintraege vom Typ: "&lt;17.11.2007,
 * 19.11.2007&gt;". zusätzlich können noch optional eine oder mehrere
 * Zeitgruppen definiert werden, z.B.: "&lt;17.11.2007, 19.11.2007&gt;
 * ({07:15:00,000 - 09:00:00,000}{15:30:00,000 - 17:45:00,000})". Die Berechnung
 * der Zeitpunkte mit Wildcards bezieht sich noch auf das aktuelle Kalenderjahr.
 * Die Vorgehensweise könnte dahingehend sein, dass beim Jahrewechsel die
 * Zustandswechsel neu berechnet werden. Muss also noch geklärt werden *
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class DatumVonBis extends DatumJahr {

	/**
	 * Die Liste der Zeitgruppen
	 */
	protected List<String[]> zeitVonBis;

	/**
	 * Position von "<" in der Definition des Ske
	 */
	private int start;

	/**
	 * Position von ">" in der Definition des Ske
	 */
	private int ende;

	/**
	 * Konstruktor der Klasse
	 * 
	 * @param pid
	 *            Die Pid
	 * @param definition
	 *            Die Definition des Ske
	 */
	public DatumVonBis(String pid, String definition) {

		super(pid, definition);
		zeitVonBis = new ArrayList<>();
		// _debug = Debug.getLogger();

	}

	@Override
	public boolean pruefeEintrag() {

		String sub;
		String[] split, sub2;

		if (!definition.contains("<")) {

			String[] str = { "01.01.*", "31.12.*" };

			jahrVonBis.add(str);

		} else {

			start = definition.indexOf("<");
			ende = definition.indexOf(">");

			sub = definition.substring(start + 1, ende);

			split = sub.split("-");

			SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
			SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy");

			try {
				String[] dat = new String[2];
				String[] uhr = new String[2];

				df1.parse(split[0]);
				df1.parse(split[1]);

				String[] split2 = split[0].split(" ");
				String[] split3 = split[1].split(" ");

				dat[0] = split2[0];
				dat[1] = split3[0];

				jahrVonBis.add(dat);

				// _debug.config(dat[0] + " " + dat[1]);

				uhr[0] = split2[1];
				uhr[1] = split3[1];

				zeitVonBis.add(uhr);

				// _debug.config(dat[0] + " " + dat[1] + " : "+ uhr[0] + " " + uhr[1]);
			} catch (ParseException e1) {
				if (split[0].contains("*") || split[1].contains("*")) {

					jahrVonBis.add(split);

					if (!definition.contains("(")) {
						String[] str = { "00:00:00,000", " 23:59:59,999" };
						zeitVonBis.add(str);
					}

				} else {

					try {

						df2.parse(split[0]);
						df2.parse(split[1]);

						jahrVonBis.add(split);

						if (!definition.contains("(")) {
							String[] str = { "00:00:00,000", " 23:59:59,999" };
							zeitVonBis.add(str);
						}
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						return false;
					}

				}

			}

		}

		if (definition.contains("(")) {

			start = definition.indexOf("(");
			ende = definition.indexOf(")");

			sub = definition.substring(start + 1, ende);

			int[] a = new int[10];
			int[] e = new int[10];
			int cnta = 0;
			int cnte = 0;

			for (int i = 0; i < sub.length(); i++) {
				char current = sub.charAt(i);
				if (current == '{')
					a[cnta++] = i + 1;
				if (current == '}')
					e[cnte++] = i;
			}

			if (cnta == cnte) {

				sub2 = new String[cnta];

				for (int i = 0; i < cnta; i++) {
					sub2[i] = sub.substring(a[i], e[i]);

					split = sub2[i].split("-");

					zeitVonBis.add(split);

				}
			} else
				return false;

		}

		return true;

	}

	@Override
	public SortedMap<Long, Boolean> berechneZustandsWechsel(int jahr) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = df.parse("01.01." + jahr + " 00:00:00,000");
			d2 = df.parse("31.12." + jahr + " 23:59:59,999");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		berecheneZustandsWechselVonBis(d1.getTime(), d2.getTime());

		return berecheneZustandsWechselVonBis(d1.getTime(), d2.getTime());
	}

	@Override
	public SortedMap<Long, Boolean> berechneZustandsWechsel(Long von, Long bis, int jahr) {

		// Die Abfrage besitzt eine eigene Zustandsliste
		ListeZustandsWechsel listeZustandsWechselAbfrage = new ListeZustandsWechsel();

		Calendar calx = Calendar.getInstance();
		int diff = jahr - calx.get(Calendar.YEAR);
		Integer temp = calx.get(Calendar.YEAR) + diff;

		long time = 0;
		long days = 0;

		String s1 = null;
		String s2 = null;

		SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

		s1 = jahrVonBis.get(0)[0];
		s2 = jahrVonBis.get(0)[1];

		if (jahrVonBis.get(0)[0].contains("*"))
			s1 = jahrVonBis.get(0)[0].replace("*", temp.toString());

		if (jahrVonBis.get(0)[1].contains("*"))
			s2 = jahrVonBis.get(0)[1].replace("*", temp.toString());

		Long def1 = null;
		Long def2 = null;
		try {
			def1 = df1.parse(s1 + " " + zeitVonBis.get(0)[0]).getTime();
			def2 = df1.parse(s2 + " " + zeitVonBis.get(0)[1]).getTime();

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Date date1 = new Date();
		Date date2 = new Date();

		date1.setTime(def1);
		date2.setTime(def2);

		if (von < def1 && bis < def1) {
			return null;

		} else if (von > def2 && bis > def2) {
			return null;

		} else if (von < def1 && bis > def2) {

			von = def1;
			bis = def2;
		} else if (von < def1 && bis >= def1) {

			von = def1;

		} else if (von >= def1 && bis > def2) {

			bis = def2;
		} else if (von >= def1 && bis <= def2) {

		}

		date1.setTime(von);
		s1 = df1.format(date1);

		date2.setTime(bis);
		s2 = df1.format(date2);

		time = bis - von;

		days = Math.round(time / (24. * 60. * 60. * 1000.));

		// Der Ende-Tag zaehlt auch noch dazu
		if (definition.contains("<"))
			days += 1;

		Long sec = time / 1000;

		if (sec >= 0L && sec < 86399L)
			days = 1;

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
		Date dt = new Date();
		Calendar cal = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		String s[] = new String[2];
		s[0] = s1;
		s[1] = s2;

		ArrayList<String[]> list = new ArrayList<>();
		list.add(s);

		Iterator<String[]> it1 = list.iterator();
		while (it1.hasNext()) {

			String[] localJahr = it1.next();

			for (int i = 0; i < days; i++) {
				Iterator<String[]> it2 = zeitVonBis.iterator();

				int loc = 0;

				while (it2.hasNext()) {

					String[] localZeit = it2.next();

					try {

						if (localJahr[0].contains("*"))
							s1 = localJahr[0].replace("*", temp.toString());

						if (localJahr[1].contains("*"))
							s2 = localJahr[1].replace("*", temp.toString());

						dt = df.parse(s1 + " " + localZeit[0]);

						cal.setTime(dt);
						tmp = cal;
						tmp.add(Calendar.DATE, i);
						cal = tmp;

						String xx = jahrVonBis.get(0)[0];
						String yy = jahrVonBis.get(0)[1];

						if (jahrVonBis.get(0)[0].contains("*"))
							xx = jahrVonBis.get(0)[0].replace("*", temp.toString());

						if (jahrVonBis.get(0)[1].contains("*"))
							yy = jahrVonBis.get(0)[1].replace("*", temp.toString());

						Long aa = df.parse(xx + " " + zeitVonBis.get(loc)[0]).getTime();
						Long bb = df.parse(yy + " " + zeitVonBis.get(loc)[1]).getTime();

						date1.setTime(aa);

						if (aa <= cal.getTimeInMillis() && bb >= cal.getTimeInMillis())
							listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis(), true);

						dt = df.parse(s1 + " " + localZeit[1]);

						cal.setTime(dt);
						tmp = cal;
						tmp.add(Calendar.DATE, i);
						cal = tmp;

						date1.setTime(bb);

						if (aa <= cal.getTimeInMillis() && bb >= cal.getTimeInMillis())
							listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis(), false);

						loc++;

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

		return listeZustandsWechselAbfrage.getListeZustandsWechsel();
	}

	@Override
	public SortedMap<Long, Long> berechneIntervall(Long von, Long bis, int jahr) {

		// Die Abfrage besitzt eine eigene Zustandsliste
		SortedMap<Long, Long> liste = new TreeMap<>();

		Calendar calx = Calendar.getInstance();
		int diff = jahr - calx.get(Calendar.YEAR);
		Integer temp = calx.get(Calendar.YEAR) + diff;

		long time = 0;
		long days = 0;

		String s1 = null;
		String s2 = null;

		SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

		s1 = jahrVonBis.get(0)[0];
		s2 = jahrVonBis.get(0)[1];

		if (jahrVonBis.get(0)[0].contains("*"))
			s1 = jahrVonBis.get(0)[0].replace("*", temp.toString());

		if (jahrVonBis.get(0)[1].contains("*"))
			s2 = jahrVonBis.get(0)[1].replace("*", temp.toString());

		Long def1 = null;
		Long def2 = null;
		try {
			def1 = df1.parse(s1 + " " + zeitVonBis.get(0)[0]).getTime();
			def2 = df1.parse(s2 + " " + zeitVonBis.get(0)[1]).getTime();

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Date date1 = new Date();
		Date date2 = new Date();

		date1.setTime(def1);
		date2.setTime(def2);

		if (von < def1 && bis < def1) {

			return null;

		} else if (von > def2 && bis > def2) {

			return null;

		} else if (von < def1 && bis > def2) {

			von = def1;
			bis = def2;
		} else if (von < def1 && bis >= def1) {

			von = def1;

		} else if (von >= def1 && bis > def2) {

		} else if (von >= def1 && bis <= def2) {

		}

		date1.setTime(von);
		s1 = df1.format(date1);

		date2.setTime(bis);
		s2 = df1.format(date2);

		time = bis - von;

		days = Math.round(time / (24. * 60. * 60. * 1000.));

		// Der Ende-Tag zaehlt auch noch dazu
		if (definition.contains("<"))
			days += 1;

		Long sec = time / 1000;

		if (sec >= 0L && sec < 86399L)
			days = 1;

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
		Date dt = new Date();
		Calendar cal = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		String s[] = new String[2];
		s[0] = s1;
		s[1] = s2;

		ArrayList<String[]> list = new ArrayList<>();
		list.add(s);

		Iterator<String[]> it1 = list.iterator();
		while (it1.hasNext()) {

			String[] localJahr = it1.next();

			for (int i = 0; i < days; i++) {
				Iterator<String[]> it2 = zeitVonBis.iterator();

				int loc = 0;

				while (it2.hasNext()) {

					String[] localZeit = it2.next();

					try {

						if (localJahr[0].contains("*"))
							s1 = localJahr[0].replace("*", temp.toString());

						if (localJahr[1].contains("*"))
							s2 = localJahr[1].replace("*", temp.toString());

						dt = df.parse(s1 + " " + localZeit[0]);

						cal.setTime(dt);
						tmp = cal;
						tmp.add(Calendar.DATE, i);
						cal = tmp;

						String xx = jahrVonBis.get(0)[0];
						String yy = jahrVonBis.get(0)[1];

						if (jahrVonBis.get(0)[0].contains("*"))
							xx = jahrVonBis.get(0)[0].replace("*", temp.toString());

						if (jahrVonBis.get(0)[1].contains("*"))
							yy = jahrVonBis.get(0)[1].replace("*", temp.toString());

						String saa = xx + " " + zeitVonBis.get(loc)[0];
						String sbb = yy + " " + zeitVonBis.get(loc)[1];

						Long aa = df.parse(saa).getTime();
						Long bb = df.parse(sbb).getTime();

						date1.setTime(aa);

						Long l1 = null;
						Long l2 = null;

						if (aa <= cal.getTimeInMillis() && bb >= cal.getTimeInMillis()) {
							// listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis(),
							// true);
							l1 = cal.getTimeInMillis();
						}

						dt = df.parse(s1 + " " + localZeit[1]);

						// dt = df.parse(s2);
						cal.setTime(dt);
						tmp = cal;
						tmp.add(Calendar.DATE, i);
						cal = tmp;

						date1.setTime(bb);

						if (aa <= cal.getTimeInMillis() && bb >= cal.getTimeInMillis()) {
							// listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis(),
							// false);
							// listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis()+1,
							// false);
							l2 = cal.getTimeInMillis();
						}

						if (l1 != null && l2 != null) {
							if (bis >= l1)
								liste.put(l1, l2);
						} else if (l1 == null && l2 != null) {
							liste.put(von, l2);
						} else if (l1 != null && l2 == null) {
							if (bis >= l1)
								liste.put(l1, bis);
						}

						loc++;

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}

		return liste;
	}

	@Override
	public SortedMap<Long, Boolean> berechneZustandsWechselZustand(Long von, Long bis, int jahr) {

		// Die Abfrage besitzt eine eigene Zustandsliste
		// ListeZustandsWechsel listeZustandsWechselAbfrage = new
		// ListeZustandsWechsel();
		listeZustandsWechsel = new ListeZustandsWechsel();

		Calendar calx = Calendar.getInstance();
		int diff = jahr - calx.get(Calendar.YEAR);
		Integer temp = calx.get(Calendar.YEAR) + diff;

		long time = 0;
		long days = 0;

		String s1 = null;
		String s2 = null;

		SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

		s1 = jahrVonBis.get(0)[0];
		s2 = jahrVonBis.get(0)[1];

		if (jahrVonBis.get(0)[0].contains("*"))
			s1 = jahrVonBis.get(0)[0].replace("*", temp.toString());

		if (jahrVonBis.get(0)[1].contains("*"))
			s2 = jahrVonBis.get(0)[1].replace("*", temp.toString());

		Long def1 = null;
		Long def2 = null;
		try {
			def1 = df1.parse(s1 + " " + zeitVonBis.get(0)[0]).getTime();
			def2 = df1.parse(s2 + " " + zeitVonBis.get(0)[1]).getTime();
			// def1 = df1.parse(s1).getTime();
			// def2 = df1.parse(s2).getTime();

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Date date1 = new Date();
		Date date2 = new Date();

		date1.setTime(def1);
		date2.setTime(def2);

		// _debug.config("HierX!!!" + pid + " : " + bis + " : " + von);
		// _debug.config("HierX!!!" + pid + " : " + def2 + " : " + def1);

		if (von < def1 && bis < def1) {

			// _debug.config("raus1" + " : " + pid);
			return null;

		} else if (von > def2 && bis > def2) {

			// _debug.config("raus2" + " : " + pid);
			return null;

		} else if (von < def1 && bis > def2) {

			von = def1;
			bis = def2;
			// _debug.config("raus3" + " : " + pid);
		} else if (von < def1 && bis >= def1) {

			von = def1;
			// _debug.config("raus4" + " : " + pid);

		} else if (von >= def1 && bis > def2) {

			bis = def2;
			// _debug.config("raus5" + " : " + pid);

		} else if (von >= def1 && bis <= def2) {

		}

		date1.setTime(von);
		s1 = df1.format(date1);

		date2.setTime(bis);
		s2 = df1.format(date2);

		time = bis - von;

		days = Math.round(time / (24. * 60. * 60. * 1000.));

		// Der Ende-Tag zaehlt auch noch dazu, aber nur bei einem Zeitbereich
		if (definition.contains("<"))
			days += 1;

		Long sec = time / 1000;

		if (sec >= 0L && sec < 86399L) // 86399 = (24 * 60 * 60) - 1
			days = 1;

		// SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
		Date dt = new Date();
		Calendar cal = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		String s[] = new String[2];
		s[0] = s1;
		s[1] = s2;

		ArrayList<String[]> list = new ArrayList<>();
		list.add(s);

		Iterator<String[]> it1 = list.iterator();
		while (it1.hasNext()) {

			String[] localJahr = it1.next();

			for (int i = 0; i < days; i++) {
				Iterator<String[]> it2 = zeitVonBis.iterator();

				while (it2.hasNext()) {

					String[] localZeit = it2.next();

					try {

						if (localJahr[0].contains("*"))
							s1 = localJahr[0].replace("*", temp.toString());

						if (localJahr[1].contains("*"))
							s2 = localJahr[1].replace("*", temp.toString());

						dt = df.parse(s1 + " " + localZeit[0]);

						cal.setTime(dt);
						tmp = cal;
						tmp.add(Calendar.DATE, i);
						cal = tmp;

						String xx = jahrVonBis.get(0)[0];
						String yy = jahrVonBis.get(0)[1];

						if (jahrVonBis.get(0)[0].contains("*"))
							xx = jahrVonBis.get(0)[0].replace("*", temp.toString());

						if (jahrVonBis.get(0)[1].contains("*"))
							yy = jahrVonBis.get(0)[1].replace("*", temp.toString());

						Long aa = df.parse(xx + " " + zeitVonBis.get(0)[0]).getTime();
						Long bb = df.parse(yy + " " + zeitVonBis.get(0)[1]).getTime();

						date1.setTime(aa);

						// _debug.config(date1 + " -:- " + cal.getTime());

						if (aa <= cal.getTimeInMillis() && bb >= cal.getTimeInMillis())
							listeZustandsWechsel.getListeZustandsWechsel().put(cal.getTimeInMillis(), true);
						// listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis(),
						// true);

						// _debug.config(s1 + " " + _zeit[1]);

						dt = df.parse(s1 + " " + localZeit[1]);

						// dt = df.parse(s2);
						cal.setTime(dt);
						tmp = cal;
						tmp.add(Calendar.DATE, i);
						cal = tmp;

						date1.setTime(bb);

						// _debug.config(date1 + " -:- " + cal.getTime());

						if (aa <= cal.getTimeInMillis() && bb >= cal.getTimeInMillis())
							listeZustandsWechsel.getListeZustandsWechsel().put(cal.getTimeInMillis(), false);
						// listeZustandsWechselAbfrage.getListeZustandsWechsel().put(cal.getTimeInMillis()+1,
						// false);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}

		return listeZustandsWechsel.getListeZustandsWechsel();
		// return listeZustandsWechselAbfrage.getListeZustandsWechsel();
	}
}

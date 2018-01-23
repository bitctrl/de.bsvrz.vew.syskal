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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Die Klasse erzeugt SystemKalenderEintraege vom Typ: "Ostersonntag + 1Tag",
 * also Eintraege die durch bereits definierte Eintraege und die Angabe eines
 * Zeitwertes definiert werden.
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class DefinierterEintrag extends LogischeVerknuepfung {

	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Elemente der Verknuepfung
	 */
	private List<String> ergebnis;

	/**
	 * Die Verknuepfungsoperation
	 */
	private String symbol;

	/**
	 * Die Pid
	 */
	String pid;

	String definition;

	Object syncObject = new Object();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS", Locale.GERMAN);

	protected List<ListeZustandsWechsel> listListeZustandsWechsel = new ArrayList<>();

	public DefinierterEintrag(Map<String, SystemkalenderEintrag> skeList, String pid, String definition) {
		super(skeList, pid, definition);
		this.pid = pid;
		this.definition = definition;
		ergebnis = new ArrayList<>();
	}

	@Override
	public boolean pruefeEintrag() {

		if (super.pruefeEintrag()) {
			// Dann mache nichts
		} else {

			StringTokenizer st = new StringTokenizer(definition, "+-");

			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				ergebnis.add(s);
			}

			if (definition.contains("+"))
				symbol = "+";
			else
				symbol = "-";

			Calendar cal = Calendar.getInstance();

			if (eintragAufloesen(ergebnis.get(0), cal.get(Calendar.YEAR)))
				return true;
		}

		return false;

	}

	/**
	 * @param s
	 *            Kontext
	 * @return true, wenn Eitrag gefunden werden konnte
	 */
	public boolean eintragAufloesen(String s) {

		listListeZustandsWechsel.clear();

		for (SystemkalenderEintrag ske : getSkeList().values()) {

			String pid = ske.getPid();
			String val = ske.getDefinition();

			if (val != null) {

				if (pid.contains(s)) {
					Calendar cal = Calendar.getInstance();

					ListeZustandsWechsel listeZustandsWechsel = new ListeZustandsWechsel();
					listeZustandsWechsel.setListeZustandsWechsel(ske.getListeZustandsWechsel());
					listListeZustandsWechsel.add(listeZustandsWechsel);

					SortedMap<Long, Boolean> sm = berechneZustandsWechsel(cal.get(Calendar.YEAR));

					for (Map.Entry<Long, Boolean> me : sm.entrySet()) {

						Date d = new Date();
						d.setTime(me.getKey());

					}

					return true;
				}

			}
		}

		return false;

	}

	public boolean eintragAufloesen(String s, int jahr) {

		listListeZustandsWechsel.clear();

		// System.out.println(s + " : " + jahr);

		// for (SystemkalenderEintrag ske : getSkeList().values())
		for (Map.Entry<String, SystemkalenderEintrag> me : getSkeList().entrySet()) {

			SystemkalenderEintrag ske = me.getValue();
			String val = ske.getDefinition();

			if (val != null) {

				if (ske.getName().equals(s)) {
					Date d1 = null;
					Date d2 = null;

					try {
						d1 = sdf.parse("01.01." + jahr + " 00:00:00,000");
						d2 = sdf.parse("31.12." + jahr + " 23:59:59,999");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					SortedMap<Long, Boolean> list = ske.berechneZustandsWechsel(d1.getTime(), d2.getTime(), jahr);

					if ((list != null) && (list.size() > 0)) // Anpassung STS 2016.06.28 TD
					{
						Long old = null;
						Long oldDiffDays = list.firstKey();
						for (Map.Entry<Long, Boolean> meTmp : list.entrySet()) {
							d1.setTime(meTmp.getKey());
							if (meTmp.getValue()) {
								if (old != null) {
									Long neu = meTmp.getKey();
									Long diff = neu - old;

									long diffDays = diff / (24 * 60 * 60 * 1000);

									if (diffDays > 1) {

										SortedMap<Long, Boolean> subMapTmp = list.subMap(oldDiffDays, meTmp.getKey());

										ListeZustandsWechsel listeZustandsWechsel = new ListeZustandsWechsel();
										listeZustandsWechsel.setListeZustandsWechsel(subMapTmp);
										listListeZustandsWechsel.add(listeZustandsWechsel);

										oldDiffDays = meTmp.getKey();
									}

								}
								old = meTmp.getKey();
							}
						}
						SortedMap<Long, Boolean> subMapEnd = list.tailMap(oldDiffDays);

						ListeZustandsWechsel listeZustandsWechsel = new ListeZustandsWechsel();
						listeZustandsWechsel.setListeZustandsWechsel(subMapEnd);
						listListeZustandsWechsel.add(listeZustandsWechsel);
					}

					return true;
				}

			}
		}

		return false;

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

		ListeZustandsWechsel listeZustandsWechselAbfrage = new ListeZustandsWechsel();

		Calendar calx = Calendar.getInstance();
		int diff = jahr - calx.get(Calendar.YEAR);
		Integer temp = calx.get(Calendar.YEAR) + diff;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		cal1.set(Calendar.YEAR, temp);
		cal2.set(Calendar.YEAR, temp);
		tmp.set(Calendar.YEAR, temp);

		for (String s : ergebnis) {
			if (!eintragAufloesen(s, jahr)) {
				continue;
			}

			for (ListeZustandsWechsel loc : listListeZustandsWechsel) {
				SortedMap<Long, Boolean> map = loc.getListeZustandsWechsel();

				if (map == null || map.size() == 0)
					continue;

				Object[] oatmp = map.keySet().toArray();
				int size = oatmp.length;
				Object[] oa = new Object[2];
				if (oatmp.length > 1) {

					if ("+".equals(symbol)) {
						oa[0] = oatmp[size - 2];
						oa[1] = oatmp[size - 1];

					} else if ("-".equals(symbol)) {
						oa[0] = oatmp[0];
						oa[1] = oatmp[1];

					} else {
						LOGGER.error("Definierter Eintrag -> Fehler Rechenzeichen: " + symbol);
						return null;
					}
				}

				Date date = new Date();
				for (int i = 0; i < oa.length; i++) {
					Long l = (Long) oa[i];
					date.setTime(l);

					Ostern ostern = new Ostern();
					Calendar oStart = Ostern.Ostersonntag(jahr);

					Object o = oStart.clone();
					Calendar oEnde = (Calendar) o;
					oEnde.set(Calendar.HOUR_OF_DAY, 23);
					oEnde.set(Calendar.MINUTE, 59);
					oEnde.set(Calendar.SECOND, 59);
					oEnde.set(Calendar.MILLISECOND, 999);

					Calendar xy = new GregorianCalendar();
					xy.setTimeInMillis((Long) oa[i]);

					if (ostern.isOstersonntag(xy)) {

						SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss,SSS");

						String zeit = df1.format(xy.getTime());

						if (zeit.equals("00:00:00,000")) {

							// _debug.config("hier11");
							Calendar x1 = Ostern.Ostersonntag(jahr);
							oa[i] = x1.getTime().getTime();

						} else if (zeit.equals("23:59:59,999")) {

							// _debug.config("hier12");
							Calendar x2 = Ostern.Ostersonntag(jahr);
							x2.set(Calendar.HOUR_OF_DAY, 23);
							x2.set(Calendar.MINUTE, 59);
							x2.set(Calendar.SECOND, 59);
							x2.set(Calendar.MILLISECOND, 999);
							oa[i] = x2.getTime().getTime();

						} else {

							// _debug.config("hier13");
							Calendar x = Calendar.getInstance();
							x.setTimeInMillis((Long) oa[i]);
							x.set(Calendar.YEAR, jahr);
							oa[i] = x.getTime().getTime();

						}

					}

					if (l >= von && l <= bis) {

						String string = ergebnis.get(1);

						char[] cs = string.toCharArray();

						StringBuffer strBuff = new StringBuffer();

						for (int j = 0; j < cs.length; j++) {
							char c = cs[j];
							Character ch = c;
							if (Character.isDigit(ch))
								strBuff.append(ch);

						}

						int t = 0;
						try {
							t = Integer.parseInt(strBuff.toString());
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						tmp.setTimeInMillis(l);
						if ("+".equals(symbol)) {
							tmp.add(Calendar.DAY_OF_MONTH, t);
						} else if ("-".equals(symbol)) {
							tmp.add(Calendar.DAY_OF_MONTH, -t);
						} else {
							LOGGER.error("Fehler Rechenzeichen: " + pid);
						}

						Boolean b = map.get(l);
						listeZustandsWechselAbfrage.getListeZustandsWechsel().put(tmp.getTimeInMillis(), b);
					}
				}
			}

		}

		return listeZustandsWechselAbfrage.getListeZustandsWechsel();

	}

	@Override
	public SortedMap<Long, Long> berechneIntervall(Long von, Long bis, int jahr) {

		SortedMap<Long, Long> liste = new TreeMap<>();

		Calendar calx = Calendar.getInstance();
		int diff = jahr - calx.get(Calendar.YEAR);
		Integer temp = calx.get(Calendar.YEAR) + diff;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		cal1.set(Calendar.YEAR, temp);
		cal2.set(Calendar.YEAR, temp);
		tmp.set(Calendar.YEAR, temp);

		if (!eintragAufloesen(ergebnis.get(0), jahr)) {
			return null;
		}

		SortedMap<Long, Boolean> map = null;

		if (map == null || map.size() == 0)
			return null;

		Object[] oa = map.keySet().toArray();
		int size = oa.length;

		Date date = new Date();

		Long l1 = null;
		Long l2 = null;

		for (int i = 0; i < oa.length; i++) {
			Long l = (Long) oa[i];
			date.setTime(l);

			Ostern ostern = new Ostern();
			Calendar oStart = Ostern.Ostersonntag(jahr);

			Object o = oStart.clone();
			Calendar oEnde = (Calendar) o;
			oEnde.set(Calendar.HOUR_OF_DAY, 23);
			oEnde.set(Calendar.MINUTE, 59);
			oEnde.set(Calendar.SECOND, 59);
			oEnde.set(Calendar.MILLISECOND, 999);

			Calendar xy = new GregorianCalendar();
			xy.setTimeInMillis((Long) oa[i]);

			if (ostern.isOstersonntag(xy)) {

				SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss,SSS");

				String zeit = df1.format(xy.getTime());

				if (zeit.equals("00:00:00,000")) {

					// _debug.config("hier11");
					Calendar x1 = Ostern.Ostersonntag(jahr);
					oa[i] = x1.getTime().getTime();

				} else if (zeit.equals("23:59:59,999")) {

					// _debug.config("hier12");
					Calendar x2 = Ostern.Ostersonntag(jahr);
					x2.set(Calendar.HOUR_OF_DAY, 23);
					x2.set(Calendar.MINUTE, 59);
					x2.set(Calendar.SECOND, 59);
					x2.set(Calendar.MILLISECOND, 999);
					oa[i] = x2.getTime().getTime();

				} else {

					// _debug.config("hier13");
					Calendar x = Calendar.getInstance();
					x.setTimeInMillis((Long) oa[i]);
					x.set(Calendar.YEAR, jahr);
					oa[i] = x.getTime().getTime();

				}

			}

			// Calendar calTmp1 = GregorianCalendar.getInstance();
			cal1.setTimeInMillis(von);
			// Calendar calTmp2 = GregorianCalendar.getInstance();
			cal2.setTimeInMillis(bis);

			cal1.set(Calendar.HOUR_OF_DAY, 0);
			cal1.set(Calendar.MINUTE, 0);
			cal1.set(Calendar.SECOND, 0);
			cal1.set(Calendar.MILLISECOND, 0);

			cal2.set(Calendar.HOUR_OF_DAY, 23);
			cal2.set(Calendar.MINUTE, 59);
			cal2.set(Calendar.SECOND, 59);
			cal2.set(Calendar.MILLISECOND, 999);

			// if (l >= von && l <= bis)
			if (l >= cal1.getTimeInMillis() && l <= cal2.getTimeInMillis()) {

				String string = ergebnis.get(1);

				char[] cs = string.toCharArray();

				StringBuffer strBuff = new StringBuffer();

				for (int j = 0; j < cs.length; j++) {
					char c = cs[j];
					Character ch = c;
					if (Character.isDigit(ch))
						strBuff.append(ch);

				}

				int t = 0;
				try {
					t = Integer.parseInt(strBuff.toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				tmp.setTimeInMillis(l);
				if ("+".equals(symbol)) {
					tmp.add(Calendar.DAY_OF_MONTH, t);
				} else if ("-".equals(symbol)) {
					tmp.add(Calendar.DAY_OF_MONTH, -t);
				} else {
					LOGGER.error("Fehler Rechenzeichen: " + pid);
				}

				Boolean b = map.get(l);

				if (b)
					l1 = tmp.getTimeInMillis();
				else
					l2 = tmp.getTimeInMillis();

				if (l1 != null && l2 != null) {
					liste.put(l1, l2);
					l1 = null;
					l2 = null;
				}
			}
		}

		return liste;
	}

	@Override
	public SortedMap<Long, Boolean> berechneZustandsWechselZustand(Long von, Long bis, int jahr) {

		listeZustandsWechsel = new ListeZustandsWechsel();

		Calendar calx = Calendar.getInstance();
		int diff = jahr - calx.get(Calendar.YEAR);
		Integer temp = calx.get(Calendar.YEAR) + diff;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		cal1.set(Calendar.YEAR, temp);
		cal2.set(Calendar.YEAR, temp);
		tmp.set(Calendar.YEAR, temp);

		Date dt = cal1.getTime();

		if (!eintragAufloesen(ergebnis.get(0), jahr)) {
			return null;
		}

		for (ListeZustandsWechsel loc : listListeZustandsWechsel) {
			SortedMap<Long, Boolean> map = loc.getListeZustandsWechsel();

			if (map == null || map.size() == 0)
				continue;

			Object[] oa = map.keySet().toArray();

			int size = oa.length;

			Date date = new Date();
			for (int i = 0; i < oa.length; i++) {

				date.setTime((Long) oa[i]);

				Date d = new Date();
				d.setTime((Long) oa[i]);

				Ostern ostern = new Ostern();
				Calendar oStart = Ostern.Ostersonntag(jahr);

				Object o = oStart.clone();
				Calendar oEnde = (Calendar) o;
				oEnde.set(Calendar.HOUR_OF_DAY, 23);
				oEnde.set(Calendar.MINUTE, 59);
				oEnde.set(Calendar.SECOND, 59);
				oEnde.set(Calendar.MILLISECOND, 999);

				Calendar xy = new GregorianCalendar();
				xy.setTimeInMillis((Long) oa[i]);

				if (ostern.isOstersonntag(xy)) {

					SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss,SSS");
					String zeit = df1.format(xy.getTime());

					if (zeit.equals("00:00:00,000")) {

						Calendar x1 = Ostern.Ostersonntag(jahr);
						oa[i] = x1.getTime().getTime();

					} else if (zeit.equals("23:59:59,999")) {
						Calendar x2 = Ostern.Ostersonntag(jahr);
						x2.set(Calendar.HOUR_OF_DAY, 23);
						x2.set(Calendar.MINUTE, 59);
						x2.set(Calendar.SECOND, 59);
						x2.set(Calendar.MILLISECOND, 999);
						oa[i] = x2.getTime().getTime();

					} else {
						Calendar x = Calendar.getInstance();
						x.setTimeInMillis((Long) oa[i]);
						x.set(Calendar.YEAR, jahr);
						oa[i] = x.getTime().getTime();

					}

				}

			}

			if ("+".equals(symbol)) {

				dt.setTime((Long) oa[size - 2]);
				cal1.setTime(dt);
				dt.setTime((Long) oa[size - 1]);
				cal2.setTime(dt);

			} else if ("-".equals(symbol)) {

				dt.setTime((Long) oa[0]);
				cal1.setTime(dt);
				dt.setTime((Long) oa[1]);
				cal2.setTime(dt);

			} else {
				LOGGER.error("Fehler Rechenzeichen: " + pid);
			}

			String string = ergebnis.get(1);

			char[] cs = string.toCharArray();

			StringBuffer strBuff = new StringBuffer();

			for (int i = 0; i < cs.length; i++) {
				char c = cs[i];
				Character ch = c;
				if (Character.isDigit(ch))
					strBuff.append(ch);

			}

			int t = 0;
			try {
				t = Integer.parseInt(strBuff.toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ("-".equals(symbol))
				t = -t;

			tmp = cal1;
			tmp.add(Calendar.DATE, t);
			cal1 = tmp;

			listeZustandsWechsel.getListeZustandsWechsel().put(cal1.getTimeInMillis(), true);

			tmp = cal2;
			tmp.add(Calendar.DATE, t);
			cal2 = tmp;

			listeZustandsWechsel.getListeZustandsWechsel().put(cal1.getTimeInMillis(), false);
		}

		return listeZustandsWechsel.getListeZustandsWechsel();
	}

	@Override
	protected DefinierterEintrag clone() {

		DefinierterEintrag eintrag = null;

		eintrag = new DefinierterEintrag(SystemkalenderArbeiter.getSkeList(), pid, definition);
		eintrag.setObjektListeZustandsWechsel(listeZustandsWechsel);

		return eintrag;
	}
}

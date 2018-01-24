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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Die Klasse erzeugt SystemKalenderEintraege vom Typ: "ODER{Samstag,
 * Sonntag}*,*" oder "UND{MoBisSa, NICHT Feiertag}*,*". Die Berechnung der
 * Zeitpunkte mit Wildcards bezieht sich noch auf das aktuelle Kalenderjahr. Die
 * Vorgehensweise könnte dahingehend sein, dass beim Jahrewechsel die
 * Zustandswechsel neu berechnet werden. Muss also noch geklÃÂ¤rt werden *
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class LogischeVerknuepfung extends DatumJahr {

	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Liste der einzelnen Teile der Verknuepfung
	 */
	private List<String> ergebnisse;

	/**
	 * Teporaere Zustandsliste
	 */
	protected ListeZustandsWechsel listeZustandsWechselTmp2 = new ListeZustandsWechsel();

	/**
	 * Die extrahierten Verknuepfungen
	 */
	private String verknuepfung;

	/**
	 * Eigene Liste mit Systemkalenderientraegen
	 */
	private Map<String, SystemkalenderEintrag> skeList;

	public LogischeVerknuepfung(Map<String, SystemkalenderEintrag> skeList, String pid, String value) {

		super(pid, value);
		ergebnisse = new ArrayList<>();
		this.skeList = skeList;

	}

	@Override
	public boolean pruefeEintrag() {

		if (!(definition.contains("{") && definition.contains("}"))) {
			return false;
		}

		verknuepfung = definition.substring(0, definition.indexOf("{"));

		String klammerAusdruck = definition.substring(definition.indexOf("{") + 1, definition.indexOf("}"));

		StringTokenizer st = new StringTokenizer(klammerAusdruck, ",");

		boolean neg = false;
		while (st.hasMoreTokens()) {
			String s = st.nextToken();

			if (s.contains("nicht")) {

				neg = true;
				s = s.replace("nicht", "");
				s = s.trim();

			}

			String skePid = null;

			for (Map.Entry<String, SystemkalenderEintrag> me : getSkeList().entrySet()) {

				String str = me.getValue().getName();

				if (s.equalsIgnoreCase(str)) {

					skePid = me.getKey();
					break;

				}

			}

			if (skePid == null) {
				LOGGER.fine(s + " nicht gefunden");
				return false;

			}

			if (!getSkeList().containsKey(skePid)) {
				LOGGER.fine("Ske nicht gefunden: " + skePid);
				return false;

			} else if (neg) {

				ergebnisse.add("nicht " + skePid);
				neg = false;

			} else {

				ergebnisse.add(skePid);

			}
		}

		String tmp = definition.substring(definition.indexOf("}") + 1);

		if (tmp.length() > 0) {
			st = new StringTokenizer(tmp, ", ");

			if (st.countTokens() != 2) {
				return false;
			}

			String[] vonbis = new String[2];
			int j = 0;
			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				vonbis[j] = s;
				j++;
			}

			jahrVonBis.add(vonbis);
		} else {

			LOGGER.error(pid + " : Fehler: keine Anfangs- bzw. Endezeit");

		}

		return true;

	}

	/**
	 * Berechen einer Dummyliste zum Bilden des Komplements
	 * 
	 * @param von
	 *            Startzeitpunkt der Berechnung
	 * @param bis
	 *            Endezeitpunkt der Berechnung
	 * @return zeitlich sortierte Liste mit dem Zeitpunkten der Zustandswechsel und
	 *         dem Zustand
	 */
	public static SortedMap<Long, Boolean> berechneNegativZustandsWechsel(String von, String bis) {

		long time = 0;
		long days = 0;
		SortedMap<Long, Boolean> timeStamps = new TreeMap<>();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = new GregorianCalendar();
		Calendar tmp = new GregorianCalendar();

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
		Date dt = new Date();

		try {
			// Ein Jahr beginnt um...
			dt = df.parse("01.01." + cal1.get(Calendar.YEAR) + " 00:00:00,000");
			cal1.setTime(dt);
			// Ein Jahr endet um...
			dt = df.parse("31.12." + cal1.get(Calendar.YEAR) + " 23:59:59,999");
			cal2.setTime(dt);

			Integer a = new Integer(von);
			Integer e = new Integer(bis);

			cal1.set(Calendar.YEAR, a);
			cal2.set(Calendar.YEAR, e);

			// Wie viele Tage hat das Jahr?
			time = cal2.getTime().getTime() - cal1.getTime().getTime();
			days = Math.round(time / (24. * 60. * 60. * 1000.));

			// Der erste Tag des Jahres endet um...
			dt = df.parse("01.01." + cal1.get(Calendar.YEAR) + " 23:59:59,999");
			cal2.setTime(dt);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < days; i++) {

			timeStamps.put(cal1.getTimeInMillis(), true);

			tmp = cal1;
			tmp.add(Calendar.DATE, 1);
			cal1 = tmp;

			timeStamps.put(cal2.getTimeInMillis(), false);
			// timeStamps.put(cal2.getTimeInMillis()+1, false);

			tmp = cal2;
			tmp.add(Calendar.DATE, 1);
			cal2 = tmp;

		}

		return timeStamps;
	}

	/**
	 * @return Map
	 */
	public Map<String, SystemkalenderEintrag> getSkeList() {
		return skeList;
	}

	public void setSkeList(Map<String, SystemkalenderEintrag> skeList) {
		this.skeList = skeList;
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

		try {
			Integer temp = jahr;

			// _debug.config(pid + " : " + definition);

			long start = 0;
			long ende = 0;
			Calendar cal = Calendar.getInstance();

			Map<Long, Boolean> negativList = new HashMap<>();
			Map<Long, Boolean> tmpEntryList = new HashMap<>();

			String s1 = null;
			String s2 = null;

			// Gesamtliste erstellen
			for (String s : ergebnisse) {

				if (jahrVonBis.get(0)[0].contains("*")) {

					s1 = jahrVonBis.get(0)[0].replace("*", temp.toString());

				} else {

					s1 = jahrVonBis.get(0)[0];

				}

				if (jahrVonBis.get(0)[1].contains("*")) {

					s2 = jahrVonBis.get(0)[1].replace("*", temp.toString());

				} else {
					s2 = jahrVonBis.get(0)[1];

				}

				if (s.contains("nicht")) {

					String[] tmp = s.split(" ");

					SystemkalenderEintrag ske = null;

					if (tmp.length == 2) {

						ske = getSkeList().get(tmp[1]);

					} else {
						LOGGER.error("Fehler Split: " + s);
						return null;

					}

					negativList = berechneNegativZustandsWechsel(s1, s2);

					SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

					if (map != null) {

						for (Long key : map.keySet()) {
							Date date = new Date();
							date.setTime(key);
							negativList.remove(key);

						}
					}

					tmpEntryList.putAll(negativList);

				} else {

					SystemkalenderEintrag ske = getSkeList().get(s);

					SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

					if (map != null)
						tmpEntryList.putAll(map);

				}

			}

			Set<Long> tmpSet = new TreeSet<>();

			boolean flag = false;
			for (String s : ergebnisse) {

				String tmp = s.replace("nicht ", "");
				SystemkalenderEintrag ske = getSkeList().get(tmp);

				if (ske != null) {
					ske.berechneZustandsWechsel(von, bis, jahr);
				} else {
					// _debug.config(s + " ist Null!!!!!!");
					continue;
				}

				if (verknuepfung.contains("und")) {

					// Schnittmenge kann nur bei einer nichtleeren Menge gebildet
					// werden, deshalb erst einmal eine nichtleere Menge erzeugen.
					if (!flag) {

						flag = true;

						if (s.contains("nicht")) {
							tmpSet.addAll(negativList.keySet());
						} else {
							SortedMap<Long, Boolean> liste = ske.berechneZustandsWechsel(von, bis, jahr);
							if (liste != null)
								tmpSet.addAll(liste.keySet());
						}

					} // ...jetzt ist die Menge nicht mehr leer!
					else {
						if (s.contains("nicht")) {

							Object[] a = tmpSet.toArray();

							Set<Long> skeSet = negativList.keySet();

							Object[] b = skeSet.toArray();

							Set<Long> tmp2Set = new TreeSet<>();

							tmpSet = new TreeSet<>();

							for (int i = 0; i < a.length; i += 2) {
								Long i1A = (Long) a[i];
								Long i1E = i1A;
								if (i < a.length - 1)
									i1E = (Long) a[i + 1];

								for (int j = 0; j < b.length; j += 2) {
									Long i2A = (Long) b[j];
									Long i2E = i2A;
									if (j < b.length - 1)
										i2E = (Long) b[j + 1];

									Long x1 = i1A > i2A ? i1A : i2A;
									Long x2 = i1E < i2E ? i1E : i2E;

									if (x2 > x1) {

										tmp2Set.add(x1);
										tmp2Set.add(x2);

									}

								}

							}

							tmpSet.addAll(tmp2Set);

						} else {

							Object[] a = tmpSet.toArray();

							SortedMap<Long, Boolean> liste = ske.berechneZustandsWechsel(von, bis, jahr);

							if (liste != null) {

								Set<Long> skeSet = liste.keySet();

								Object[] b = skeSet.toArray();

								Set<Long> tmp2Set = new TreeSet<>();

								tmpSet = new TreeSet<>();

								for (int i = 0; i < a.length - 1; i += 2) {
									Long i1A = (Long) a[i];
									Long i1E = i1A;
									if (i < a.length - 1)
										i1E = (Long) a[i + 1];
									// Long i1E = (Long)a[i + 1];

									for (int j = 0; j < b.length - 1; j += 2) {
										Long i2A = (Long) b[j];
										Long i2E = i2A;
										if (j < b.length - 1)
											i2E = (Long) b[j + 1];
										// Long i2E = (Long)b[j + 1];

										Long x1 = i1A > i2A ? i1A : i2A;
										Long x2 = i1E < i2E ? i1E : i2E;

										if (x2 > x1) {

											tmp2Set.add(x1);
											tmp2Set.add(x2);
										}
									}
								}
								tmpSet.addAll(tmp2Set);
							}
						}
					}
				} else if (verknuepfung.contains("oder")) {
					if (s.contains("nicht")) {
						tmpSet.addAll(negativList.keySet());
					} else {

						SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

						if (map != null)
							tmpSet.addAll(map.keySet());

					}
				} else if (s.contains("nicht")) {
					tmpSet.addAll(negativList.keySet());
				} else {
					LOGGER.error("Fehler kein UND, ODER, NICHT!!!");
				}

			}

			Iterator<Long> it = tmpSet.iterator();
			while (it.hasNext()) {
				Long key = it.next();
				listeZustandsWechselTmp2.getListeZustandsWechsel().put(key, tmpEntryList.get(key));
			}

			Date dt = new Date();

			dt.setTime(von);
			cal.setTime(dt);
			cal.set(Calendar.YEAR, jahr);

			start = dt.getTime();

			dt.setTime(bis);
			cal.setTime(dt);
			cal.set(Calendar.YEAR, jahr);

			ende = dt.getTime();

			listeZustandsWechselAbfrage.setListeZustandsWechsel(listeZustandsWechselTmp2.berechneVonBis(start, ende));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listeZustandsWechselAbfrage.getListeZustandsWechsel();
	}

	@Override
	public SortedMap<Long, Long> berechneIntervall(Long von, Long bis, int jahr) {
		// Die Abfrage besitzt eine eigene Zustandsliste
		// ListeZustandsWechsel listeZustandsWechselAbfrage = new
		// ListeZustandsWechsel();
		SortedMap<Long, Long> listeGruppen = new TreeMap<>();

		try {
			Integer temp = jahr;

			// _debug.config(pid + " : " + definition);

			long start = 0;
			long ende = 0;
			Calendar cal = Calendar.getInstance();

			Map<Long, Boolean> negativList = new HashMap<>();
			Map<Long, Boolean> tmpEntryList = new HashMap<>();

			String s1 = null;
			String s2 = null;

			// Gesamtliste erstellen
			for (String s : ergebnisse) {

				if (jahrVonBis.get(0)[0].contains("*")) {

					s1 = jahrVonBis.get(0)[0].replace("*", temp.toString());

				} else {

					s1 = jahrVonBis.get(0)[0];

				}

				if (jahrVonBis.get(0)[1].contains("*")) {

					s2 = jahrVonBis.get(0)[1].replace("*", temp.toString());

				} else {
					s2 = jahrVonBis.get(0)[1];

				}

				if (s.contains("nicht")) {

					String[] tmp = s.split(" ");

					SystemkalenderEintrag ske = null;

					if (tmp.length == 2) {

						ske = getSkeList().get(tmp[1]);

					} else {
						LOGGER.error("Fehler Split: " + s);
						return null;

					}

					negativList = berechneNegativZustandsWechsel(s1, s2);

					SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

					if (map != null) {

						for (Long key : map.keySet()) {
							Date date = new Date();
							date.setTime(key);
							negativList.remove(key);

						}
					}

					tmpEntryList.putAll(negativList);

				} else {

					SystemkalenderEintrag ske = getSkeList().get(s);

					SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

					if (map != null)
						tmpEntryList.putAll(map);

				}

			}

			Set<Long> tmpSet = new TreeSet<>();

			boolean flag = false;
			for (String s : ergebnisse) {

				String tmp = s.replace("nicht ", "");
				SystemkalenderEintrag ske = getSkeList().get(tmp);

				if (ske != null) {
					ske.berechneZustandsWechsel(von, bis, jahr);
				} else {
					// _debug.config(s + " ist Null!!!!!!");
					continue;
				}

				if (verknuepfung.contains("und")) {

					// Schnittmenge kann nur bei einer nichtleeren Menge gebildet
					// werden, deshalb erst einmal eine nichtleere Menge erzeugen.
					if (!flag) {

						flag = true;

						if (s.contains("nicht")) {
							tmpSet.addAll(negativList.keySet());
						} else {
							SortedMap<Long, Boolean> liste = ske.berechneZustandsWechsel(von, bis, jahr);
							if (liste != null)
								tmpSet.addAll(liste.keySet());
						}

					} // ...jetzt ist die Menge nicht mehr leer!
					else {
						if (s.contains("nicht")) {

							Object[] a = tmpSet.toArray();

							Set<Long> skeSet = negativList.keySet();

							Object[] b = skeSet.toArray();

							Set<Long> tmp2Set = new TreeSet<>();

							tmpSet = new TreeSet<>();

							for (int i = 0; i < a.length; i += 2) {
								Long i1A = (Long) a[i];
								Long i1E = i1A;
								if (i < a.length - 1)
									i1E = (Long) a[i + 1];

								for (int j = 0; j < b.length; j += 2) {
									Long i2A = (Long) b[j];
									Long i2E = i2A;
									if (j < b.length - 1)
										i2E = (Long) b[j + 1];

									Long x1 = i1A > i2A ? i1A : i2A;
									Long x2 = i1E < i2E ? i1E : i2E;

									if (x2 > x1) {

										tmp2Set.add(x1);
										tmp2Set.add(x2);

									}

								}

							}

							tmpSet.addAll(tmp2Set);

						} else {

							Object[] a = tmpSet.toArray();

							SortedMap<Long, Boolean> liste = ske.berechneZustandsWechsel(von, bis, jahr);

							if (liste != null) {

								Set<Long> skeSet = liste.keySet();

								Object[] b = skeSet.toArray();

								Set<Long> tmp2Set = new TreeSet<>();

								tmpSet = new TreeSet<>();

								for (int i = 0; i < a.length; i += 2) {
									Long i1A = (Long) a[i];
									Long i1E = (Long) a[i + 1];

									for (int j = 0; j < b.length; j += 2) {
										Long i2A = (Long) b[j];
										Long i2E = (Long) b[j + 1];

										Long x1 = i1A > i2A ? i1A : i2A;
										Long x2 = i1E < i2E ? i1E : i2E;

										if (x2 > x1) {

											tmp2Set.add(x1);
											tmp2Set.add(x2);

										}

									}

								}

								tmpSet.addAll(tmp2Set);

							}
						}
					}
				} else if (verknuepfung.contains("oder")) {
					if (s.contains("nicht")) {
						tmpSet.addAll(negativList.keySet());
					} else {

						SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

						if (map != null)
							tmpSet.addAll(map.keySet());

					}
				} else if (s.contains("nicht")) {
					tmpSet.addAll(negativList.keySet());
				} else {
					LOGGER.error("Fehler kein UND, ODER, NICHT!!!");
				}

			}

			Iterator<Long> it = tmpSet.iterator();
			while (it.hasNext()) {
				Long key = it.next();
				listeZustandsWechselTmp2.getListeZustandsWechsel().put(key, tmpEntryList.get(key));
			}

			Date dt = new Date();

			dt.setTime(von);
			cal.setTime(dt);
			cal.set(Calendar.YEAR, jahr);

			start = dt.getTime();

			dt.setTime(bis);
			cal.setTime(dt);
			cal.set(Calendar.YEAR, jahr);

			ende = dt.getTime();

			SortedMap<Long, Boolean> tmp = listeZustandsWechselTmp2.berechneVonBis(start, ende);

			Long l1 = null;
			Long l2 = null;
			for (Map.Entry<Long, Boolean> me : tmp.entrySet()) {
				if (me.getValue())
					l1 = me.getKey();
				else
					l2 = me.getKey();
				if (l1 != null && l2 != null) {
					listeGruppen.put(l1, l2);
					l1 = null;
					l2 = null;
				}

			}

			// listeZustandsWechselAbfrage.setListeZustandsWechsel(listeZustandsWechselTmp.berechneVonBis(start,
			// ende));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listeGruppen;
	}

	@Override
	public SortedMap<Long, Boolean> berechneZustandsWechselZustand(Long von, Long bis, int jahr) {
		// Die Abfrage besitzt eine eigene Zustandsliste

		listeZustandsWechsel = new ListeZustandsWechsel();

		try {
			Integer temp = jahr;

			// _debug.config(pid + " : " + definition);

			long start = 0;
			long ende = 0;
			Calendar cal = Calendar.getInstance();

			Map<Long, Boolean> negativList = new HashMap<>();
			Map<Long, Boolean> tmpEntryList = new HashMap<>();

			String s1 = null;
			String s2 = null;

			// Gesamtliste erstellen
			for (String s : ergebnisse) {

				if (jahrVonBis.get(0)[0].contains("*")) {

					s1 = jahrVonBis.get(0)[0].replace("*", temp.toString());

				} else {

					s1 = jahrVonBis.get(0)[0];

				}

				if (jahrVonBis.get(0)[1].contains("*")) {

					s2 = jahrVonBis.get(0)[1].replace("*", temp.toString());

				} else {
					s2 = jahrVonBis.get(0)[1];

				}

				if (s.contains("nicht")) {

					String[] tmp = s.split(" ");

					SystemkalenderEintrag ske = null;

					if (tmp.length == 2) {

						ske = getSkeList().get(tmp[1]);

					} else {
						LOGGER.error("Fehler: " + s);
						return null;

					}

					negativList = berechneNegativZustandsWechsel(s1, s2);

					SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

					for (Long key : map.keySet()) {
						Date date = new Date();
						date.setTime(key);
						negativList.remove(key);

					}

					tmpEntryList.putAll(negativList);

				} else {

					SystemkalenderEintrag ske = getSkeList().get(s);

					SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

					if (map != null)
						tmpEntryList.putAll(map);
				}
			}

			Set<Long> tmpSet = new TreeSet<>();

			boolean flag = false;
			for (String s : ergebnisse) {

				String tmp = s.replace("nicht ", "");
				SystemkalenderEintrag ske = getSkeList().get(tmp);

				if (ske != null) {
					ske.berechneZustandsWechsel(von, bis, jahr);
				} else {
					// _debug.config(s + " ist Null!!!!!!");
					continue;
				}

				if (verknuepfung.contains("und")) {

					// Schnittmenge kann nur bei einer nichtleeren Menge gebildet
					// werden, deshalb erst einmal eine nichtleere Menge erzeugen.
					if (!flag) {

						flag = true;

						if (s.contains("nicht")) {
							tmpSet.addAll(negativList.keySet());
						} else {
							SortedMap<Long, Boolean> sm = ske.berechneZustandsWechsel(von, bis, jahr);
							if (sm != null)
								tmpSet.addAll(sm.keySet());
						}

					} // ...jetzt ist die Menge nicht mehr leer!
					else {
						if (s.contains("nicht")) {

							Object[] a = tmpSet.toArray();

							Set<Long> skeSet = negativList.keySet();

							Object[] b = skeSet.toArray();

							Set<Long> tmp2Set = new TreeSet<>();

							tmpSet = new TreeSet<>();

							for (int i = 0; i < a.length; i += 2) {
								Long i1A = (Long) a[i];
								Long i1E = (Long) a[i + 1];

								for (int j = 0; j < b.length; j += 2) {
									Long i2A = (Long) b[j];
									Long i2E = (Long) b[j + 1];

									Long x1 = i1A > i2A ? i1A : i2A;
									Long x2 = i1E < i2E ? i1E : i2E;

									if (x2 > x1) {

										tmp2Set.add(x1);
										tmp2Set.add(x2);

									}

								}

							}

							tmpSet.addAll(tmp2Set);

						} else {

							Object[] a = tmpSet.toArray();

							SortedMap<Long, Boolean> sm = ske.berechneZustandsWechsel(von, bis, jahr);

							if (sm != null) {

								Set<Long> skeSet = sm.keySet();

								Object[] b = skeSet.toArray();

								Set<Long> tmp2Set = new TreeSet<>();

								tmpSet = new TreeSet<>();

								for (int i = 0; i < a.length; i += 2) {
									Long i1A = (Long) a[i];
									Long i1E = (Long) a[i + 1];

									for (int j = 0; j < b.length; j += 2) {
										Long i2A = (Long) b[j];
										Long i2E = (Long) b[j + 1];

										Long x1 = i1A > i2A ? i1A : i2A;
										Long x2 = i1E < i2E ? i1E : i2E;

										if (x2 > x1) {

											tmp2Set.add(x1);
											tmp2Set.add(x2);

										}

									}

								}

								tmpSet.addAll(tmp2Set);

							}
						}
					}
				} else if (verknuepfung.contains("oder")) {
					if (s.contains("nicht")) {
						tmpSet.addAll(negativList.keySet());
					} else {

						SortedMap<Long, Boolean> map = ske.berechneZustandsWechsel(von, bis, jahr);

						if (map != null)
							tmpSet.addAll(map.keySet());

					}
				} else if (s.contains("nicht")) {
					tmpSet.addAll(negativList.keySet());
				} else {
					LOGGER.error("Fehler kein UND, ODER, NICHT!!!");
				}

			}

			Iterator<Long> it = tmpSet.iterator();
			while (it.hasNext()) {
				Long key = it.next();
				listeZustandsWechselTmp2.getListeZustandsWechsel().put(key, tmpEntryList.get(key));
			}

			Date dt = new Date();

			dt.setTime(von);
			cal.setTime(dt);
			cal.set(Calendar.YEAR, jahr);

			start = dt.getTime();

			dt.setTime(bis);
			cal.setTime(dt);
			cal.set(Calendar.YEAR, jahr);

			ende = dt.getTime();

			listeZustandsWechsel.setListeZustandsWechsel(listeZustandsWechselTmp2.berechneVonBis(start, ende));
			// listeZustandsWechselAbfrage.setListeZustandsWechsel(listeZustandsWechselTmp.berechneVonBis(start,
			// ende));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listeZustandsWechsel.getListeZustandsWechsel();
	}
}

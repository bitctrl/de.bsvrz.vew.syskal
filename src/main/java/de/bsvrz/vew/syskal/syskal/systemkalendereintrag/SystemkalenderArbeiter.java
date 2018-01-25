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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataState;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.syskal.benachrichtigungsfunktion.BenachrichtigeEvent;
import de.bsvrz.vew.syskal.syskal.benachrichtigungsfunktion.BenachrichtigeFunktion;
import de.bsvrz.vew.syskal.syskal.benachrichtigungsfunktion.BenachrichtigeListener;

/**
 * Klasse die Methoden bereitstellt, welche die Systemkalender-Bibliothek
 * benutzt. Es kann damit ein Systemkalender aufgebaut werden!
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class SystemkalenderArbeiter
		implements ClientReceiverInterface, ClientSenderInterface, MutableSetChangeListener, BenachrichtigeListener {

	private static final Debug LOGGER = Debug.getLogger();
	private ClientDavInterface connection;

	/**
	 * Datenmodell
	 */
	private DataModel datenmodell;

	/**
	 * Simulationsvariante
	 */
	private short simulationsvariante;

	/**
	 * Empfaengeroption
	 */
	private ReceiveOptions empfaengeroptionen;

	/**
	 * Empfaengerrrolle
	 */
	private ReceiverRole empfaengerrolle;

	/**
	 * Konfigurationsobjekt
	 */
	private ConfigurationObject configObj;

	/**
	 * Liste mit den Pid's der SystemKalenderEintraege
	 */
	private Map<String, String[]> parseList = new HashMap<>();

	/**
	 * Die statische Liste der SystemKalenderEintraege
	 */
	private Map<String, AlterSystemkalenderEintrag> skeList = new HashMap<>();

	/**
	 * Zaehler fuer SystemKalenderEintraege
	 */
	private int cntSke;

	/**
	 * Zaehler fuer geparste Ske
	 */
	private int cntParse;

	/**
	 * String fuer den Kalender
	 */
	private String kalender;

	/**
	 * String fuer den Kalender
	 */
	private Boolean inInit;

	/**
	 * Instanz des Singletons
	 */
	private static SystemkalenderArbeiter instance = null;

	private boolean used = false;

	private DataDescription datenbeschreibung;

	public SystemkalenderArbeiter(ClientDavInterface connection, String kalender) {
		this.connection = connection;
		this.kalender = kalender;
		this.used = true;

	}

	public static SystemkalenderArbeiter getInstance(ClientDavInterface connection, String kalender) {

		if (instance == null) {
			instance = new SystemkalenderArbeiter(connection, kalender);
		}

		return instance;
	}

	/**
	 * Hilfsmethode, die formal nicht zur Systemkalenderbibliothek gehört, aber für
	 * deren Test notwendig ist. Hierbei werden die Einträge vom DaV gelesen und
	 * ausgewertet.
	 * 
	 * @return Liste der Einträge als HashMap
	 */
	public Map<String, AlterSystemkalenderEintrag> starteSystemKalenderArbeiter() {
		if (used) {
			used = false;

			try {
				inInit = true;

				datenmodell = connection.getDataModel();

				empfaengeroptionen = ReceiveOptions.normal();

				empfaengerrolle = ReceiverRole.receiver();

				// Hole SystemKalenderEintraege
				String objectAtg = "atg.systemKalenderEintrag";
				String objectAsp = "asp.parameterSoll";

				AttributeGroup attributgruppe = connection.getDataModel().getAttributeGroup(objectAtg);
				Aspect aspekt = connection.getDataModel().getAspect(objectAsp);
				simulationsvariante = 0;
				datenbeschreibung = new DataDescription(attributgruppe, aspekt, simulationsvariante);

				configObj = (ConfigurationObject) datenmodell.getObject(kalender);

				BenachrichtigeFunktion.getBenachrichtigeListenerVerwaltung().addBenachrichtigeListener(this);

				MutableSet ms = configObj.getMutableSet("SystemKalenderEinträge");

				if (ms != null) {
					LOGGER.fine("Listener Menge SystemKalenderEinträge angemeldet");

					ms.addChangeListener(this);
				} else
					LOGGER.error("Menge ist null");

				List<SystemObject> objSke = readSystemKalenderEintragMenge();

				cntSke = 0;

				connection.subscribeReceiver(this, objSke, datenbeschreibung, empfaengeroptionen, empfaengerrolle);

				synchronized (this) {

					while ((cntSke < objSke.size())) {
						this.wait();
					}
				}

				parseArbeiter();

				inInit = false;

			} catch (Exception e) {
				e.getStackTrace();
			}

		}

		return skeList;

	}

	private List<SystemObject> readSystemKalenderEintragMenge() throws Exception {
		ConfigurationObject kalender = (ConfigurationObject) connection.getDataModel().getObject(this.kalender);

		ObjectSet objekte = kalender.getObjectSet("SystemKalenderEinträge");

		List<SystemObject> listSke = objekte.getElements();

		return listSke;
	}


	@Override
	public void update(ResultData[] results) {
		// TODO Auto-generated method stub
		for (ResultData data : results) {

			// _debug.config("update: " + data.getObject().getPid() + " : " +
			// data.getData());

			if (data.getDataState() == DataState.NO_SOURCE)
				synchronized (this) {
					notify();
				}

			if (data.getObject().isOfType("typ.systemKalenderEintrag")) {

				if (data.getData() != null) {

					String s = data.getData().getItem("Definition").valueToString();

					int pos = s.indexOf(":=");

					String[] str = new String[2];
					if (pos != -1) {

						str[0] = s.substring(0, pos);
						str[1] = s.substring(pos + 2);

					} else {
						str[0] = s;
						str[1] = s;

					}

					parseList.put(data.getObject().getPid(), str);

					cntSke++;

					if (!inInit) {

						parseArbeiter();

					}

					synchronized (this) {
						notify();
					}

				} else {
					cntSke++;
					// _debug.config("Data ist null");
				}

			} else
				LOGGER.fine(data.getObject().getType().toString() + " ist nicht versorgt!");
		}

	}

	@Override
	public void update(MutableSet set, SystemObject[] addedObjects, SystemObject[] removedObjects) {
		// TODO Auto-generated method stub
		try {
			// _debug.config("update Menge: " + set.getName());

			if (addedObjects != null) {
				List<SystemObject> list = new ArrayList<>();

				for (int i = 0; i < addedObjects.length; i++) {

					SystemObject so = addedObjects[i];

					// _debug.config(so.getPid());

					LOGGER.fine("added - " + addedObjects[i]);

					list.add(so);

				}
				connection.subscribeReceiver(this, list, datenbeschreibung, empfaengeroptionen, empfaengerrolle);
			}

			if (removedObjects != null) {
				for (int i = 0; i < removedObjects.length; i++) {
					SystemObject so = removedObjects[i];

					// _debug.config(so.getPid());

					if (getSkeList().containsKey(so.getPid())) {

						getSkeList().remove(so.getPid());
						// _debug.config(so.getPid() + " : Objekt geloescht!");

					} else {

						// _debug.config(so.getPid() + " : Objekt nicht verhanden!");

					}

					LOGGER.fine("removed - " + removedObjects[i]);

				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void dataRequest(SystemObject object, DataDescription dataDescription, byte state) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRequestSupported(SystemObject object, DataDescription dataDescription) {
		// TODO Auto-generated method stub
		return false;
	}

	public static Debug getDebug() {
		return LOGGER;
	}

	/**
	 * Holt die liste der Systemkalender Einträge
	 * 
	 * @return Liste der Einträge
	 */
	public Map<String, AlterSystemkalenderEintrag> getSkeList() {
		return skeList;
	}

	/**
	 * Parst einen Systemkalendereintrag durch Benutzung der gleichgnamigen Methode
	 * der Klasse Parser
	 * 
	 * @param pid
	 *            die Pid des Eintrags
	 * @param name
	 *            der name
	 * @param definiton
	 *            die Definition
	 * 
	 * @return true, wenn der Eintrag geparst werden konnten
	 */
	public Boolean parseSystemkalenderEintrag(String pid, String name, String definiton) {

		Boolean gueltig = null;

		Parser parser = new Parser(this);

		try {
			String s = null;

			int pos = definiton.indexOf(":=");

			if (pos != -1) {
				s = definiton.substring(pos + 2);
			}

			if (s != null) {
				gueltig = parser.parseSystemkalenderEintrag(pid, name, s);
			} else {
				gueltig = parser.parseSystemkalenderEintrag(pid, name, definiton);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gueltig = false;
		}

		return gueltig;
	}

	public SortedMap<String, Boolean> berechneGueltigJetzt(Long jetzt) {
		// Eintragen der Ereignisse und Berechnen der Anzahl
		SortedMap<String, Boolean> ergebnis = new TreeMap<>();

		Date d = new Date();
		d.setTime(jetzt);

		for (Map.Entry<String, AlterSystemkalenderEintrag> me : getSkeList().entrySet()) {
			AlterSystemkalenderEintrag ske = me.getValue();

			if (ske.isGueltig(jetzt)) {
				ergebnis.put(jetzt + "_" + ske.getPid(), true);
			} else {
				ergebnis.put(jetzt + "_" + ske.getPid(), false);
			}
		}

		return ergebnis;

	}

	public Map.Entry<String, Boolean> berechneGueltigJetzt(String pid, Long jetzt) {

		SortedMap<String, Boolean> ergebnis = new TreeMap<>();

		Date d = new Date();
		d.setTime(jetzt);

		if (getSkeList().containsKey(pid)) {
			AlterSystemkalenderEintrag ske = getSkeList().get(pid);

			if (ske.isGueltig(jetzt)) {
				ergebnis.put(jetzt + "_" + ske.getPid(), true);
			} else {
				ergebnis.put(jetzt + "_" + ske.getPid(), false);
			}

		} else
			return null;

		Map.Entry<String, Boolean> mapEntry = null;

		for (Map.Entry<String, Boolean> me : ergebnis.entrySet()) {
			mapEntry = me;
			break;
		}

		return mapEntry;

	}

	@Override
	public void update(BenachrichtigeEvent e) {
	}

	private void parseArbeiter() {

		try {
			// Parsen!!!
			cntParse = 0;

			int cntRekursiv = 0;

			while (parseList.size() > 0) {

				if (cntRekursiv > 20) {
					getDebug().fine("Maximale Rekursionstiefe erreicht -> Abbruch des Parsens!!!");
					break;
				}

				List<String> delList = new ArrayList<>();

				Parser parser = new Parser(this);

				for (Map.Entry<String, String[]> me : parseList.entrySet()) {
					if (parser.parseSystemkalenderEintrag(me.getKey(), me.getValue()[0], me.getValue()[1])) {
						delList.add(me.getKey());
					} else {
						getDebug().fine(me.getKey() + " konnte nicht geparst werden");
					}

					cntParse++;

					synchronized (this) {
						notify();
					}

				}

				for (String s : delList) {

					parseList.remove(s);

				}

				cntRekursiv++;

			}

			if (inInit) {

				synchronized (this) {

					while ((cntParse < skeList.size())) {
						this.wait();
					}
				}
				LOGGER.fine("Alle SystemKalenderEintraege geparst!");
			} else
				LOGGER.fine("SystemKalenderEintrag geparst!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public SortedMap<String, Boolean> berechneGueltigVonBis(Long von, Long bis) {

		SortedMap<String, Boolean> ergebnis = new TreeMap<>();

		for (Map.Entry<String, AlterSystemkalenderEintrag> me : getSkeList().entrySet()) {
			AlterSystemkalenderEintrag ske = me.getValue();
			SortedMap<Long, Boolean> tmp = ske.berecheneZustandsWechselVonBis(von, bis);
			if (tmp == null)
				return null;

			for (Map.Entry<Long, Boolean> m : tmp.entrySet()) {
				String s = m.getKey() + "_" + ske.getPid();
				ergebnis.put(s, m.getValue());
			}
		}

		return ergebnis;

	}

	public SortedMap<String, Long> berechneIntervallVonBis(Long von, Long bis) {

		SortedMap<String, Long> ergebnis = new TreeMap<>();

		for (Map.Entry<String, AlterSystemkalenderEintrag> me : getSkeList().entrySet()) {
			AlterSystemkalenderEintrag ske = me.getValue();
			SortedMap<Long, Long> tmp = ske.berecheneIntervallVonBis(von, bis);
			if (tmp == null)
				return null;

			for (Map.Entry<Long, Long> m : tmp.entrySet()) {
				String s = m.getKey() + "_" + ske.getPid();
				ergebnis.put(s, m.getValue());
			}
		}

		return ergebnis;

	}

	public SortedMap<String, Boolean> berechneGueltigVonBis(List<SystemObject> list, Long von, Long bis) {

		// Sortierte Map mit den zu sendenden Daten wird erstellt
		SortedMap<String, Boolean> ergebnis = new TreeMap<>();

		// Eintragen der Ereignisse und Berechnen der Anzahl
		for (SystemObject so : list) {
			AlterSystemkalenderEintrag ske = getSkeList().get(so.getPid());

			if (ske != null) {

				SortedMap<Long, Boolean> tmp = ske.berecheneZustandsWechselVonBis(von, bis);

				if (tmp == null)
					return null;

				for (Map.Entry<Long, Boolean> m : tmp.entrySet()) {
					String s = m.getKey() + "_" + ske.getPid();
					ergebnis.put(s, m.getValue());
				}

			} else {
				LOGGER.error(so.getPid() + " ist nicht der Liste der Systemkalendereinträge");
			}
		}

		return ergebnis;
	}

	public SortedMap<String, Long> berechneIntervallVonBis(List<SystemObject> list, Long von, Long bis) {

		// Sortierte Map mit den zu sendenden Daten wird erstellt
		SortedMap<String, Long> ergebnis = new TreeMap<>();

		// Eintragen der Ereignisse und Berechnen der Anzahl
		for (SystemObject so : list) {
			AlterSystemkalenderEintrag ske = getSkeList().get(so.getPid());

			if (ske != null) {

				SortedMap<Long, Long> tmp = ske.berecheneIntervallVonBis(von, bis);

				if (tmp == null)
					return null;

				for (Map.Entry<Long, Long> m : tmp.entrySet()) {
					String s = m.getKey() + "_" + ske.getPid();
					ergebnis.put(s, m.getValue());
				}

			} else {
				LOGGER.error(so.getPid() + " ist nicht der Liste der Systemkalendereinträge");
			}
		}

		return ergebnis;

	}

	public SortedMap<String, Boolean> berechneGueltigVonBis(String pid, Long von, Long bis) {
		pid = pid.toLowerCase();

		// Sortierte Map mit den zu sendenden Daten wird erstellt
		SortedMap<String, Boolean> ergebnis = new TreeMap<>();

		if (getSkeList().containsKey(pid)) {

			AlterSystemkalenderEintrag ske = getSkeList().get(pid);

			SortedMap<Long, Boolean> tmp = ske.berecheneZustandsWechselVonBis(von, bis);

			if (tmp == null)
				return null;

			for (Map.Entry<Long, Boolean> m : tmp.entrySet()) {
				String s = m.getKey() + "_" + ske.getPid();
				ergebnis.put(s, m.getValue());
			}

		} else {
			LOGGER.error(pid + " ist nicht der Liste der Systemkalendereinträge");
		}

		return ergebnis;
	}

	public SortedMap<String, Long> berechneIntervallVonBis(String pid, Long von, Long bis) {
		pid = pid.toLowerCase();

		// Sortierte Map mit den zu sendenden Daten wird erstellt
		SortedMap<String, Long> ergebnis = new TreeMap<>();

		if (getSkeList().containsKey(pid)) {

			AlterSystemkalenderEintrag ske = getSkeList().get(pid);

			SortedMap<Long, Long> tmp = ske.berecheneIntervallVonBis(von, bis);

			if (tmp == null)
				return null;

			for (Map.Entry<Long, Long> m : tmp.entrySet()) {
				String s = m.getKey() + "_" + ske.getPid();
				ergebnis.put(s, m.getValue());
			}

		} else {
			LOGGER.error(pid + " ist nicht der Liste der Systemkalendereinträge");
		}

		return ergebnis;
	}

}

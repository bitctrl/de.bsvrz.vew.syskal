/*
 * SWE Systemkalender - Version 2
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
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
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49-341-49067-0
 * Fax: +49-341-49067-15
 * mailto: info@bitctrl.de
 */

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class TestSyskalOnline2IT implements StandardApplication {
	/**
	 * DebugLogger für Debug-Ausgaben
	 */
	private static Debug logger;

	/**
	 * Konfigurationsobjekt, hier das Systemkalenderobjekt
	 */
	private String strKonfigObjekt;

	/**
	 * Konstruktor.<br>
	 */
	public TestSyskalOnline2IT() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Überschriebene Methode von StandardApplication, die die speziellen
	 * Startparameter auswertet.<br>
	 * Die Liste der Konfigurationsbereiche wird durch Aufspaltung des übergebenen
	 * Strings erstellt und die speziellen Startparameter werden in die Log-Datei
	 * eingetragen.
	 * 
	 * @param argumentList
	 *            siehe sys.funclib.application.StandardApplication#parseArguments
	 *            (sys.funclib.ArgumentList)
	 */
	@Override
	public void parseArguments(ArgumentList argumentList) throws Exception {
		logger = Debug.getLogger();

		logger.config("argumentList = " + argumentList);

		strKonfigObjekt = argumentList.fetchArgument("-konfigurationsobjekt=").asNonEmptyString();

		logger.config("KonfigurationsObjekt = '" + strKonfigObjekt + "'");

		argumentList.fetchUnusedArguments();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sys.funclib.application.StandardApplication#initialize(stauma.dav.clientside
	 * .ClientDavInterface)
	 */
	/**
	 * Überschriebene Methode von StandardApplication, die die Initialisierung
	 * durchführt.<br>
	 * Entsprechend dem Argument -layer wird die entsprechende Methode aufgerufen
	 * und danach die Log-Datei geschlossen.<br>
	 * 
	 * @param connection
	 *            siehe
	 *            sys.funclib.application.StandardApplication#initialize(stauma
	 *            .dav.clientside.ClientDavInterface)
	 */
	@Override
	public void initialize(ClientDavInterface connection) throws Exception {

		SystemKalender systemKalender = new SystemKalender(connection,
				(ConfigurationObject) connection.getDataModel().getObject(strKonfigObjekt));

		Collection<SystemkalenderEintrag> eintraege = systemKalender.getEintraege();
		System.out.println("TestSyskalOnline2.initialize(LIST) " + eintraege.size());
		for (SystemkalenderEintrag eintrag : eintraege) {
			System.out.println("-----------> " + eintrag);
		}

		SystemkalenderEintrag eintrag = systemKalender
				.getEintrag(connection.getDataModel().getObject("ske.nw.vrz-nrw.es.sts.tagvoreinemfeiertag"));
		LocalDateTime startTime = LocalDateTime.of(2016, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2016, 12, 31, 0, 0, 0);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = systemKalender
				.getEintrag(connection.getDataModel().getObject("ske.nw.vrz-nrw.es.sts.feiertag"));
		zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage2: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);
	}

	/**
	 * Programmeinstieg.<br>
	 * 
	 * @param arguments
	 *            Kommandozeilenargumente
	 */
	public static void main(String[] arguments) {
		StandardApplicationRunner.run(new TestSyskalOnline2IT(), arguments);

	}
}

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Kommentar
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
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
		ZustandsWechsel zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
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

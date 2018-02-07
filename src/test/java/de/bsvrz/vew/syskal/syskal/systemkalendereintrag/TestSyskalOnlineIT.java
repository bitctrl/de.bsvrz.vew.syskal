package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.time.LocalDateTime;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
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
public class TestSyskalOnlineIT implements StandardApplication {
	/**
	 * DebugLogger für Debug-Ausgaben
	 */
	private static Debug logger;

	/**
	 * Konfigurationsibjekt, hier das Systemalenderobjekt
	 */
	private String pidKonfigObjekt;


	/**
	 * Konstruktor.<br>
	 */
	public TestSyskalOnlineIT() {
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

		pidKonfigObjekt = argumentList.fetchArgument("-konfigurationsobjekt=").asNonEmptyString();

		logger.config("KonfigurationsObjekt = '" + pidKonfigObjekt + "'");

		argumentList.fetchUnusedArguments();
	}

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

		SystemObjectType type = connection.getDataModel().getType("typ.sonderprogrammDefinition");

		if (type != null) {
			List<SystemObject> elements = type.getElements();
			System.out.println(elements);

		} else
			System.out.println("type ist null!");

		SystemKalender systemKalender = new SystemKalender(connection,
				(ConfigurationObject) connection.getDataModel().getObject(pidKonfigObjekt));
		systemKalender.getEintraege();

		SystemkalenderEintrag eintrag = systemKalender
				.getEintrag(connection.getDataModel().getObject("ske.montag_berufsverkehr"));
		LocalDateTime startTime = LocalDateTime.of(2009, 8, 1, 10, 40, 35);
		LocalDateTime endTime = LocalDateTime.of(2009, 9, 18, 10, 40, 35);
		List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage1: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = systemKalender.getEintrag(connection.getDataModel().getObject("ske.geburtstag_hck"));
		startTime = LocalDateTime.of(1970, 9, 25, 14, 59, 00);
		endTime = LocalDateTime.of(1975, 9, 25, 14, 59, 9);
		zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage2: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		startTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
		endTime = LocalDateTime.of(1970, 12, 31, 23, 59, 59);
		zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage3: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);


		eintrag = systemKalender.getEintrag(connection.getDataModel().getObject("ske.dienstagalsverknüpfung"));
		startTime = LocalDateTime.of(2009, 01, 01, 15, 15, 37);
		endTime = LocalDateTime.of(2009, 12, 21, 15, 15, 37);
		zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage4: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

		eintrag = systemKalender.getEintrag(connection.getDataModel().getObject("ske.geburtstag_hck_kopie"));
		startTime = LocalDateTime.of(2009, 01, 01, 15, 15, 37);
		endTime = LocalDateTime.of(2009, 12, 21, 15, 15, 37);
		zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
		System.out
				.println("Abfrage5: " + eintrag.getName() + " " + startTime + " - " + endTime + ": " + zustandsWechsel);

	}

	/**
	 * Programmeinstieg.<br>
	 * 
	 * @param arguments
	 *            Kommandozeilenargumente
	 */
	public static void main(String[] arguments) {
		StandardApplicationRunner.run(new TestSyskalOnlineIT(), arguments);

	}

}

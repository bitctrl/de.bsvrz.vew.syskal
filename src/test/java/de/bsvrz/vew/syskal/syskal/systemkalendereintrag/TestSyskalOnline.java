package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Kommentar
 * 
 * @version $Revision: 1.1 $ / $Date: 2009/10/26 12:13:14 $ / ($Author: Pittner $)
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class TestSyskalOnline implements StandardApplication
{
  /**
   * DebugLogger für Debug-Ausgaben
   */
  private static Debug _debug;

  /**
   * Konfigurationsibjekt, hier das Systemalenderobjekt
   */
  private String _strKonfigObjekt;

  /**
   * Das Format der Ergebnisausgabe
   */
  private static SimpleDateFormat _sdf;

  /**
   * Konstruktor.<br>
   */
  public TestSyskalOnline()
  {
    try
    {
      Thread.sleep(100);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see sys.funclib.application.StandardApplication#parseArguments(sys.funclib .ArgumentList)
   */
  /**
   * Überschriebene Methode von StandardApplication, die die speziellen Startparameter auswertet.<br>
   * Die Liste der Konfigurationsbereiche wird durch Aufspaltung des übergebenen Strings erstellt und die speziellen
   * Startparameter werden in die Log-Datei eingetragen.
   * 
   * @param argumentList
   *          siehe sys.funclib.application.StandardApplication#parseArguments (sys.funclib.ArgumentList)
   */
  public void parseArguments(ArgumentList argumentList) throws Exception
  {
    _debug = Debug.getLogger();

    _debug.config("argumentList = " + argumentList);

    _strKonfigObjekt = argumentList.fetchArgument("-konfigurationsobjekt=").asNonEmptyString();

    _debug.config("KonfigurationsObjekt = '" + _strKonfigObjekt + "'");

    argumentList.fetchUnusedArguments();
  }

  /*
   * (non-Javadoc)
   * 
   * @see sys.funclib.application.StandardApplication#initialize(stauma.dav.clientside .ClientDavInterface)
   */
  /**
   * Überschriebene Methode von StandardApplication, die die Initialisierung durchführt.<br>
   * Entsprechend dem Argument -layer wird die entsprechende Methode aufgerufen und danach die Log-Datei geschlossen.<br>
   * 
   * @param connection
   *          siehe sys.funclib.application.StandardApplication#initialize(stauma .dav.clientside.ClientDavInterface)
   */
  public void initialize(ClientDavInterface connection) throws Exception
  {
    
    SystemObjectType type = connection.getDataModel().getType("typ.sonderprogrammDefinition");
    
    if (type != null){
      List<SystemObject> elements = type.getElements();
      System.out.println(elements);
      
    }else
      System.out.println("type ist null!");
    
    
    Map<String, SystemkalenderEintrag> map = SystemkalenderArbeiter.getInstance(connection, _strKonfigObjekt).starteSystemKalenderArbeiter();
    
    _sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

    SystemkalenderEintrag ske1 = map.get("ske.montag_berufsverkehr");

    Date d1 = _sdf.parse("01.08.2009 10:40:35,000");
    Date d2 = _sdf.parse("18.09.2009 10:40:35,000");

    System.out.println("Abfrage1: " + ske1.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

    erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp3(ske1, 2009);

    SystemkalenderEintrag ske2 = map.get("ske.geburtstag_hck");

    d1 = _sdf.parse("25.09.1970 14:59:09,000");
    d2 = _sdf.parse("25.09.1975 14:59:09,000");

    System.out.println("Abfrage2: " + ske2.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

    erstelleAbfrageUndAusgabeErgebnisTyp1(ske2, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp2(ske2, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp3(ske2, 1970);

    d1 = _sdf.parse("01.01.1970 00:00:00,000");
    d2 = _sdf.parse("31.12.1970 23:59:59,000");

    System.out.println("Abfrage3: " + ske2.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

    erstelleAbfrageUndAusgabeErgebnisTyp1(ske2, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp2(ske2, d1, d2);

    SystemkalenderEintrag ske3 = map.get("ske.dienstagalsverknüpfung");

    d1 = _sdf.parse("01.01.2009 15:15:37,000");
    d2 = _sdf.parse("21.12.2009 15:15:37,000");

    System.out.println("Abfrage4: " + ske3.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

    erstelleAbfrageUndAusgabeErgebnisTyp1(ske3, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp2(ske3, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp3(ske3, 1970);

    SystemkalenderEintrag ske4 = map.get("ske.geburtstag_hck_kopie");

    d1 = _sdf.parse("01.01.2009 15:15:37,000");
    d2 = _sdf.parse("21.12.2009 15:15:37,000");

    System.out.println("Abfrage5: " + ske4.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

    erstelleAbfrageUndAusgabeErgebnisTyp1(ske4, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp2(ske4, d1, d2);
    erstelleAbfrageUndAusgabeErgebnisTyp3(ske4, 2009);

  }// public void initialize(ClientDavInterface connection)

  /**
   * Programmeinstieg.<br>
   * 
   * @param arguments
   *          Kommandozeilenargumente
   */
  public static void main(String[] arguments)
  {
    StandardApplicationRunner.run(new TestSyskalOnline(), arguments);

  }

  /**
   * Erstellt eine Abfrage der manuellen Zeitbereiche durch Benutzung der <br>
   * vom Systemkalender bereitgestellten Methode <br>
   * {@link SystemkalenderEintrag#berecheneZustandsWechselVonBis(Long, Long)} <br> 
   * Diese Methode liefert das Ergebnis in der Form: <br>
   * {@link SortedMap} mit dem Wertepaar <{@link Long}, {@link Boolean}> 
   * @param ske
   *            der Systemkalendereintrag
   * @param von
   *          Anfangsdatum
   * @param bis
   *          Enddatum
   */
  private static void erstelleAbfrageUndAusgabeErgebnisTyp1(SystemkalenderEintrag ske, Date von, Date bis)
  {
    SortedMap<Long, Boolean> sm = ske.berecheneZustandsWechselVonBis(von.getTime(), bis.getTime());

    if (sm != null)
    {
      Date d = new Date();
      for (Map.Entry<Long, Boolean> me : sm.entrySet())
      {
        d.setTime(me.getKey());
        System.out.println("Ergebnistyp 1: " + _sdf.format(d) + " " + me.getValue());

      }
    }
    else
      System.out.println("Abfrage liefert kein Ergebnis!");
  }

  /**
   * Erstellt eine Abfrage der manuellen Zeitbereiche durch Benutzung der <br>
   * vom Systemkalender bereitgestellten Methode <br>
   * {@link SystemkalenderEintrag#berecheneIntervallVonBis(Long, Long)} <br> 
   * Diese Methode liefert das Ergebnis in der Form: <br>
   * {@link SortedMap} mit dem Wertepaar <{@link Long}, {@link Long}>
   * @param ske
   *            der Systemkalendereintrag
   * @param von
   *          Anfangsdatum
   * @param bis
   *          Enddatum
   */
  private static void erstelleAbfrageUndAusgabeErgebnisTyp2(SystemkalenderEintrag ske, Date von, Date bis)
  {
    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(von.getTime(), bis.getTime());

    if (sm != null)
    {
      Date d1 = new Date();
      Date d2 = new Date();
      for (Map.Entry<Long, Long> me : sm.entrySet())
      {
        d1.setTime(me.getKey());
        d2.setTime(me.getValue());
        System.out.println("Ergebnistyp 2: " + _sdf.format(d1) + " " + _sdf.format(d2));

      }
    }
    else
      System.out.println("Abfrage liefert kein Ergebnis!");
  }
  
  /**
   * Erstellt eine Abfrage der Zeitbereiche für das Jahr des Anfangszeitpunktes <br>
   * durch Benutzung der vom Systemkalender bereitgestellten Methode <br>
   * {@link SystemkalenderEintrag#berechneZustandsWechsel(int)} <br> 
   * Diese Methode liefert das Ergebnis in der Form: <br>
   * {@link SortedMap} mit dem Wertepaar <{@link Long}, {@link Boolean}>
   * @param ske
   *            der Systemkalendereintrag
   * @param von
   *          Anfangsdatum
   * @param bis
   *          Enddatum
   */
  private static void erstelleAbfrageUndAusgabeErgebnisTyp3(SystemkalenderEintrag ske, int jahr)
  {
    SortedMap<Long, Boolean> sm = ske.berechneZustandsWechsel(jahr);

    if (sm != null)
    {
      Date d = new Date();
      for (Map.Entry<Long, Boolean> me : sm.entrySet())
      {
        d.setTime(me.getKey());
        System.out.println("Ergebnistyp 3: " + _sdf.format(d) + " " + me.getValue());

      }
    }
    else
      System.out.println("Abfrage liefert kein Ergebnis!");
  }

}

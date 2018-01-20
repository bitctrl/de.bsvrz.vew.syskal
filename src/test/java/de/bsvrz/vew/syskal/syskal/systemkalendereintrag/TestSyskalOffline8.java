package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

public class TestSyskalOffline8
{
  /**
   * Das Format der Ergebnisausgabe
   */
  private static DateFormat _sdf;

  public static void main(String[] args)
  {   
    try
    {         
      
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.montag", "Montag", "Montag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.dienstag", "Dienstag", "Dienstag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.mittwoch", "Mittwoch", "Mittwoch");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.donnerstag", "Donnerstag", "Donnerstag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.freitag", "Freitag", "Freitag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.samstag", "Samstag", "Samstag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.sonntag", "Sonntag", "Sonntag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ostersonntag", "Ostersonntag", "Ostersonntag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tag", "Tag", "Tag");
     
      
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ferien1", "Ferien1", "Ferien1:=<30.07.2015-12.09.2015>");
//      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ende1", "Ende1", "Ende1:=12.09.2015,2015");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ferien2", "Ferien2", "Ferien2:=<02.11.2015-06.11.2015>");
//      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ende2", "Ende2", "Ende2:=06.11.2015,2015");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ferien", "Ferien", "Ferien:=ODER{Ferien1,Ferien2}*,*");
      
//      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ferien11", "Ferien11", "Ferien11:=Ferien1+1Tag");
//      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ferien21", "Ferien21", "Ferien21:=Ferien2+1Tag");
      
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ferienplus", "FerienPlus", "FerienPlus:=Ferien+1Tag");
//      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ende11", "Ende11", "Ende11:=Ende1+1Tag");
//      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ende21", "Ende21", "Ende21:=Ende2+1Tag");
      
      
      _sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

      SystemkalenderEintrag ske1 = SystemkalenderArbeiter.getSkeList().get("ske.ferienplus");
//      SystemkalenderEintrag ske1 = SystemkalenderArbeiter.getSkeList().get("ske.ferien");

      Date d1 = _sdf.parse("01.01.2015 00:00:00,000");
      Date d2 = _sdf.parse("31.12.2015 23:59:59,999");

      System.out.println("Abfrage: " + ske1.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

      erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
//      erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
      //erstelleAbfrageUndAusgabeErgebnisTyp3(ske1, 2015);

    }
    catch (Exception e)
    {
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
   * @param jahr
   *          das Jahr
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

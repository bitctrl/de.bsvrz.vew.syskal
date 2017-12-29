package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

public class TestSyskalOffline4
{
  /**
   * Das Format der Ergebnisausgabe
   */
  private static DateFormat _sdf;

  public static void main(String[] args)
  {   
    try
    {               
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tag", "Tag", "Tag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ostersonntag", "Ostersonntag", "Ostersonntag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test1", "Test1", "Test1:=29.02.2001,2010");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test2", "Test2", "Test2:=29.02.2004,2010");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test3", "Test3", "Test3:=({13:00:00,000-15:00:00,000}{14:00:00,000-16:00:00,000})*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test31", "Test31", "Test31:=({13:00:00,000-14:00:00,000}{14:00:00,000-15:00:00,000})*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test32", "Test32", "Test32:=({13:00:00,000-14:00:00,000}{15:00:00,000-16:00:00,000})");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test4", "Test4", "Test4:=Ostersonntag+1Tag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test5", "Test5", "Test5:=Ostersonntag-4Tage");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ostermontag", "Ostermontag", "Ostermontag:=Ostersonntag+1Tag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.karfreitag", "Karfreitag", "Karfreitag:=Ostermontag-3Tage");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test6", "Test6", "Test6:=UND{Tag,NICHT Ostersonntag}*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.test7", "Test7", "Test7:=UND{Test32,Tag}*,*");
      
      _sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

      SystemkalenderEintrag ske1 = SystemkalenderArbeiter.getSkeList().get("ske.test1");

      Date d1 = _sdf.parse("01.01.2000 00:00:00,000");
      Date d2 = _sdf.parse("31.12.2010 23:59:59,999");

      System.out.println();
      System.out.println("Abfrage1: " + ske1.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

      erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);

      SystemkalenderEintrag ske2 = SystemkalenderArbeiter.getSkeList().get("ske.test2");
      
      d1 = _sdf.parse("01.01.2004 00:00:00,000");
      d2 = _sdf.parse("31.12.2010 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage2: " + ske2.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske2, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske2, d1, d2);
      
      SystemkalenderEintrag ske3 = SystemkalenderArbeiter.getSkeList().get("ske.test3");
      
      d1 = _sdf.parse("10.03.2004 13:30:00,000");
      d2 = _sdf.parse("10.03.2004 15:29:59,999");
      
      System.out.println();
      System.out.println("Abfrage3: " + ske3.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske3, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske3, d1, d2);
      
      SystemkalenderEintrag ske31 = SystemkalenderArbeiter.getSkeList().get("ske.test31");
      
      d1 = _sdf.parse("10.03.2004 00:00:00,000");
      d2 = _sdf.parse("11.03.2004 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage31: " + ske3.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske31, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske31, d1, d2);
      
      SystemkalenderEintrag ske = SystemkalenderArbeiter.getSkeList().get("ske.ostersonntag");
      
      d1 = _sdf.parse("01.01.2000 00:00:00,000");
      d2 = _sdf.parse("31.12.2010 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage4: " + ske.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske, d1, d2);
      
      SystemkalenderEintrag ske4 = SystemkalenderArbeiter.getSkeList().get("ske.test4");
      
      d1 = _sdf.parse("01.01.2000 00:00:00,000");
      d2 = _sdf.parse("31.12.2003 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage5: " + ske4.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske4, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske4, d1, d2);
      
      SystemkalenderEintrag ske5 = SystemkalenderArbeiter.getSkeList().get("ske.test5");
      
      d1 = _sdf.parse("01.01.2000 00:00:00,000");
      d2 = _sdf.parse("31.12.2003 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage6: " + ske5.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske5, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske5, d1, d2);
      
      SystemkalenderEintrag skk = SystemkalenderArbeiter.getSkeList().get("ske.karfreitag");
      
      d1 = _sdf.parse("01.01.2000 00:00:00,000");
      d2 = _sdf.parse("31.12.2003 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage7: " + skk.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(skk, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(skk, d1, d2);
      
      SystemkalenderEintrag ske6 = SystemkalenderArbeiter.getSkeList().get("ske.test6");
      
      d1 = _sdf.parse("01.04.2001 00:00:00,000");
      d2 = _sdf.parse("01.05.2001 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage8: " + ske6.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske6, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske6, d1, d2);
      
      SystemkalenderEintrag ske7 = SystemkalenderArbeiter.getSkeList().get("ske.test7");
      
      d1 = _sdf.parse("19.09.2011 00:00:00,000");
      d2 = _sdf.parse("23.09.2011 23:59:59,999");
      
      System.out.println();
      System.out.println("Abfrage7: " + ske7.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));
      
      erstelleAbfrageUndAusgabeErgebnisTyp1(ske7, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske7, d1, d2);

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

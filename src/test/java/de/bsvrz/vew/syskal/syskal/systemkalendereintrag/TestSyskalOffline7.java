package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

public class TestSyskalOffline7
{
  /**
   * Das Format der Ergebnisausgabe
   */
  private static DateFormat _sdf;

  /**
   * @param args
   */
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
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.neujahr", "Neujahr", "Neujahr:=01.01.*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.karfreitag", "Karfreitag", "Karfreitag:=Ostersonntag-2Tage");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tdde", "Einheit", "Einheit:=03.10.*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tda", "Arbeit", "Arbeit:=01.05.*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.allerheil", "Allerheil", "Allerheil:=01.11.*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.weihnacht1", "Weihnacht1", "Weihnacht1:=25.12.*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.weihnacht2", "Weihnacht2", "Weihnacht2:=26.12.*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ostermontag", "Ostermontag", "Ostermontag:=Ostersonntag+1Tag");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.himmel", "Himmel", "Himmel:=Ostersonntag+39Tage");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.pfingstmo", "PfingstMo", "PfingstMo:=Ostersonntag+50Tage");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.fronleich", "Fronleich", "Fronleich:=Ostersonntag+60Tage");
      
      
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.feiertag", "Feiertag", "Feiertag:=ODER{Neujahr,Karfreitag,Ostersonntag,Ostermontag,Arbeit,Himmel,PfingstMo,Fronleich,Einheit,Allerheil,Weihnacht1,Weihnacht2}*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.mobissa", "MoBisSa", "MoBisSa:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag}*,*");
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.mobisfr", "MoBisFr", "MoBisFr:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag}*,*");
      
      SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.werktag", "Werktag", "Werktag:=UND{MoBisFr,NICHT Feiertag}*,*");
      _sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

      SystemkalenderEintrag ske1 = SystemkalenderArbeiter.getSkeList().get("ske.werktag");

      Date d1 = _sdf.parse("01.08.2014 14:00:00,000");
      Date d2 = _sdf.parse("31.08.2014 00:00:00,000");

      System.out.println("Abfrage: " + ske1.getPid() + " " + _sdf.format(d1) + " - " + _sdf.format(d2));

//      erstelleAbfrageUndAusgabeErgebnisTyp1(ske1, d1, d2);
      erstelleAbfrageUndAusgabeErgebnisTyp2(ske1, d1, d2);
//      erstelleAbfrageUndAusgabeErgebnisTyp3(ske1, 2015);

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

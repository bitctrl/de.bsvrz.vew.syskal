package de.bsvrz.vew.syskal.syskal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavConnection;
import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientDavParameters;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.authentication.ClientCredentials;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.SystemObject;
//import de.bsvrz.sys.funclib.dambach.vewdynobj.VerwaltungDynObj;
import de.bsvrz.sys.funclib.dambach.vewdynobj.VerwaltungDynObj;
import de.bsvrz.vew.syskal.syskal.systemkalendereintrag.SystemkalenderArbeiter;

public class SystemTestIT
{

  /**
   * Datenverteilerverbindung.
   */
  private static ClientDavInterface connection = null;

//private static String _strKalender = "kv.testKonfiguration";
  private static String pidKalender = "kv.aoe.nw.nba.koeln.koblenz";

  /**
   * Pid Kalender
   */
//  private static String _strKonfigBereich = "kb.default.testKonfiguration";
  private static String pidKonfigBereich = "kb.default.aoe.nw.nba.koeln.koblenz";

  /**
   * Die Instanz des SystenkalenderArbeiters
   */
  private static SystemkalenderArbeiter systemKalenderArbeiter;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    ClientDavParameters parameters = new ClientDavParameters();
    parameters.setDavCommunicationAddress("localhost");
    parameters.setDavCommunicationSubAddress(8083);
    parameters.setUserName("Tester");

    connection = new ClientDavConnection(parameters);
    connection.connect();
    connection.login("Tester", ClientCredentials.ofPassword("geheim".toCharArray()));

	systemKalenderArbeiter = new SystemkalenderArbeiter(connection, null);
    systemKalenderArbeiter.getSkeList().clear();  
    
    erzeugeSystemKalenderEintrag("ske.osternTest", "Ostersonntag", "Ostersonntag");
    erzeugeSystemKalenderEintrag("ske. ", " ", " :=Ostersonntag");
    erzeugeSystemKalenderEintrag("ske.@", "@", "@:=asdf+1Tag");
    erzeugeSystemKalenderEintrag("ske.1", "1", "1:= -1Tag");
    erzeugeSystemKalenderEintrag("ske._", "_", "_:=18.05.*,*");
    erzeugeSystemKalenderEintrag("ske.#", "#", "#:=17.05.2007,*");
    erzeugeSystemKalenderEintrag("ske.^", "^", "^:=17.05.*,1989");
    erzeugeSystemKalenderEintrag("ske.º", "º", "º:=17.05.2004,2004");
    erzeugeSystemKalenderEintrag("ske.\\", "\\", "\\:=<25.05.2004-30.05.2004>");
    erzeugeSystemKalenderEintrag("ske./", "/", "/:=ODER{1,_,#}*,*");
    erzeugeSystemKalenderEintrag("ske.hauptverkehrszeit", "Hauptverkehrszeit",
        "Hauptverkehrszeit:=({07:15:00,000-08:59:59,999}{15:30:00,000-17:44:59,999})*,*");
    
    Calendar cal = Calendar.getInstance();

    int dow = cal.get(Calendar.DAY_OF_WEEK);

    String defGueltig = null;
    String defUngueltig = null;

    switch (dow)
    {
      case 0:
        defGueltig = "Samstag";
        defUngueltig = "Sonntag";
        break;
      case 1:
        defGueltig = "Sonntag";
        defUngueltig = "Montag";
        break;
      case 2:
        defGueltig = "Montag";
        defUngueltig = "Dienstag";
        break;
      case 3:
        defGueltig = "Dienstag";
        defUngueltig = "Mittwoch";
        break;
      case 4:
        defGueltig = "Mittwoch";
        defUngueltig = "Donnerstag";
        break;
      case 5:
        defGueltig = "Donnerstag";
        defUngueltig = "Freitag";
        break;
      case 6:
        defGueltig = "Freitag";
        defUngueltig = "Samstag";
        break;

      default:
        fail("setUpBeforeClass()");

    }

    erzeugeSystemKalenderEintrag("ske.gueltig", "Gueltig", defGueltig);
    erzeugeSystemKalenderEintrag("ske.ungueltig", "Ungueltig", defUngueltig);
    
    systemKalenderArbeiter.starteSystemKalenderArbeiter();

  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    loescheSystemKalenderEintrag("ske.osternTest");
    loescheSystemKalenderEintrag("ske. ");
    loescheSystemKalenderEintrag("ske.@");
    loescheSystemKalenderEintrag("ske.1");
    loescheSystemKalenderEintrag("ske._");
    loescheSystemKalenderEintrag("ske.#");
    loescheSystemKalenderEintrag("ske.^");
    loescheSystemKalenderEintrag("ske.º");
    // loescheSystemKalenderEintrag("ske.\\");
    loescheSystemKalenderEintrag("ske./");
    loescheSystemKalenderEintrag("ske.hauptverkehrszeit");
    loescheSystemKalenderEintrag("ske.gueltig");
    loescheSystemKalenderEintrag("ske.ungueltig");

  }
 
  @Test
  public void systemTest1() throws Exception
  {
	  
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

    Date d1 = sdf.parse("01.01.2004 00:00:00,000");
    Date d2 = sdf.parse("31.12.2004 23:59:59,999");

    List<SystemObject> list = new ArrayList<>();

    list.add(connection.getDataModel().getObject("ske. "));
    list.add(connection.getDataModel().getObject("ske.@"));
    list.add(connection.getDataModel().getObject("ske.1"));
    list.add(connection.getDataModel().getObject("ske._"));
    list.add(connection.getDataModel().getObject("ske.#"));
    list.add(connection.getDataModel().getObject("ske.^"));
    list.add(connection.getDataModel().getObject("ske.º"));
    list.add(connection.getDataModel().getObject("ske.\\"));
    list.add(connection.getDataModel().getObject("ske./"));

    SortedMap<String, Boolean> sm = systemKalenderArbeiter.berechneGueltigVonBis(list, d1.getTime(), d2.getTime());

    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<String, Boolean> me : sm.entrySet())
      {

        String key = me.getKey();
        String[] split = key.split("_");

        Date d = new Date();
        d.setTime(Long.parseLong(split[0]));
        strings[cnt] = split[1] + " : " + sdf.format(d) + " " + me.getValue();

        cnt++;
      }

      assertTrue("Test 1", strings.length == 24);
      assertEquals("Test 2", "ske./ : 10.04.2004 00:00:00,000 true", strings[0]);
      assertEquals("Test 3", "ske.1 : 10.04.2004 00:00:00,000 true", strings[1]);
      assertEquals("Test 4", "ske./ : 10.04.2004 23:59:59,999 false", strings[2]);
      assertEquals("Test 5", "ske.1 : 10.04.2004 23:59:59,999 false", strings[3]);
      assertEquals("Test 6", "ske.  : 11.04.2004 00:00:00,000 true", strings[4]);
      assertEquals("Test 7", "ske.  : 11.04.2004 23:59:59,999 false", strings[5]);
      assertEquals("Test 8", "ske.º : 17.05.2004 00:00:00,000 true", strings[6]);
      assertEquals("Test 9", "ske.º : 17.05.2004 23:59:59,999 false", strings[7]);
      assertEquals("Test 10", "ske./ : 18.05.2004 00:00:00,000 true", strings[8]);
      assertEquals("Test 11", "ske. : 18.05.2004 00:00:00,000 true", strings[9]);
      assertEquals("Test 12", "ske./ : 18.05.2004 23:59:59,999 false", strings[10]);
      assertEquals("Test 13", "ske. : 18.05.2004 23:59:59,999 false", strings[11]);
      assertEquals("Test 14", "ske.\\ : 25.05.2004 00:00:00,000 true", strings[12]);
      assertEquals("Test 15", "ske.\\ : 25.05.2004 23:59:59,999 false", strings[13]);
      assertEquals("Test 16", "ske.\\ : 26.05.2004 00:00:00,000 true", strings[14]);
      assertEquals("Test 17", "ske.\\ : 26.05.2004 23:59:59,999 false", strings[15]);
      assertEquals("Test 18", "ske.\\ : 27.05.2004 00:00:00,000 true", strings[16]);
      assertEquals("Test 19", "ske.\\ : 27.05.2004 23:59:59,999 false", strings[17]);
      assertEquals("Test 20", "ske.\\ : 28.05.2004 00:00:00,000 true", strings[18]);
      assertEquals("Test 21", "ske.\\ : 28.05.2004 23:59:59,999 false", strings[19]);
      assertEquals("Test 22", "ske.\\ : 29.05.2004 00:00:00,000 true", strings[20]);
      assertEquals("Test 23", "ske.\\ : 29.05.2004 23:59:59,999 false", strings[21]);
      assertEquals("Test 24", "ske.\\ : 30.05.2004 00:00:00,000 true", strings[22]);
      assertEquals("Test 25", "ske.\\ : 30.05.2004 23:59:59,999 false", strings[23]);

    }
    else
      fail("Test 1-25");
    
    
    SortedMap<String, Long> sm2 = systemKalenderArbeiter.berechneIntervallVonBis(list, d1.getTime(), d2.getTime());
    
    if (sm2 != null)
    {
      
      String[] strings = new String[sm2.entrySet().size()];
      System.out.println("SystemTest.systemTest1(): " + strings.length);
      
      int cnt = 0;
      for (Map.Entry<String, Long> me : sm2.entrySet())
      {
        
        String key = me.getKey();
        String[] split = key.split("_");
        
        Date d11 = new Date();
        d11.setTime(Long.parseLong(split[0]));
        Date d21 = new Date();
        d21.setTime( me.getValue());
                       
        String var = split[1] + " : " + sdf.format(d11) + " " + sdf.format(d21);
        System.out.println("SystemTest.systemTest1(): " + var);
        strings[cnt] = var;
     
        cnt++;
      }
      
      int loc = 0;      
      assertTrue("Test 26", strings.length == 12);
      
      
      assertEquals("Test 27", "ske./ : 10.04.2004 00:00:00,000 10.04.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 28", "ske.1 : 10.04.2004 00:00:00,000 10.04.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 29", "ske.  : 11.04.2004 00:00:00,000 11.04.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 30", "ske.º : 17.05.2004 00:00:00,000 17.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 31", "ske./ : 18.05.2004 00:00:00,000 18.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 32", "ske. : 18.05.2004 00:00:00,000 18.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 33", "ske.\\ : 25.05.2004 00:00:00,000 25.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 34", "ske.\\ : 26.05.2004 00:00:00,000 26.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 35", "ske.\\ : 27.05.2004 00:00:00,000 27.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 36", "ske.\\ : 28.05.2004 00:00:00,000 28.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 37", "ske.\\ : 29.05.2004 00:00:00,000 29.05.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 38", "ske.\\ : 30.05.2004 00:00:00,000 30.05.2004 23:59:59,999", strings[loc++]);            
    }
    else
      fail("Test 26-38");
    

    systemKalenderArbeiter.parseSystemkalenderEintrag("ske./", "/", "/:=ODER{ ,_,#}*,*");
    loescheSystemKalenderEintrag("ske.\\");

    d1 = sdf.parse("01.01.2003 00:00:00,000");
    d2 = sdf.parse("31.12.2004 23:59:59,999");

    sm = systemKalenderArbeiter.berechneGueltigVonBis("ske./", d1.getTime(), d2.getTime());

    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<String, Boolean> me : sm.entrySet())
      {

        String key = me.getKey();
        String[] split = key.split("_");

        Date d = new Date();
        d.setTime(Long.parseLong(split[0]));
        strings[cnt] = split[1] + " : " + sdf.format(d) + " " + me.getValue();

        cnt++;
      }

      assertTrue("Test 39", strings.length == 8);
      assertEquals("Test 40", "ske./ : 20.04.2003 00:00:00,000 true", strings[0]);
      assertEquals("Test 41", "ske./ : 20.04.2003 23:59:59,999 false", strings[1]);
      assertEquals("Test 42", "ske./ : 18.05.2003 00:00:00,000 true", strings[2]);
      assertEquals("Test 43", "ske./ : 18.05.2003 23:59:59,999 false", strings[3]);
      assertEquals("Test 44", "ske./ : 11.04.2004 00:00:00,000 true", strings[4]);
      assertEquals("Test 45", "ske./ : 11.04.2004 23:59:59,999 false", strings[5]);
      assertEquals("Test 46", "ske./ : 18.05.2004 00:00:00,000 true", strings[6]);
      assertEquals("Test 47", "ske./ : 18.05.2004 23:59:59,999 false", strings[7]);

    }
    else
      fail("Test 39-47");
    
    sm2 = systemKalenderArbeiter.berechneIntervallVonBis("ske./", d1.getTime(), d2.getTime());
    
    if (sm2 != null)
    {
      
      String[] strings = new String[sm2.entrySet().size()];
      
      int cnt = 0;
      for (Map.Entry<String, Long> me : sm2.entrySet())
      {
        
        String key = me.getKey();
        String[] split = key.split("_");
        
        Date d11 = new Date();
        d11.setTime(Long.parseLong(split[0]));
        Date d21 = new Date();
        d21.setTime(me.getValue());
        strings[cnt] = split[1] + " : " + sdf.format(d11) + " " + sdf.format(d21);

        cnt++;
      }
      
      int loc = 0;
      assertTrue("Test 48", strings.length == 4);
      assertEquals("Test 49", "ske./ : 20.04.2003 00:00:00,000 20.04.2003 23:59:59,999", strings[loc++]);
      assertEquals("Test 50", "ske./ : 18.05.2003 00:00:00,000 18.05.2003 23:59:59,999", strings[loc++]);
      assertEquals("Test 51", "ske./ : 11.04.2004 00:00:00,000 11.04.2004 23:59:59,999", strings[loc++]);
      assertEquals("Test 52", "ske./ : 18.05.2004 00:00:00,000 18.05.2004 23:59:59,999", strings[loc++]);            
    }
    else
      fail("Test 48-52");
    

    d1 = sdf.parse("01.11.2004 00:00:00,000");
    d2 = sdf.parse("08.11.2004 23:59:59,999");

    sm = systemKalenderArbeiter.berechneGueltigVonBis("ske.hauptverkehrszeit", d1.getTime(), d2.getTime());

    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<String, Boolean> me : sm.entrySet())
      {

        String key = me.getKey();
        String[] split = key.split("_");

        Date d = new Date();
        d.setTime(Long.parseLong(split[0]));
        strings[cnt] = split[1] + " : " + sdf.format(d) + " " + me.getValue();

        cnt++;
      }

      assertTrue("Test 53", strings.length == 32);
      assertEquals("Test 54", "ske.hauptverkehrszeit : 01.11.2004 07:15:00,000 true", strings[0]);
      assertEquals("Test 55", "ske.hauptverkehrszeit : 01.11.2004 08:59:59,999 false", strings[1]);
      assertEquals("Test 56", "ske.hauptverkehrszeit : 01.11.2004 15:30:00,000 true", strings[2]);
      assertEquals("Test 57", "ske.hauptverkehrszeit : 01.11.2004 17:44:59,999 false", strings[3]);
      assertEquals("Test 58", "ske.hauptverkehrszeit : 02.11.2004 07:15:00,000 true", strings[4]);
      assertEquals("Test 59", "ske.hauptverkehrszeit : 02.11.2004 08:59:59,999 false", strings[5]);
      assertEquals("Test 60", "ske.hauptverkehrszeit : 02.11.2004 15:30:00,000 true", strings[6]);
      assertEquals("Test 61", "ske.hauptverkehrszeit : 02.11.2004 17:44:59,999 false", strings[7]);
      assertEquals("Test 62", "ske.hauptverkehrszeit : 03.11.2004 07:15:00,000 true", strings[8]);
      assertEquals("Test 63", "ske.hauptverkehrszeit : 03.11.2004 08:59:59,999 false", strings[9]);
      assertEquals("Test 64", "ske.hauptverkehrszeit : 03.11.2004 15:30:00,000 true", strings[10]);
      assertEquals("Test 65", "ske.hauptverkehrszeit : 03.11.2004 17:44:59,999 false", strings[11]);
      assertEquals("Test 66", "ske.hauptverkehrszeit : 04.11.2004 07:15:00,000 true", strings[12]);
      assertEquals("Test 67", "ske.hauptverkehrszeit : 04.11.2004 08:59:59,999 false", strings[13]);
      assertEquals("Test 68", "ske.hauptverkehrszeit : 04.11.2004 15:30:00,000 true", strings[14]);
      assertEquals("Test 69", "ske.hauptverkehrszeit : 04.11.2004 17:44:59,999 false", strings[15]);
      assertEquals("Test 70", "ske.hauptverkehrszeit : 05.11.2004 07:15:00,000 true", strings[16]);
      assertEquals("Test 71", "ske.hauptverkehrszeit : 05.11.2004 08:59:59,999 false", strings[17]);
      assertEquals("Test 72", "ske.hauptverkehrszeit : 05.11.2004 15:30:00,000 true", strings[18]);
      assertEquals("Test 73", "ske.hauptverkehrszeit : 05.11.2004 17:44:59,999 false", strings[19]);
      assertEquals("Test 74", "ske.hauptverkehrszeit : 06.11.2004 07:15:00,000 true", strings[20]);
      assertEquals("Test 75", "ske.hauptverkehrszeit : 06.11.2004 08:59:59,999 false", strings[21]);
      assertEquals("Test 76", "ske.hauptverkehrszeit : 06.11.2004 15:30:00,000 true", strings[22]);
      assertEquals("Test 77", "ske.hauptverkehrszeit : 06.11.2004 17:44:59,999 false", strings[23]);
      assertEquals("Test 78", "ske.hauptverkehrszeit : 07.11.2004 07:15:00,000 true", strings[24]);
      assertEquals("Test 79", "ske.hauptverkehrszeit : 07.11.2004 08:59:59,999 false", strings[25]);
      assertEquals("Test 80", "ske.hauptverkehrszeit : 07.11.2004 15:30:00,000 true", strings[26]);
      assertEquals("Test 81", "ske.hauptverkehrszeit : 07.11.2004 17:44:59,999 false", strings[27]);
      assertEquals("Test 82", "ske.hauptverkehrszeit : 08.11.2004 07:15:00,000 true", strings[28]);
      assertEquals("Test 83", "ske.hauptverkehrszeit : 08.11.2004 08:59:59,999 false", strings[29]);
      assertEquals("Test 84", "ske.hauptverkehrszeit : 08.11.2004 15:30:00,000 true", strings[30]);
      assertEquals("Test 85", "ske.hauptverkehrszeit : 08.11.2004 17:44:59,999 false", strings[31]);

    }
    else
      fail("Test 53-85");
    
    sm2 = systemKalenderArbeiter.berechneIntervallVonBis("ske.hauptverkehrszeit", d1.getTime(), d2.getTime());
    
    if (sm2 != null)
    {
      
      String[] strings = new String[sm2.entrySet().size()];
      
      int cnt = 0;
      for (Map.Entry<String, Long> me : sm2.entrySet())
      {
        
        String key = me.getKey();
        String[] split = key.split("_");
        
        Date d11 = new Date();
        d11.setTime(Long.parseLong(split[0]));
        Date d21 = new Date();
        d21.setTime(me.getValue());
        strings[cnt] = split[1] + " : " + sdf.format(d11) + " " + sdf.format(d21);
                
        cnt++;
      }
      
      int loc = 0;
      assertTrue("Test 86", strings.length == 16);
      assertEquals("Test 87", "ske.hauptverkehrszeit : 01.11.2004 07:15:00,000 01.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 88", "ske.hauptverkehrszeit : 01.11.2004 15:30:00,000 01.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 89", "ske.hauptverkehrszeit : 02.11.2004 07:15:00,000 02.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 90", "ske.hauptverkehrszeit : 02.11.2004 15:30:00,000 02.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 91", "ske.hauptverkehrszeit : 03.11.2004 07:15:00,000 03.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 92", "ske.hauptverkehrszeit : 03.11.2004 15:30:00,000 03.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 93", "ske.hauptverkehrszeit : 04.11.2004 07:15:00,000 04.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 94", "ske.hauptverkehrszeit : 04.11.2004 15:30:00,000 04.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 95", "ske.hauptverkehrszeit : 05.11.2004 07:15:00,000 05.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 96", "ske.hauptverkehrszeit : 05.11.2004 15:30:00,000 05.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 97", "ske.hauptverkehrszeit : 06.11.2004 07:15:00,000 06.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 98", "ske.hauptverkehrszeit : 06.11.2004 15:30:00,000 06.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 99", "ske.hauptverkehrszeit : 07.11.2004 07:15:00,000 07.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 100", "ske.hauptverkehrszeit : 07.11.2004 15:30:00,000 07.11.2004 17:44:59,999", strings[loc++]);
      assertEquals("Test 101", "ske.hauptverkehrszeit : 08.11.2004 07:15:00,000 08.11.2004 08:59:59,999", strings[loc++]);
      assertEquals("Test 102", "ske.hauptverkehrszeit : 08.11.2004 15:30:00,000 08.11.2004 17:44:59,999", strings[loc++]);
      
    }
    else
      fail("Test 86-102");
    

    Calendar cal = Calendar.getInstance();

    cal.setTime(new Date());
    cal.set(Calendar.HOUR_OF_DAY, 12);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    // Jetzt ist heute 12:00:00,000
    String jetzt = sdf.format(cal.getTime());

    Map.Entry<String, Boolean> entry = systemKalenderArbeiter.berechneGueltigJetzt("ske.gueltig", cal.getTimeInMillis());

    if (entry != null)
    {

      String key = entry.getKey();
      String[] split = key.split("_");

      Date d = new Date();

      d.setTime(Long.parseLong(split[0]));
      String s = split[1] + " : " + sdf.format(d) + " " + entry.getValue();

      assertEquals("Test 103", "ske.gueltig : " + jetzt + " true", s);

    }

    entry = systemKalenderArbeiter.berechneGueltigJetzt("ske.ungueltig", cal.getTimeInMillis());

    if (entry != null)
    {

      String key = entry.getKey();
      String[] split = key.split("_");

      Date d = new Date();
      d.setTime(Long.parseLong(split[0]));
      String s = split[1] + " : " + sdf.format(d) + " " + entry.getValue();

      assertEquals("Test 104", "ske.ungueltig : " + jetzt + " false", s);
    }

  }
  
  public static void erzeugeSystemKalenderEintrag(String pid, String name, String definition)
  {

    ConfigurationArea ca = connection.getDataModel().getConfigurationArea(pidKonfigBereich);
    ConfigurationObject co = (ConfigurationObject)connection.getDataModel().getObject(pidKalender);
    DynamicObjectType dot = (DynamicObjectType)connection.getDataModel().getType("typ.systemKalenderEintrag");

    // Erzeuge dynamisches Objekt
    AttributeGroup atgKonfig = connection.getDataModel().getAttributeGroup("atg.systemKalenderEintrag");
    Aspect aspKonfig = connection.getDataModel().getAspect("asp.eigenschaften");
    VerwaltungDynObj vewKonfig = new VerwaltungDynObj(connection, connection.getDataModel(), ca, dot, co, atgKonfig,
        aspKonfig);

    Data data = connection.createData(atgKonfig);
    Data[] datas = new Data[1];
    datas[0] = data;
    vewKonfig.erzeuge(pid, name, "SystemKalenderEinträge", null);

    // Erzeuge Parameter
    AttributeGroup atgParam = connection.getDataModel().getAttributeGroup("atg.systemKalenderEintrag");
    Aspect aspParam = connection.getDataModel().getAspect("asp.parameterVorgabe");
    VerwaltungDynObj vewParam = new VerwaltungDynObj(connection, connection.getDataModel(), ca, dot, co, atgParam,
        aspParam);
    data = connection.createData(atgParam);
    data.getTextValue("Definition").setText(definition);
    datas[0] = data;
    vewParam.setDynamicObject(connection.getDataModel().getObject(pid));
    vewParam.parametriere(datas);
  }

  public static void loescheSystemKalenderEintrag(String pid)
  {
    ConfigurationArea ca = connection.getDataModel().getConfigurationArea(pidKonfigBereich);
    ConfigurationObject co = (ConfigurationObject)connection.getDataModel().getObject(pidKalender);
    DynamicObjectType dot = (DynamicObjectType)connection.getDataModel().getType("typ.systemKalenderEintrag");

    AttributeGroup atgKonfig = connection.getDataModel().getAttributeGroup("atg.systemKalenderEintrag");
    Aspect aspKonfig = connection.getDataModel().getAspect("asp.eigenschaften");
    VerwaltungDynObj vewKonfig = new VerwaltungDynObj(connection, connection.getDataModel(), ca, dot, co, atgKonfig,
        aspKonfig);

    vewKonfig.setDynamicObject(connection.getDataModel().getObject(pid));
    vewKonfig.loesche(pid, "SystemKalenderEinträge");
  }

}

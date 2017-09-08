package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.SortedMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SystemkalenderEintragTest
{

  // /**
  // * Datenverteilerverbindung.
  // */
  // private static ClientDavInterface _connection = null;
  //
  // /**
  // * Pid Kalender
  // */
  // private static String _strKalender = "kv.testKonfiguration";

  /**
   * Aufbau der Datenverteilerverbindung.
   * 
   * @throws Exception
   *           kann durch verschiedene Quellen erzeut werden.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Datenverteilerverbindung aufbauen
    // ClientDavParameters parameters = new ClientDavParameters();
    // parameters.setDavCommunicationAddress("localhost");
    // parameters.setDavCommunicationSubAddress(8083);
    // parameters.setUserName("Tester");
    // parameters.setUserPassword("geheim");
    //
    // _connection = new ClientDavConnection(parameters);
    // _connection.connect();
    // _connection.login();

    SystemkalenderArbeiter.getSkeList().clear();

    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.montag", "Montag", "Montag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.dienstag", "Dienstag", "Dienstag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.mittwoch", "Mittwoch", "Mittwoch");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.donnerstag", "Donnerstag", "Donnerstag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.freitag", "Freitag", "Freitag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.samstag", "Samstag", "Samstag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.sonntag", "Sonntag", "Sonntag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tag", "Tag", "Tag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.ostersonntag", "Ostersonntag", "Ostersonntag");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.bereich1", "Bereich1",
        "Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.bereich2", "Bereich2",
        "Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.bereich3", "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.bereich4", "Bereich4",
        "Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tddeAlt", "TDDEalt", "17.06.1963,1989");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tddeNeu", "TDDEneu", "03.10.1990,*");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.tdde", "Tag der deutschen Einheit",
        "ODER{TDDEalt,TDDEneu}*,*");
    SystemkalenderArbeiter.parseSystemkalenderEintrag("ske.bereich5", "Bereich5",
    "Bereich5:=<01.09.2009-30.09.2009>({08:00:00,000-16:00:00,000})");
  }

  /**
   * Abbau der Datenverteilerverbindung.
   * 
   * @throws Exception
   *           kann durch verschiedene Quellen erzeut werden.
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    // _skeArbeiter.stoppeSystemKalenderArbeiter();
    // _connection.disconnect(false, "");
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void isGueltigTest() throws Exception
  {
    Calendar cal = new GregorianCalendar().getInstance();

    int dow = cal.get(Calendar.DAY_OF_WEEK);

    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();

    SystemkalenderEintrag skeMo = skeList.get("ske.montag");
    SystemkalenderEintrag skeDi = skeList.get("ske.dienstag");
    SystemkalenderEintrag skeMi = skeList.get("ske.mittwoch");
    SystemkalenderEintrag skeDo = skeList.get("ske.donnerstag");
    SystemkalenderEintrag skeFr = skeList.get("ske.freitag");
    SystemkalenderEintrag skeSa = skeList.get("ske.samstag");
    SystemkalenderEintrag skeSo = skeList.get("ske.sonntag");

    switch (dow)
    {
      case 0:
        assertTrue("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;
      case 1:
        assertFalse("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertTrue("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;
      case 2:
        assertFalse("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertTrue("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;
      case 3:
        assertFalse("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertTrue("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;
      case 4:
        assertFalse("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertTrue("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;
      case 5:
        assertFalse("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertTrue("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;
      case 6:
        assertFalse("Test 0", skeSa.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 1", skeSo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 2", skeMo.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 3", skeDi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 4", skeMi.isGueltig(cal.getTimeInMillis()));
        assertFalse("Test 5", skeDo.isGueltig(cal.getTimeInMillis()));
        assertTrue("Test 6", skeFr.isGueltig(cal.getTimeInMillis()));
        break;

      default:
        fail("Test 0-6");

    }

  }

  @Test
  public void isGueltigVonBisTest() throws Exception
  {
    Calendar cal = new GregorianCalendar().getInstance();

    int dow = cal.get(Calendar.DAY_OF_WEEK);

    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();

    SystemkalenderEintrag ske = null;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
    int dom = cal.get(Calendar.DAY_OF_MONTH);
    int mon = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    switch (dow)
    {
      case 0:
        ske = skeList.get("ske.samstag");
        break;
      case 1:
        ske = skeList.get("ske.sonntag");
        break;
      case 2:
        ske = skeList.get("ske.montag");
        break;
      case 3:
        ske = skeList.get("ske.dienstag");
        break;
      case 4:
        ske = skeList.get("ske.mittwoch");
        break;
      case 5:
        ske = skeList.get("ske.donnerstag");
        break;
      case 6:
        ske = skeList.get("ske.freitag");
        break;
      default:
        fail("Test 8");
    }

    Date d1 = sdf.parse(dom + "." + mon + "." + year + " 10:00:00,000");
    Date d2 = sdf.parse(dom + "." + mon + "." + year + " 22:59:59,999");

    assertFalse("Test 8", ske.isGueltigVonBis(d1.getTime(), d2.getTime()));

    Date d3 = sdf.parse(dom - 1 + "." + mon + "." + year + " 10:00:00,000");
    Date d4 = sdf.parse(dom + "." + mon + "." + year + " 09:59:59,999");

    assertTrue("Test 9", ske.isGueltigVonBis(d3.getTime(), d4.getTime()));

    Date d5 = sdf.parse(dom - 2 + "." + mon + "." + year + " 10:00:00,000");
    Date d6 = sdf.parse(dom - 1 + "." + mon + "." + year + " 09:59:59,999");

    assertFalse("Test 10", ske.isGueltigVonBis(d5.getTime(), d6.getTime()));

    Date d7 = sdf.parse(dom + "." + mon + "." + year + " 10:00:00,000");
    Date d8 = sdf.parse(dom + 1 + "." + mon + "." + year + " 09:59:59,999");

    assertFalse("Test 11", ske.isGueltigVonBis(d7.getTime(), d8.getTime()));

  }

  @Test
  public void berechneZustandsWechselVonBisTest() throws Exception
  {
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();

    SystemkalenderEintrag ske = skeList.get("ske.bereich4");

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

    Date d1 = sdf.parse("15.01.2008 00:00:00,000");
    Date d2 = sdf.parse("16.01.2008 23:59:59,999");

    SortedMap<Long, Boolean> sm = ske.berecheneZustandsWechselVonBis(d1.getTime(), d2.getTime());

    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      System.out.println("SystemkalenderEintragTest.berechneZustandsWechselVonBisTest(): " + strings.length);
      int cnt = 0;
      for (Map.Entry<Long, Boolean> me : sm.entrySet())
      {
        Date d = new Date();
        d.setTime(me.getKey());
         String var = sdf.format(d) + " " + me.getValue();
         System.out.println("SystemkalenderEintragTest.berechneZustandsWechselVonBisTest(): " + var);
         strings[cnt] = var;
         cnt++;
        
      }

      assertTrue("Test 12", strings.length == 8);
      

      assertEquals("Test 13", "15.01.2008 09:00:00,000 true", strings[0]);
      assertEquals("Test 14", "15.01.2008 11:59:59,999 false", strings[1]);
      assertEquals("Test 15", "15.01.2008 15:30:00,000 true", strings[2]);
      assertEquals("Test 16", "15.01.2008 17:59:59,999 false", strings[3]);
      assertEquals("Test 17", "16.01.2008 09:00:00,000 true", strings[4]);
      assertEquals("Test 18", "16.01.2008 11:59:59,999 false", strings[5]);
      assertEquals("Test 19", "16.01.2008 15:30:00,000 true", strings[6]);
      assertEquals("Test 20", "16.01.2008 17:59:59,999 false", strings[7]);

    }
    else
      fail("Test 12-20");

    ske = skeList.get("ske.tdde");

    d1 = sdf.parse("01.01.1960 00:00:00,000");
    d2 = sdf.parse("31.12.2010 23:59:59,999");

    sm = ske.berecheneZustandsWechselVonBis(d1.getTime(), d2.getTime());

    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<Long, Boolean> me : sm.entrySet())
      {
        Date d = new Date();
        d.setTime(me.getKey());
        strings[cnt] = sdf.format(d) + " " + me.getValue();
        cnt++;
      }

      int loc = 0;

      assertTrue("Test 21", strings.length == 96);
      assertEquals("Test 22", "17.06.1963 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 23", "17.06.1963 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 24", "17.06.1964 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 25", "17.06.1964 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 26", "17.06.1965 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 27", "17.06.1965 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 28", "17.06.1966 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 29", "17.06.1966 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 30", "17.06.1967 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 31", "17.06.1967 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 32", "17.06.1968 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 33", "17.06.1968 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 34", "17.06.1969 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 35", "17.06.1969 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 36", "17.06.1970 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 37", "17.06.1970 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 38", "17.06.1971 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 39", "17.06.1971 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 40", "17.06.1972 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 41", "17.06.1972 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 42", "17.06.1973 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 43", "17.06.1973 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 44", "17.06.1974 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 45", "17.06.1974 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 46", "17.06.1975 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 47", "17.06.1975 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 48", "17.06.1976 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 49", "17.06.1976 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 50", "17.06.1977 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 51", "17.06.1977 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 52", "17.06.1978 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 53", "17.06.1978 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 54", "17.06.1979 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 55", "17.06.1979 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 56", "17.06.1980 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 57", "17.06.1980 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 58", "17.06.1981 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 59", "17.06.1981 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 60", "17.06.1982 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 61", "17.06.1982 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 62", "17.06.1983 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 63", "17.06.1983 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 64", "17.06.1984 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 65", "17.06.1984 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 66", "17.06.1985 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 67", "17.06.1985 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 68", "17.06.1986 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 69", "17.06.1986 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 70", "17.06.1987 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 71", "17.06.1987 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 72", "17.06.1988 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 73", "17.06.1988 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 74", "17.06.1989 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 75", "17.06.1989 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 76", "03.10.1990 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 77", "03.10.1990 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 78", "03.10.1991 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 79", "03.10.1991 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 80", "03.10.1992 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 81", "03.10.1992 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 82", "03.10.1993 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 83", "03.10.1993 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 84", "03.10.1994 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 85", "03.10.1994 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 86", "03.10.1995 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 87", "03.10.1995 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 88", "03.10.1996 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 89", "03.10.1996 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 90", "03.10.1997 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 91", "03.10.1997 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 92", "03.10.1998 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 93", "03.10.1998 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 94", "03.10.1999 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 95", "03.10.1999 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 96", "03.10.2000 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 97", "03.10.2000 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 98", "03.10.2001 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 99", "03.10.2001 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 100", "03.10.2002 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 101", "03.10.2002 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 102", "03.10.2003 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 103", "03.10.2003 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 104", "03.10.2004 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 105", "03.10.2004 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 106", "03.10.2005 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 107", "03.10.2005 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 108", "03.10.2006 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 109", "03.10.2006 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 110", "03.10.2007 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 111", "03.10.2007 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 112", "03.10.2008 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 113", "03.10.2008 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 114", "03.10.2009 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 115", "03.10.2009 23:59:59,999 false", strings[loc++]);
      assertEquals("Test 116", "03.10.2010 00:00:00,000 true", strings[loc++]);
      assertEquals("Test 117", "03.10.2010 23:59:59,999 false", strings[loc++]);

    }
    else
      fail("Test 21-117");

  }

  @Test
  public void berechneIntervallVonBisTest() throws Exception
  {
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();

    SystemkalenderEintrag ske = skeList.get("ske.bereich4");

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

    Date d1 = sdf.parse("15.01.2008 00:00:00,000");
    Date d2 = sdf.parse("15.02.2008 23:59:59,999");

    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());

    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<Long, Long> me : sm.entrySet())
      {
        Date dVon = new Date();
        dVon.setTime(me.getKey());
        Date dBis = new Date();
        dBis.setTime(me.getValue());
        strings[cnt] = sdf.format(dVon) + " " + sdf.format(dBis);

//        System.out.println(strings[cnt]);
        cnt++;

      }

      
//      System.out.println(strings.length);
      
      assertTrue("Test 118", strings.length == 64);

      int loc = 0;

      assertEquals("Test 119", "15.01.2008 09:00:00,000 15.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 120", "15.01.2008 15:30:00,000 15.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 121", "16.01.2008 09:00:00,000 16.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 122", "16.01.2008 15:30:00,000 16.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 123", "17.01.2008 09:00:00,000 17.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 124", "17.01.2008 15:30:00,000 17.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 125", "18.01.2008 09:00:00,000 18.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 126", "18.01.2008 15:30:00,000 18.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 127", "19.01.2008 09:00:00,000 19.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 128", "19.01.2008 15:30:00,000 19.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 129", "20.01.2008 09:00:00,000 20.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 130", "20.01.2008 15:30:00,000 20.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 131", "21.01.2008 09:00:00,000 21.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 132", "21.01.2008 15:30:00,000 21.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 133", "22.01.2008 09:00:00,000 22.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 134", "22.01.2008 15:30:00,000 22.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 135", "23.01.2008 09:00:00,000 23.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 136", "23.01.2008 15:30:00,000 23.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 137", "24.01.2008 09:00:00,000 24.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 138", "24.01.2008 15:30:00,000 24.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 139", "25.01.2008 09:00:00,000 25.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 140", "25.01.2008 15:30:00,000 25.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 141", "26.01.2008 09:00:00,000 26.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 142", "26.01.2008 15:30:00,000 26.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 143", "27.01.2008 09:00:00,000 27.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 144", "27.01.2008 15:30:00,000 27.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 145", "28.01.2008 09:00:00,000 28.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 146", "28.01.2008 15:30:00,000 28.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 147", "29.01.2008 09:00:00,000 29.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 148", "29.01.2008 15:30:00,000 29.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 149", "30.01.2008 09:00:00,000 30.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 150", "30.01.2008 15:30:00,000 30.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 151", "31.01.2008 09:00:00,000 31.01.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 152", "31.01.2008 15:30:00,000 31.01.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 153", "01.02.2008 09:00:00,000 01.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 154", "01.02.2008 15:30:00,000 01.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 155", "02.02.2008 09:00:00,000 02.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 156", "02.02.2008 15:30:00,000 02.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 157", "03.02.2008 09:00:00,000 03.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 158", "03.02.2008 15:30:00,000 03.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 159", "04.02.2008 09:00:00,000 04.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 160", "04.02.2008 15:30:00,000 04.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 161", "05.02.2008 09:00:00,000 05.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 162", "05.02.2008 15:30:00,000 05.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 163", "06.02.2008 09:00:00,000 06.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 164", "06.02.2008 15:30:00,000 06.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 165", "07.02.2008 09:00:00,000 07.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 166", "07.02.2008 15:30:00,000 07.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 167", "08.02.2008 09:00:00,000 08.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 168", "08.02.2008 15:30:00,000 08.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 169", "09.02.2008 09:00:00,000 09.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 170", "09.02.2008 15:30:00,000 09.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 171", "10.02.2008 09:00:00,000 10.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 172", "10.02.2008 15:30:00,000 10.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 173", "11.02.2008 09:00:00,000 11.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 174", "11.02.2008 15:30:00,000 11.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 175", "12.02.2008 09:00:00,000 12.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 176", "12.02.2008 15:30:00,000 12.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 177", "13.02.2008 09:00:00,000 13.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 178", "13.02.2008 15:30:00,000 13.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 179", "14.02.2008 09:00:00,000 14.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 180", "14.02.2008 15:30:00,000 14.02.2008 17:59:59,999", strings[loc++]);
      assertEquals("Test 181", "15.02.2008 09:00:00,000 15.02.2008 11:59:59,999", strings[loc++]);
      assertEquals("Test 182", "15.02.2008 15:30:00,000 15.02.2008 17:59:59,999", strings[loc++]);
    }
    else
      fail("Test 118-182");
  }

  @Test
  public void berechneIntervallVonBisTest2() throws Exception
  {
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();

    SystemkalenderEintrag ske = skeList.get("ske.bereich5");

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

    Date d1 = sdf.parse("19.06.2009 18:00:00,000");
    Date d2 = sdf.parse("22.06.2009 05:00:00,000");

    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());

    if (sm != null)
    {
      assertTrue("Test 183", sm.size() == 0);
    }
    
    else
      fail("Test 183");
  }
  
  @Test
  public void berechneIntervallVonBisTest3() throws Exception
  {
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();
    
    SystemkalenderEintrag ske = skeList.get("ske.bereich5");
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
    
    Date d1 = sdf.parse("02.09.2009 07:30:00,000");
    Date d2 = sdf.parse("02.09.2009 10:00:00,000");
    
    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());
    
    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<Long, Long> me : sm.entrySet())
      {
        Date dVon = new Date();
        dVon.setTime(me.getKey());
        Date dBis = new Date();
        dBis.setTime(me.getValue());
        strings[cnt] = sdf.format(dVon) + " " + sdf.format(dBis);

        cnt++;

      }

      assertTrue("Test 184", strings.length == 1);

      int loc = 0;

      assertEquals("Test 185", "02.09.2009 08:00:00,000 02.09.2009 10:00:00,000", strings[loc++]);
    }
    
    else
      fail("Test 184-185");
  }
  
  @Test
  public void berechneIntervallVonBisTest4() throws Exception
  {
    
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();
    
    SystemkalenderEintrag ske = skeList.get("ske.bereich5");
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
    
    Date d1 = sdf.parse("02.09.2009 10:00:00,000");
    Date d2 = sdf.parse("02.09.2009 12:00:00,000");
    
    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());
    
    if (sm != null)
    {
      
      String[] strings = new String[sm.entrySet().size()];
      
      int cnt = 0;
      for (Map.Entry<Long, Long> me : sm.entrySet())
      {
        Date dVon = new Date();
        dVon.setTime(me.getKey());
        Date dBis = new Date();
        dBis.setTime(me.getValue());
        strings[cnt] = sdf.format(dVon) + " " + sdf.format(dBis);
        
        cnt++;
        
      }
            
      assertTrue("Test 186", strings.length == 1);
      
      int loc = 0;
      
      assertEquals("Test 187", "02.09.2009 10:00:00,000 02.09.2009 12:00:00,000", strings[loc++]);
    }
  
    else
      fail("Test 186-187");
  }
  
  @Test
  public void berechneIntervallVonBisTest5() throws Exception
  {
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();
    
    SystemkalenderEintrag ske = skeList.get("ske.bereich5");
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
    
    Date d1 = sdf.parse("02.09.2009 15:00:00,000");
    Date d2 = sdf.parse("02.09.2009 18:00:00,000");
    
    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());
    
    if (sm != null)
    {

      String[] strings = new String[sm.entrySet().size()];

      int cnt = 0;
      for (Map.Entry<Long, Long> me : sm.entrySet())
      {
        Date dVon = new Date();
        dVon.setTime(me.getKey());
        Date dBis = new Date();
        dBis.setTime(me.getValue());
        strings[cnt] = sdf.format(dVon) + " " + sdf.format(dBis);

        cnt++;

      }

      assertTrue("Test 184", strings.length == 1);

      int loc = 0;

      assertEquals("Test 185", "02.09.2009 15:00:00,000 02.09.2009 16:00:00,000", strings[loc++]);
    }
    
    else
      fail("Test 184-185");
  }
  
  @Test
  public void berechneIntervallVonBisTest6() throws Exception
  {
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();

    SystemkalenderEintrag ske = skeList.get("ske.bereich5");

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

    Date d1 = sdf.parse("15.10.2009 08:30:00,000");
    Date d2 = sdf.parse("15.10.2009 15:30:00,000");

    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());

    if (sm != null)
    {
      assertTrue("Test 188", sm.size() == 0);
    }
    
    else
      fail("Test 188");
  }
  
  @Test
  public void berechneIntervallVonBisTest7() throws Exception
  {
    
    Map<String, SystemkalenderEintrag> skeList = SystemkalenderArbeiter.getSkeList();
    
    SystemkalenderEintrag ske = skeList.get("ske.bereich4");
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");
    
    Date d1 = sdf.parse("18.01.2008 10:00:00,000");
    Date d2 = sdf.parse("18.01.2008 10:29:59,999");
    
    SortedMap<Long, Long> sm = ske.berecheneIntervallVonBis(d1.getTime(), d2.getTime());
    
    if (sm != null)
    {
      
      String[] strings = new String[sm.entrySet().size()];
      
      int cnt = 0;
      for (Map.Entry<Long, Long> me : sm.entrySet())
      {
        Date dVon = new Date();
        dVon.setTime(me.getKey());
        Date dBis = new Date();
        dBis.setTime(me.getValue());
        strings[cnt] = sdf.format(dVon) + " " + sdf.format(dBis);
        
        cnt++;
        
      }
            
      assertTrue("Test 189", strings.length == 1);
      
      int loc = 0;
      
      assertEquals("Test 190", "18.01.2008 10:00:00,000 18.01.2008 10:29:59,999", strings[loc++]);
    }
    
  
    else
      fail("Test 189-190");
  }

}

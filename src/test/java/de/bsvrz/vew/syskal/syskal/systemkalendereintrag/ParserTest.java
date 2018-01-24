package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParserTest {

	private static Parser parser;

	/**
	 * Aufbau der Testumgebung.
	 * 
	 * @throws Exception
	 *             kann durch verschiedene Quellen erzeut werden.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parser = new Parser(new SystemkalenderArbeiter(null, null));

		parser.parseSystemkalenderEintrag("ske.montag", "Montag", "Montag");
		parser.parseSystemkalenderEintrag("ske.dienstag", "Dienstag", "Dienstag");
		parser.parseSystemkalenderEintrag("ske.mittwoch", "Mittwoch", "Mittwoch");
		parser.parseSystemkalenderEintrag("ske.donnerstag", "Donnerstag", "Donnerstag");
		parser.parseSystemkalenderEintrag("ske.freitag", "Freitag", "Freitag");
		parser.parseSystemkalenderEintrag("ske.samstag", "Samstag", "Samstag");
		parser.parseSystemkalenderEintrag("ske.sonntag", "Sonntag", "Sonntag");
		parser.parseSystemkalenderEintrag("ske.tag", "Tag", "Tag");
		parser.parseSystemkalenderEintrag("ske.ostersonntag", "Ostersonntag", "Ostersonntag");
		parser.parseSystemkalenderEintrag("ske.bereich1", "Bereich1",
				"Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>");
		parser.parseSystemkalenderEintrag("ske.bereich2", "Bereich2",
				"Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>");
		parser.parseSystemkalenderEintrag("ske.bereich3", "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>");
		parser.parseSystemkalenderEintrag("ske.bereich4", "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Abbau der Datenverteilerverbindung.
	 * 
	 * @throws Exception
	 *             kann durch verschiedene Quellen erzeut werden.
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testParseSystemkalenderEintrag() {
		try {
			assertTrue("Test 1", parser.parseSystemkalenderEintrag("ske.1", "Ske1", "Montag+1Tag"));
			assertTrue("Test 2", parser.parseSystemkalenderEintrag("ske.2", "Ske2", "Dienstag-1Tag"));
			assertTrue("Test 3", parser.parseSystemkalenderEintrag("ske.3", "Ske3", "Mittwoch+1Tag"));
			assertTrue("Test 4", parser.parseSystemkalenderEintrag("ske.4", "Ske4", "Donnerstag+2Tage"));
			assertTrue("Test 5", parser.parseSystemkalenderEintrag("ske.5", "Ske5", "UND{Bereich1,Bereich2}*,*"));
			assertTrue("Test 6", parser.parseSystemkalenderEintrag("ske.6", "Ske6", "ODER{Bereich1,Bereich2}*,*"));
			assertTrue("Test 7",
					parser.parseSystemkalenderEintrag("ske.7", "Ske7", "ODER{Bereich1,NICHT Bereich2}*,*"));
			assertTrue("Test 8",
					parser.parseSystemkalenderEintrag("ske.8", "Ske8", "UND{NICHT Bereich1,Bereich2}*,2008"));
			assertTrue("Test 9", parser.parseSystemkalenderEintrag("ske.9", "Ske9", "ODER{Bereich4,Bereich2}*,*"));
			assertTrue("Test 10", parser.parseSystemkalenderEintrag("ske.10", "Ske10", "UND{Bereich3,Bereich1}*,*"));
			assertTrue("Test 11", parser.parseSystemkalenderEintrag("ske.11", "Ske11", "UND{Ske5,Ske6}*,*"));

			assertFalse("Test 12", parser.parseSystemkalenderEintrag("ske.12", "Ske12", "Sontag+1Tag"));
			assertFalse("Test 13", parser.parseSystemkalenderEintrag("ske.13", "Ske13", "Dienstag2-1Tag"));
			assertFalse("Test 14", parser.parseSystemkalenderEintrag("ske.14", "Ske14", "aMittwoch+1Tag"));
			assertFalse("Test 15", parser.parseSystemkalenderEintrag("ske.15", "Ske15", "OTER{Bereich1,Bereich2}*,*"));
			assertFalse("Test 16",
					parser.parseSystemkalenderEintrag("ske.16", "Ske16", "ODER{BereichXyz,Bereich2}*,*"));
			assertFalse("Test 17", parser.parseSystemkalenderEintrag("ske.17", "Ske17", "ODER{Bereich4,Bereich2*,*"));
			assertFalse("Test 18", parser.parseSystemkalenderEintrag("ske.18", "Ske18", "Ske15"));
			assertFalse("Test 19", parser.parseSystemkalenderEintrag("ske.19", "Ske19", "Ske16+1Tag"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}

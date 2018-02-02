package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class ParserTest {

	private static TestKalenderEintragProvider eintragsProvider;

	/**
	 * Aufbau der Testumgebung.
	 * 
	 * @throws Exception
	 *             kann durch verschiedene Quellen erzeut werden.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		eintragsProvider = new TestKalenderEintragProvider();

		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Montag", "Montag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Dienstag", "Dienstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Mittwoch", "Mittwoch"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Donnerstag", "Donnerstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Freitag", "Freitag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Samstag", "Samstag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Sonntag", "Sonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Tag", "Tag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Ostersonntag", "Ostersonntag"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich1",
				"Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich2",
				"Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>"));
		eintragsProvider.addEintrag(
				KalenderEintrag.parse(eintragsProvider, "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>"));
		eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})"));

	}

	private KalenderEintrag ergaenzeEintrag(String name, String definition) {
		KalenderEintrag eintragDefinition = KalenderEintrag.parse(eintragsProvider, name,
				definition);
		eintragsProvider.addEintrag(eintragDefinition);
		return eintragDefinition;
	}

	@Test
	public void testParseSystemkalenderEintrag() {
		assertTrue("Test 1", !ergaenzeEintrag("Ske1", "Montag+1Tag").isFehler());
		assertTrue("Test 2", !ergaenzeEintrag("Ske2", "Dienstag-1Tag").isFehler());
		assertTrue("Test 3", !ergaenzeEintrag("Ske3", "Mittwoch+1Tag").isFehler());
		assertTrue("Test 4", !ergaenzeEintrag("Ske4", "Donnerstag+2Tage").isFehler());
		assertTrue("Test 5", !ergaenzeEintrag("Ske5", "UND{Bereich1,Bereich2}*,*").isFehler());
		assertTrue("Test 6", !ergaenzeEintrag("Ske6", "ODER{Bereich1,Bereich2}*,*").isFehler());
		assertTrue("Test 7", !ergaenzeEintrag("Ske7", "ODER{Bereich1,NICHT Bereich2}*,*").isFehler());
		assertTrue("Test 8", !ergaenzeEintrag("Ske8", "UND{NICHT Bereich1,Bereich2}*,2008").isFehler());
		assertTrue("Test 9", !ergaenzeEintrag("Ske9", "ODER{Bereich4,Bereich2}*,*").isFehler());
		assertTrue("Test 10", !ergaenzeEintrag("Ske10", "UND{Bereich3,Bereich1}*,*").isFehler());
		assertTrue("Test 11", !ergaenzeEintrag("Ske11", "UND{Ske5,Ske6}*,*").isFehler());

		assertFalse("Test 12", !ergaenzeEintrag("Ske12", "Sontag+1Tag").isFehler());
		assertFalse("Test 13", !ergaenzeEintrag("Ske13", "Dienstag2-1Tag").isFehler());
		assertFalse("Test 14", !ergaenzeEintrag("Ske14", "aMittwoch+1Tag").isFehler());
		assertFalse("Test 15", !ergaenzeEintrag("Ske15", "OTER{Bereich1,Bereich2}*,*").isFehler());
		assertFalse("Test 16", !ergaenzeEintrag("Ske16", "ODER{BereichXyz,Bereich2}*,*").isFehler());
		assertFalse("Test 17", !ergaenzeEintrag("Ske17", "ODER{Bereich4,Bereich2*,*").isFehler());
		assertTrue("Test 18", !ergaenzeEintrag("Ske18", "Ske15").isFehler());
		assertTrue("Test 19", !ergaenzeEintrag("Ske19", "Ske16+1Tag").isFehler());
	}

}

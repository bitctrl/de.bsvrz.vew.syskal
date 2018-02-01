package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.internal.KalenderEintrag;

public class KalenderEintragTest {

	private static TestKalenderEintragProvider provider;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		provider = new TestKalenderEintragProvider();

		provider.addEintrag(KalenderEintrag.parse(provider, "Montag", "Montag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Dienstag", "Dienstag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Mittwoch", "Mittwoch"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Donnerstag", "Donnerstag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Freitag", "Freitag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Samstag", "Samstag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Sonntag", "Sonntag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Tag", "Tag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Ostersonntag", "Ostersonntag"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Bereich1",
				"Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Bereich2",
				"Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>"));
		provider.addEintrag(KalenderEintrag.parse(provider, "Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})"));

	}

	@Test
	public void testParseSystemkalenderEintrag() {

		assertFalse("Test 1", provider.parseAndAdd(provider, "Ske1", "Montag+1Tag").isFehler());
		assertFalse("Test 2", provider.parseAndAdd(provider, "Ske2", "Dienstag-1Tag").isFehler());
		assertFalse("Test 3", provider.parseAndAdd(provider, "Ske3", "Mittwoch+1Tag").isFehler());
		assertFalse("Test 4", provider.parseAndAdd(provider, "Ske4", "Donnerstag+2Tage").isFehler());
		assertFalse("Test 5", provider.parseAndAdd(provider, "Ske5", "UND{Bereich1,Bereich2}*,*").isFehler());
		assertFalse("Test 6", provider.parseAndAdd(provider, "Ske6", "ODER{Bereich1,Bereich2}*,*").isFehler());
		assertFalse("Test 7", provider.parseAndAdd(provider, "Ske7", "ODER{Bereich1,NICHT Bereich2}*,*").isFehler());
		assertFalse("Test 8", provider.parseAndAdd(provider, "Ske8", "UND{NICHT Bereich1,Bereich2}*,2008").isFehler());
		assertFalse("Test 9", provider.parseAndAdd(provider, "Ske9", "ODER{Bereich4,Bereich2}*,*").isFehler());
		assertFalse("Test 10", provider.parseAndAdd(provider, "Ske10", "UND{Bereich3,Bereich1}*,*").isFehler());
		assertFalse("Test 11", provider.parseAndAdd(provider, "Ske11", "UND{Ske5,Ske6}*,*").isFehler());

		assertTrue("Test 12", provider.parseAndAdd(provider, "Ske12", "Sontag+1Tag").isFehler());
		assertTrue("Test 13", provider.parseAndAdd(provider, "Ske13", "Dienstag2-1Tag").isFehler());
		assertTrue("Test 14", provider.parseAndAdd(provider, "Ske14", "aMittwoch+1Tag").isFehler());
		assertTrue("Test 15", provider.parseAndAdd(provider, "Ske15", "OTER{Bereich1,Bereich2}*,*").isFehler());
		assertTrue("Test 16", provider.parseAndAdd(provider, "Ske16", "ODER{BereichXyz,Bereich2}*,*").isFehler());
		assertTrue("Test 17", provider.parseAndAdd(provider, "Ske17", "ODER{Bereich4,Bereich2*,*").isFehler());
		assertFalse("Test 18", provider.parseAndAdd(provider, "Ske18", "Ske15").isFehler());
		assertFalse("Test 19", provider.parseAndAdd(provider, "Ske19", "Ske16+1Tag").isFehler());
	}

}

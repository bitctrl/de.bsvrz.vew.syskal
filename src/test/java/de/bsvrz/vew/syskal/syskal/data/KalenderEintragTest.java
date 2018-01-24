package de.bsvrz.vew.syskal.syskal.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class KalenderEintragTest {

	private static KalenderEintragProvider provider;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		provider = new KalenderEintragProvider() {

			Map<String, KalenderEintrag> eintraege = new LinkedHashMap<>();
			
			@Override
			public KalenderEintrag getEintrag(String name) {
				return eintraege.get(name);
			}
			
			@Override
			public void addEintrag(String name, KalenderEintrag eintrag) {
				eintraege.put(name, eintrag);
			}
		};

		KalenderEintrag.parse( provider,"Montag", "Montag");
		KalenderEintrag.parse( provider,"Dienstag", "Dienstag");
		KalenderEintrag.parse( provider,"Mittwoch", "Mittwoch");
		KalenderEintrag.parse( provider,"Donnerstag", "Donnerstag");
		KalenderEintrag.parse( provider,"Freitag", "Freitag");
		KalenderEintrag.parse( provider,"Samstag", "Samstag");
		KalenderEintrag.parse( provider,"Sonntag", "Sonntag");
		KalenderEintrag.parse( provider,"Tag", "Tag");
		KalenderEintrag.parse( provider,"Ostersonntag", "Ostersonntag");
		KalenderEintrag.parse( provider,"Bereich1",
				"Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>");
		KalenderEintrag.parse( provider,"Bereich2",
				"Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>");
		KalenderEintrag.parse( provider,"Bereich3", "Bereich3:=<15.01.2008-15.02.2008>");
		KalenderEintrag.parse( provider,"Bereich4",
				"Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})");

	}
	
	@Test
	public void testParseSystemkalenderEintrag() {
		try {
			assertNotNull("Test 1", KalenderEintrag.parse( provider,"Ske1", "Montag+1Tag"));
			assertNotNull("Test 2", KalenderEintrag.parse( provider,"Ske2", "Dienstag-1Tag"));
			assertNotNull("Test 3", KalenderEintrag.parse( provider,"Ske3", "Mittwoch+1Tag"));
			assertNotNull("Test 4", KalenderEintrag.parse( provider,"Ske4", "Donnerstag+2Tage"));
			assertNotNull("Test 5", KalenderEintrag.parse( provider,"Ske5", "UND{Bereich1,Bereich2}*,*"));
			assertNotNull("Test 6", KalenderEintrag.parse( provider,"Ske6", "ODER{Bereich1,Bereich2}*,*"));
			assertNotNull("Test 7", KalenderEintrag.parse( provider,"Ske7", "ODER{Bereich1,NICHT Bereich2}*,*"));
			assertNotNull("Test 8", KalenderEintrag.parse( provider,"Ske8", "UND{NICHT Bereich1,Bereich2}*,2008"));
			assertNotNull("Test 9", KalenderEintrag.parse( provider,"Ske9", "ODER{Bereich4,Bereich2}*,*"));
			assertNotNull("Test 10", KalenderEintrag.parse( provider,"Ske10", "UND{Bereich3,Bereich1}*,*"));
			assertNotNull("Test 11", KalenderEintrag.parse( provider,"Ske11", "UND{Ske5,Ske6}*,*"));

			assertNull("Test 12", KalenderEintrag.parse( provider,"Ske12", "Sontag+1Tag"));
			assertNull("Test 13", KalenderEintrag.parse( provider,"Ske13", "Dienstag2-1Tag"));
			assertNull("Test 14", KalenderEintrag.parse( provider,"Ske14", "aMittwoch+1Tag"));
			assertNull("Test 15", KalenderEintrag.parse( provider,"Ske15", "OTER{Bereich1,Bereich2}*,*"));
			assertNull("Test 16", KalenderEintrag.parse( provider,"Ske16", "ODER{BereichXyz,Bereich2}*,*"));
			assertNull("Test 17", KalenderEintrag.parse( provider,"Ske17", "ODER{Bereich4,Bereich2*,*"));
			assertNull("Test 18", KalenderEintrag.parse( provider,"Ske18", "Ske15"));
			assertNull("Test 19", KalenderEintrag.parse( provider,"Ske19", "Ske16+1Tag"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}

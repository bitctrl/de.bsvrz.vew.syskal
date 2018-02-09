package de.bsvrz.vew.syskal;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestWechsel {
	LocalDateTime datum;
	boolean status;
	
	private TestWechsel(String datum, boolean status) {
		this.datum = LocalDateTime.parse(datum, DateTimeFormatter.ofPattern("d.M.y H[:m[:s[.SSS]]]"));
		this.status = status;
	}

	public static TestWechsel of(String datum, boolean status) {
		return new TestWechsel(datum, status);
	}
	
	public static void pruefeWechsel(TestWechsel[] erwarteteWechsel, List<ZustandsWechsel> zustandsWechsel) {
		
		assertEquals("Erwartete Zustandswechsel", erwarteteWechsel.length, zustandsWechsel.size());
		
		for (int index = 0; index < zustandsWechsel.size(); index++) {

			ZustandsWechsel wechsel = zustandsWechsel.get(index);
			TestWechsel erwartet = erwarteteWechsel[index];

			assertEquals("Wechselzeit: " + index, erwartet.datum, wechsel.getZeitPunkt());
			assertEquals("Zustand: " + index, erwartet.status, wechsel.isWirdGueltig());
		}
	}
}

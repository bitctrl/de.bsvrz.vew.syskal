package de.bsvrz.vew.syskal;

import java.time.LocalDateTime;

public class ZustandsWechsel {

	public static final ZustandsWechsel MIN = ZustandsWechsel.of(LocalDateTime.MIN, false);
	public static final ZustandsWechsel MAX = ZustandsWechsel.of(LocalDateTime.MAX, false);
	
	private LocalDateTime zeitPunkt;
	private boolean wirdGueltig;
	
	public static ZustandsWechsel of(LocalDateTime zeitPunkt, boolean wirdGueltig) {
		ZustandsWechsel zustandsWechsel = new ZustandsWechsel();
		zustandsWechsel.zeitPunkt = zeitPunkt;
		zustandsWechsel.wirdGueltig = wirdGueltig;
		return zustandsWechsel;
	}
	
	private ZustandsWechsel() {
		// Objekt wird nur per 'of' erzeugt
	}
	
	public LocalDateTime getZeitPunkt() {
		return zeitPunkt;
	}
	
	public boolean isWirdGueltig() {
		return wirdGueltig;
	}

	@Override
	public String toString() {
		return "ZustandsWechsel [" + zeitPunkt + ": " + wirdGueltig + "]";
	}
	
}

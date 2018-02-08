package de.bsvrz.vew.syskal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ZustandsWechsel {

	public static final ZustandsWechsel MIN = ZustandsWechsel.of(SystemKalender.MIN_DATETIME, false);
	public static final ZustandsWechsel MAX = ZustandsWechsel.of(SystemKalender.MAX_DATETIME, false);
	
	private LocalDateTime zeitPunkt;
	private boolean wirdGueltig;

	public static ZustandsWechsel of(LocalDate datum, boolean wirdGueltig) {
		ZustandsWechsel zustandsWechsel = new ZustandsWechsel();
		zustandsWechsel.zeitPunkt = LocalDateTime.of(datum, LocalTime.MIDNIGHT);
		zustandsWechsel.wirdGueltig = wirdGueltig;
		return zustandsWechsel;
	}
	
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

package de.bsvrz.vew.syskal.syskal.data;

import java.time.LocalDateTime;

public class ZustandsWechsel {

	private LocalDateTime zeitPunkt;
	private boolean wirdGueltig;
	
	public LocalDateTime getZeitPunkt() {
		return zeitPunkt;
	}
	public boolean isWirdGueltig() {
		return wirdGueltig;
	}
	
}

package de.bsvrz.vew.syskal.internal;

import java.time.LocalDateTime;

import de.bsvrz.vew.syskal.ZustandsWechsel;

public class ZustandsWechselImpl implements ZustandsWechsel {

	public static final ZustandsWechselImpl MIN = ZustandsWechselImpl.of(LocalDateTime.MIN, false);
	public static final ZustandsWechselImpl MAX = ZustandsWechselImpl.of(LocalDateTime.MAX, false);
	
	private LocalDateTime zeitPunkt;
	private boolean wirdGueltig;
	
	public static ZustandsWechselImpl of(LocalDateTime zeitPunkt, boolean wirdGueltig) {
		ZustandsWechselImpl zustandsWechsel = new ZustandsWechselImpl();
		zustandsWechsel.zeitPunkt = zeitPunkt;
		zustandsWechsel.wirdGueltig = wirdGueltig;
		return zustandsWechsel;
	}
	
	private ZustandsWechselImpl() {
		// Objekt wird nur per 'of' erzeugt
	}
	
	@Override
	public LocalDateTime getZeitPunkt() {
		return zeitPunkt;
	}
	
	@Override
	public boolean isWirdGueltig() {
		return wirdGueltig;
	}

	@Override
	public String toString() {
		return "ZustandsWechsel [" + zeitPunkt + ": " + wirdGueltig + "]";
	}
	
}

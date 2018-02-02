package de.bsvrz.vew.syskal.internal;

import de.bsvrz.vew.syskal.Gueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class GueltigkeitImpl implements Gueltigkeit {
	
	public static final GueltigkeitImpl NICHT_GUELTIG = GueltigkeitImpl.of(false, ZustandsWechselImpl.MAX);
	
	boolean zeitlichGueltig; 
	ZustandsWechsel naechsterWechsel = ZustandsWechselImpl.MIN;
	
	private GueltigkeitImpl() {
	}

	public static GueltigkeitImpl of(boolean zustand, ZustandsWechsel wechsel) {
		GueltigkeitImpl gueltigkeit = new GueltigkeitImpl();
		gueltigkeit.zeitlichGueltig = zustand;
		gueltigkeit.naechsterWechsel = wechsel;
		return gueltigkeit;
	}
	
	@Override
	public ZustandsWechsel getNaechsterWechsel() {
		return naechsterWechsel;
	}
	
	@Override
	public boolean isZeitlichGueltig() {
		return zeitlichGueltig;
	}
}

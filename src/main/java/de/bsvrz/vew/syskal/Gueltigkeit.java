package de.bsvrz.vew.syskal;

public class Gueltigkeit {
	
	public static final Gueltigkeit NICHT_GUELTIG = Gueltigkeit.of(false, ZustandsWechsel.MAX);
	
	boolean zeitlichGueltig; 
	ZustandsWechsel naechsterWechsel = ZustandsWechsel.MIN;
	
	private Gueltigkeit() {
	}

	public static Gueltigkeit of(boolean zustand, ZustandsWechsel wechsel) {
		Gueltigkeit gueltigkeit = new Gueltigkeit();
		gueltigkeit.zeitlichGueltig = zustand;
		gueltigkeit.naechsterWechsel = wechsel;
		return gueltigkeit;
	}
	
	public ZustandsWechsel getNaechsterWechsel() {
		return naechsterWechsel;
	}
	
	public boolean isZeitlichGueltig() {
		return zeitlichGueltig;
	}
}

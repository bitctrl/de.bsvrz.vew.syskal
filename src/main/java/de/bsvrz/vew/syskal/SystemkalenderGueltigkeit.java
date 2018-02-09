package de.bsvrz.vew.syskal;

public class SystemkalenderGueltigkeit {
	
	public static final SystemkalenderGueltigkeit NICHT_GUELTIG = SystemkalenderGueltigkeit.of(ZustandsWechsel.MIN, ZustandsWechsel.MAX);
	
	ZustandsWechsel ersterWechsel = ZustandsWechsel.MIN;
	ZustandsWechsel naechsterWechsel = ZustandsWechsel.MAX;
	
	private SystemkalenderGueltigkeit() {
	}

	public static SystemkalenderGueltigkeit of(ZustandsWechsel beginn, ZustandsWechsel wechsel) {
		SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
		gueltigkeit.ersterWechsel = beginn;
		gueltigkeit.naechsterWechsel = wechsel;
		return gueltigkeit;
	}
	
	public ZustandsWechsel getErsterWechsel() {
		return ersterWechsel;
	}

	public ZustandsWechsel getNaechsterWechsel() {
		return naechsterWechsel;
	}
	
	public boolean isZeitlichGueltig() {
		return ersterWechsel.isWirdGueltig();
	}
	
	@Override
	public String toString() {
		return ersterWechsel + " --> " + naechsterWechsel;
	}
}

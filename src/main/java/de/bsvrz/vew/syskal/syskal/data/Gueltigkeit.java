package de.bsvrz.vew.syskal.syskal.data;

public class Gueltigkeit {

	private ZustandsWechsel beginn;
	private ZustandsWechsel naechsteAenderung;

	public static final Gueltigkeit NICHT_GUELTIG = Gueltigkeit.of(ZustandsWechsel.MIN, ZustandsWechsel.MAX);
	
	public static Gueltigkeit of(ZustandsWechsel beginn, ZustandsWechsel naechsteAenderung) {
		Gueltigkeit gueltigkeit = new Gueltigkeit();
		gueltigkeit.beginn = beginn;
		gueltigkeit.naechsteAenderung = naechsteAenderung;
		return gueltigkeit;
	}

	private Gueltigkeit() {
		// Objekt wird nur per 'of' erzeugt
	}
	
	public ZustandsWechsel getBeginn() {
		return beginn;
	}
	public ZustandsWechsel getNaechsteAenderung() {
		return naechsteAenderung;
	}

	@Override
	public String toString() {
		return "Gueltigkeit [beginn=" + beginn + ", naechsteAenderung=" + naechsteAenderung
				+ "]";
	}
}

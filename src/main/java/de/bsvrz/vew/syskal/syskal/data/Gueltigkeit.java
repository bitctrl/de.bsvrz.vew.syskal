package de.bsvrz.vew.syskal.syskal.data;

import java.time.LocalDateTime;

public class Gueltigkeit {

	private LocalDateTime zeitPunkt;
	private ZustandsWechsel beginn;
	private ZustandsWechsel naechsteAenderung;

	public Gueltigkeit(LocalDateTime zeitPunkt) {
		this.zeitPunkt = zeitPunkt;
	}

	public ZustandsWechsel getBeginn() {
		return beginn;
	}
	public ZustandsWechsel getNaechsteAenderung() {
		return naechsteAenderung;
	}

	public LocalDateTime getZeitPunkt() {
		return zeitPunkt;
	}
}

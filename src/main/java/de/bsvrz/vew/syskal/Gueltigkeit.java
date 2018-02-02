package de.bsvrz.vew.syskal;

public interface Gueltigkeit {
	
	ZustandsWechsel getNaechsterWechsel();
	boolean isZeitlichGueltig();
}

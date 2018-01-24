package de.bsvrz.vew.syskal.syskal.data;

public interface KalenderEintragProvider {
	
	void addEintrag(String name, KalenderEintrag eintrag);
	KalenderEintrag getEintrag(String name);
}

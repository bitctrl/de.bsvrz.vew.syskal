package de.bsvrz.vew.syskal.syskal.data;

import java.time.LocalDateTime;
import java.util.Objects;

import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.SystemkalenderEintrag;

public class SystemKalenderEintragImpl implements SystemkalenderEintrag {

	private KalenderEintragDefinition kalenderEintrag;
	private KalenderEintragProvider provider;
	private DynamicObject systemObject;
	private String originalDefinition;

	public SystemKalenderEintragImpl(KalenderEintragProvider provider, DynamicObject obj) {
		this.provider = provider;
		this.systemObject = obj;
		kalenderEintrag = VorDefinierterEintrag.getEintrag(systemObject.getName());
	}

	void bestimmeKalendereintrag() {
		String name = systemObject.getName();
		kalenderEintrag = KalenderEintragDefinition.parse(provider, name, originalDefinition);
	}

	public KalenderEintragDefinition getKalenderEintrag() {
		return kalenderEintrag;
	}

	public SystemObject getSystemObject() {
		return systemObject;
	}

	void setDefinition(String text) {
		if (!Objects.equals(originalDefinition, text)) {
			originalDefinition = text;
			bestimmeKalendereintrag();
		}
	}

	@Override
	public String getName() {
		return systemObject.getName();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(100);
		builder.append(systemObject.getName());
		builder.append(':');
		
		if (kalenderEintrag == null) {
			builder.append("NULL  :");
		} else if (kalenderEintrag.isFehler()) {
			builder.append("FEHLER :"); 
		} else {
			builder.append("OK    :"); 
		}
		builder.append(kalenderEintrag);
		return builder.toString();
	}

	@Override
	public Gueltigkeit getGueltigkeit(LocalDateTime zeitPunkt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ZustandsWechsel getZustandsWechsel(LocalDateTime von, LocalDateTime bis) {
		// TODO Auto-generated method stub
		return null;
	}
}

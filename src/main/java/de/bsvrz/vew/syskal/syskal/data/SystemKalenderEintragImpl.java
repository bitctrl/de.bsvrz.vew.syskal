package de.bsvrz.vew.syskal.syskal.data;

import java.time.LocalDateTime;

import com.google.common.base.Objects;

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
		kalenderEintrag = VorDefinierterEintrag.getEintrag(name);
		if (kalenderEintrag == null && originalDefinition != null) {
			final String[] parts = originalDefinition.split(":=");

			if (parts.length < 2) {
				kalenderEintrag = KalenderEintragDefinition.parse(provider, name, parts[0].trim());
			} else {
				final String defName = parts[0].trim();
				if (!defName.equals(name)) {
					Debug.getLogger().warning("Für den Systemkalendereintrag " + systemObject
							+ " ist der abweichende Name: \"" + name + "\" definiert!");
				}
				kalenderEintrag = KalenderEintragDefinition.parse(provider, name, parts[1].trim());
			}
		}
	}

	public KalenderEintragDefinition getKalenderEintrag() {
		return kalenderEintrag;
	}

	public SystemObject getSystemObject() {
		return systemObject;
	}

	void setDefinition(String text) {
		if (!Objects.equal(originalDefinition, text)) {
			originalDefinition = text;
			bestimmeKalendereintrag();
		}
	}

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
	public ZustandsWechsel getZustandWechsel(LocalDateTime von, LocalDateTime bis) {
		// TODO Auto-generated method stub
		return null;
	}
}

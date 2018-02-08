package de.bsvrz.vew.syskal.internal;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class SystemKalenderEintragImpl implements SystemkalenderEintrag {

	private KalenderEintrag kalenderEintrag;
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
		if (originalDefinition == null) {
			kalenderEintrag = new Undefined();
		} else {
			kalenderEintrag = KalenderEintrag.parse(provider, name, originalDefinition);
		}
	}

	public KalenderEintrag getKalenderEintrag() {
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
	public List<ZustandsWechsel> getZustandsWechsel(LocalDateTime von, LocalDateTime bis) {
		if ((kalenderEintrag == null) || kalenderEintrag.isFehler()) {
			return Collections.emptyList();
		}
		return kalenderEintrag.getZustandsWechselImBereich(von, bis);
	}

	@Override
	public boolean isGueltig(LocalDateTime zeitPunkt) {
		if ((kalenderEintrag == null) || kalenderEintrag.isFehler()) {
			return false;
		}
		return kalenderEintrag.getZeitlicheGueltigkeit(zeitPunkt).isZeitlichGueltig();
	}

	@Override
	public ZustandsWechsel getNaechstenWechsel(LocalDateTime zeitPunkt) {
		if ((kalenderEintrag == null) || kalenderEintrag.isFehler()) {
			return ZustandsWechsel.MAX;
		}
		return kalenderEintrag.getZeitlicheGueltigkeit(zeitPunkt).getNaechsterWechsel();
	}

	@Override
	public SystemkalenderGueltigkeit getGueltigkeit(LocalDateTime zeitPunkt) {
		if ((kalenderEintrag == null) || kalenderEintrag.isFehler()) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}
		return kalenderEintrag.getZeitlicheGueltigkeit(zeitPunkt);
	}

	@Override
	public SystemkalenderGueltigkeit getGueltigkeitVor(LocalDateTime zeitPunkt) {
		if ((kalenderEintrag == null) || kalenderEintrag.isFehler()) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}
		return kalenderEintrag.getZeitlicheGueltigkeitVor(zeitPunkt);
	}
}

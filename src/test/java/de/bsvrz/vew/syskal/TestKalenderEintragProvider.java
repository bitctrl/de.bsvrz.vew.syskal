package de.bsvrz.vew.syskal;

import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.vew.syskal.internal.KalenderEintrag;
import de.bsvrz.vew.syskal.internal.KalenderEintragProvider;

public final class TestKalenderEintragProvider implements KalenderEintragProvider {
	Map<String, KalenderEintrag> eintraege = new LinkedHashMap<>();

	@Override
	public KalenderEintrag getKalenderEintrag(String name) {
		return eintraege.get(name);
	}

	public void addEintrag(KalenderEintrag eintrag) {
		eintraege.put(eintrag.getName(), eintrag);
	}

	public KalenderEintrag parseAndAdd(TestKalenderEintragProvider provider, String name, String definition) {
		KalenderEintrag eintrag = KalenderEintrag.parse(provider, name, definition);
		addEintrag(eintrag);
		return eintrag;
	}
}
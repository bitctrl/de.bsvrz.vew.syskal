package de.bsvrz.vew.syskal;

import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragProvider;

public final class TestKalenderEintragProvider implements KalenderEintragProvider {
	Map<String, KalenderEintragDefinition> eintraege = new LinkedHashMap<>();

	@Override
	public KalenderEintragDefinition getKalenderEintrag(String name) {
		return eintraege.get(name);
	}

	public void addEintrag(KalenderEintragDefinition eintrag) {
		eintraege.put(eintrag.getName(), eintrag);
	}

	public KalenderEintragDefinition parseAndAdd(TestKalenderEintragProvider provider, String name, String definition) {
		KalenderEintragDefinition eintrag = KalenderEintragDefinition.parse(provider, name, definition);
		addEintrag(eintrag);
		return eintrag;
	}
}
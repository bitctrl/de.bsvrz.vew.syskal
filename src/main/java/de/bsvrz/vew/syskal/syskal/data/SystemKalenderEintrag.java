package de.bsvrz.vew.syskal.syskal.data;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.DynamicObject;

public abstract class SystemKalenderEintrag {

	static void initalize(KalenderEintragProvider provider, DynamicObject obj) {

		Data configurationData = obj
				.getConfigurationData(obj.getDataModel().getAttributeGroup("atg.systemKalenderEintrag"));
		String definition = configurationData.getTextValue("Definition").getText();

		final String[] parts = definition.split(":=");
		final String name = parts[0];

		KalenderEintrag result = VorDefinierterEintrag.getEintrag(name);
		if (result == null) {
			if (parts.length < 2) {
				KalenderEintrag.parse(provider, name, "");
			} else {
				KalenderEintrag.parse(provider, name, parts[1].trim());
			}
		}
	}
}

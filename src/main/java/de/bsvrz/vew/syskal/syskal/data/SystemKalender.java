package de.bsvrz.vew.syskal.syskal.data;

import java.util.LinkedHashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.SystemObject;

public class SystemKalender implements MutableSetChangeListener, KalenderEintragProvider {

	private Map<String, KalenderEintrag> eintraege = new LinkedHashMap<>();
	private MutableSet eintragsSet;
	private boolean valid = true;

	public SystemKalender(ClientDavInterface dav, ConfigurationObject object) {

		dav.addConnectionListener(connection->invalidate());
		
		if (!object.isOfType("typ.kalender")) {
			throw new IllegalStateException("Das Objekt " + object + " ist nicht vom Typ \"typ.kalender\"!");
		}
		
		eintragsSet = object.getMutableSet("SystemKalenderEinträge");
		eintragsSet.addChangeListener(this);
		
		for( SystemObject obj : eintragsSet.getElements()) {
			SystemKalenderEintrag.initalize(this, (DynamicObject) obj);
		}
	}

	private void invalidate() {
		if( eintragsSet != null) {
			eintragsSet.removeChangeListener(this);
			eintraege.clear();
		}
		valid  = false;
	}

	@Override
	public void update(MutableSet set, SystemObject[] addedObjects, SystemObject[] removedObjects) {
		for( SystemObject object : removedObjects) {
			eintraege.remove(object.getPid());
		}
		for( SystemObject object : addedObjects) {
			SystemKalenderEintrag.initalize(this, (DynamicObject) object);
		}
	}

	@Override
	public void addEintrag(String name, KalenderEintrag eintrag) {
		eintraege.put(name, eintrag);
	}

	@Override
	public KalenderEintrag getEintrag(String name) {
		return eintraege.get(name);
	}
}

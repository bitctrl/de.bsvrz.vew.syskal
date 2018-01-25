package de.bsvrz.vew.syskal.syskal.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.SystemKalenderException;
import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class EintragsVerwaltung implements KalenderEintragProvider, ClientReceiverInterface, MutableSetChangeListener {

	private ObservableSet<SystemkalenderEintrag> eintraege = FXCollections.observableSet(new LinkedHashSet<>());
	private Map<SystemObject, SystemKalenderEintragImpl> cache = new LinkedHashMap<>();

	private ClientDavInterface dav;
	private DataDescription dataDescription;
	private MutableSet eintragsSet;
	private boolean valid = true;

	public ObservableSet<SystemkalenderEintrag> getSystemKalenderEintraege() throws SystemKalenderException {
		if( !valid) {
			throw new SystemKalenderException();
		}
		return FXCollections.unmodifiableObservableSet(eintraege);
	}
	
	public EintragsVerwaltung(ClientDavInterface dav, ConfigurationObject kalenderObject) {
		if (!kalenderObject.isOfType("typ.kalender")) {
			throw new IllegalStateException("Das Objekt " + kalenderObject + " ist nicht vom Typ \"typ.kalender\"!");
		}
		
		this.dav = dav;
		this.dav.addConnectionListener(connection -> invalidate());

		AttributeGroup attributeGroup = dav.getDataModel().getAttributeGroup("atg.systemKalenderEintrag");
		Aspect aspect = dav.getDataModel().getAspect("asp.parameterSoll");
		dataDescription = new DataDescription(attributeGroup, aspect);
		

		eintragsSet = kalenderObject.getMutableSet("SystemKalenderEinträge");
		eintragsSet.addChangeListener(this);

		addEintraege(eintragsSet.getElements());
	}

	@Override
	public KalenderEintragDefinition getKalenderEintrag(String name) {
		Optional<SystemKalenderEintragImpl> systemKalenderEintrag = cache.values().stream().filter(s->name.equals(s.getName())).findFirst();
		if (systemKalenderEintrag.isPresent()) {
			return systemKalenderEintrag.get().getKalenderEintrag();
		}
		return null;
	}

	private void addEintraege(Collection<SystemObject> list) {
		
		Collection<SystemkalenderEintrag> neueEintrage = new ArrayList<>();
		for (SystemObject obj : list) {
			
			SystemKalenderEintragImpl neuerEintrag = new SystemKalenderEintragImpl(this, (DynamicObject) obj);
			neueEintrage.add(neuerEintrag);
			SystemKalenderEintragImpl alterEintrag = cache.put(obj, neuerEintrag);
			if (alterEintrag != null) {
				eintraege.remove(alterEintrag);
				dav.unsubscribeReceiver(this, alterEintrag.getSystemObject(), dataDescription);
			}
			dav.subscribeReceiver(this, obj, dataDescription, ReceiveOptions.normal(), ReceiverRole.receiver());
		}

		berechneKalendereintraegeNeu();
		eintraege.addAll(neueEintrage);
	}

	private void berechneKalendereintraegeNeu() {
		cache.values().stream().forEach(e -> e.bestimmeKalendereintrag());
	}

	private void invalidate() {
		cache.clear();
		eintraege.clear();
		if (eintragsSet != null) {
			eintragsSet.removeChangeListener(this);
		}
		valid  = false;
	}
	
	private void removeEintraege(Collection<SystemObject> list) {

		Collection<SystemkalenderEintrag> alteEintrage = new ArrayList<>();
		for (SystemObject object : list) {
			SystemKalenderEintragImpl alterEintrag = cache.remove(object);
			if (alterEintrag != null) {
				alteEintrage.add(alterEintrag);
				dav.unsubscribeReceiver(this, object, dataDescription);
			}
		}
		berechneKalendereintraegeNeu();
		eintraege.removeAll(alteEintrage);
	}

	@Override
	public void update(ResultData[] results) {

		for (ResultData result : results) {
			if (result.hasData()) {
				SystemKalenderEintragImpl systemKalenderEintrag = cache.get(result.getObject());
				if( systemKalenderEintrag != null) {
					systemKalenderEintrag.setDefinition(result.getData().getTextValue("Definition").getText());
				}
			}
		}
		berechneKalendereintraegeNeu();
	}
	@Override
	public void update(MutableSet set, SystemObject[] addedObjects, SystemObject[] removedObjects) {
		removeEintraege(Arrays.asList(removedObjects));
		addEintraege(Arrays.asList(addedObjects));
	}

	public SystemkalenderEintrag getSystemKalenderEintrag(SystemObject object) throws SystemKalenderException {
		if( !valid) {
			throw new SystemKalenderException();
		}
		return cache.get(object);
	}
}

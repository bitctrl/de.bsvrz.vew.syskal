package de.bsvrz.vew.syskal;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.internal.EintragsVerwaltung;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SystemKalender  {

	private final EintragsVerwaltung verwaltung;
	SimpleLongProperty syskalChangedProperty = new SimpleLongProperty(this, "syskalChanged", 0);
	
	public SystemKalender(ClientDavInterface dav, ConfigurationObject object) {
		verwaltung = new EintragsVerwaltung(dav, object);
//		syskalChangedProperty.addListener((a,b,c)->System.err.println("Hallo A: " + a + " B: " + b + " C: " + c ));
		syskalChangedProperty.addListener(a->System.err.println("Hallo: " + a));
	}
	
	public SystemkalenderEintrag getEintrag(SystemObject object) throws SystemKalenderException {
		return verwaltung.getSystemKalenderEintrag(object);
	} 

	public Collection<SystemkalenderEintrag> getEintraege() throws SystemKalenderException {
 		syskalChangedProperty.add(1);
		syskalChangedProperty.set(syskalChangedProperty.get() + 1);
		return verwaltung.getSystemKalenderEintraege();
	} 
}

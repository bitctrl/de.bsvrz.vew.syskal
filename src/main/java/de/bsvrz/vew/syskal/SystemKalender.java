package de.bsvrz.vew.syskal;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.internal.EintragsVerwaltung;
import javafx.beans.property.SimpleLongProperty;

public class SystemKalender  {

	private final EintragsVerwaltung verwaltung;
	
	public SystemKalender(ClientDavInterface dav, ConfigurationObject object) {
		verwaltung = new EintragsVerwaltung(dav, object);
	}
	
	public SystemkalenderEintrag getEintrag(SystemObject object) throws SystemKalenderException {
		return verwaltung.getSystemKalenderEintrag(object);
	} 

	public Collection<SystemkalenderEintrag> getEintraege() throws SystemKalenderException {
		return verwaltung.getSystemKalenderEintraege();
	} 
}

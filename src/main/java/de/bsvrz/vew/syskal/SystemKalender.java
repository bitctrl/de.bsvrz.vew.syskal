package de.bsvrz.vew.syskal;

import java.time.LocalDateTime;
import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.internal.EintragsVerwaltung;

public class SystemKalender  {

	public static final LocalDateTime MIN_DATETIME = LocalDateTime.of(0, 1, 1, 0, 0, 0);
	public static final LocalDateTime MAX_DATETIME = LocalDateTime.of(3000, 1, 1, 0, 0, 0);
	
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

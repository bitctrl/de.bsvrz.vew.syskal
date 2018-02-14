/*
 * SWE Systemkalender - Version 2
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49-341-49067-0
 * Fax: +49-341-49067-15
 * mailto: info@bitctrl.de
 */

package de.bsvrz.vew.syskal.internal;

import java.util.Collection;
import java.util.Objects;

import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalenderEintrag;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SystemKalenderEintragImpl implements SystemKalenderEintrag {

	private final ObjectProperty<KalenderEintrag> kalenderEintragProperty = new SimpleObjectProperty<>(this, "kalendereintrag", VorDefinierterEintrag.UNDEFINIERT);
	private KalenderEintragProvider provider;
	private DynamicObject systemObject;
	private String originalDefinition;

	public SystemKalenderEintragImpl(KalenderEintragProvider provider, DynamicObject obj) {
		this.provider = provider;
		this.systemObject = obj;
	}

	void bestimmeKalendereintrag() {
		String name = systemObject.getName();
		if (originalDefinition == null) {
			kalenderEintragProperty.set(VorDefinierterEintrag.UNDEFINIERT);
		} else {
			kalenderEintragProperty.set(KalenderEintragImpl.parse(provider, name, originalDefinition));
		}
	}

	@Override
	public KalenderEintrag getKalenderEintrag() {
		return kalenderEintragProperty.get();
	}
	
	@Override
	public SystemObject getSystemObject() {
		return systemObject;
	}

	public void setDefinition(String text) {
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

		if (((KalenderEintragImpl) kalenderEintragProperty.get()).isFehler()) {
			builder.append("FEHLER :");
		} else {
			builder.append("OK    :");
		}
		builder.append(kalenderEintragProperty.get());
		return builder.toString();
	}

	@Override
	public ObjectProperty<KalenderEintrag> getKalenderEintragProperty() {
		return kalenderEintragProperty;
	}

	public void aktualisiereVonReferenzen(Collection<SystemKalenderEintrag> referenzen) {
		for( SystemKalenderEintrag referenz  : referenzen) {
			if (referenz.equals(this)) {
				continue;
			}
			
			if( ((KalenderEintragImpl) getKalenderEintrag()).benutzt(referenz)) {
				bestimmeKalendereintrag();
				return;
			}
		}
	}
}

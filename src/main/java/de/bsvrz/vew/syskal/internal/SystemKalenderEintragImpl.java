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

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

package de.bsvrz.vew.syskal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

public class ZustandsWechsel  {

	public static final Comparator<ZustandsWechsel> ZEIT_COMPARATOR = new Comparator<ZustandsWechsel>() {

		@Override
		public int compare(ZustandsWechsel o1, ZustandsWechsel o2) {
			return o1.getZeitPunkt().compareTo(o2.getZeitPunkt());
		}
	};
	
	public static final ZustandsWechsel MIN = ZustandsWechsel.of(SystemKalender.MIN_DATETIME, false);
	public static final ZustandsWechsel MAX = ZustandsWechsel.of(SystemKalender.MAX_DATETIME, false);
	
	private LocalDateTime zeitPunkt;
	private boolean wirdGueltig;

	public static ZustandsWechsel zuGueltig(LocalDate datum) {
		return new ZustandsWechsel(LocalDateTime.of(datum, LocalTime.MIDNIGHT), true);
	}

	public static ZustandsWechsel zuGueltig(LocalDateTime zeitPunkt) {
		return new ZustandsWechsel(zeitPunkt, true);
	}

	public static ZustandsWechsel zuUnGueltig(LocalDate datum) {
		return new ZustandsWechsel(LocalDateTime.of(datum, LocalTime.MIDNIGHT), false);
	}

	public static ZustandsWechsel zuUnGueltig(LocalDateTime zeitPunkt) {
		return new ZustandsWechsel(zeitPunkt, false);
	}
	
	public static ZustandsWechsel of(LocalDateTime zeitPunkt, boolean wirdGueltig) {
		return new ZustandsWechsel(zeitPunkt, wirdGueltig);
	}
	
	public static ZustandsWechsel oof(LocalDate datum, boolean wirdGueltig) {
		return new ZustandsWechsel(LocalDateTime.of(datum, LocalTime.MIDNIGHT), wirdGueltig);
	}
	
	
	private ZustandsWechsel(LocalDateTime zeitPunkt, boolean wirdGueltig) {
		this.zeitPunkt = zeitPunkt;
		this.wirdGueltig = wirdGueltig;
	}
	
	public LocalDateTime getZeitPunkt() {
		return zeitPunkt;
	}
	
	public boolean isWirdGueltig() {
		return wirdGueltig;
	}

	@Override
	public String toString() {
		return "ZustandsWechsel [" + zeitPunkt + ": " + wirdGueltig + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(zeitPunkt, wirdGueltig);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		ZustandsWechsel other = (ZustandsWechsel) obj;
		return Objects.equals(zeitPunkt, other.zeitPunkt) && wirdGueltig == other.wirdGueltig;
	}
}

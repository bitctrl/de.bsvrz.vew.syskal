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

public class SystemkalenderGueltigkeit {
	
	public static final SystemkalenderGueltigkeit NICHT_GUELTIG = SystemkalenderGueltigkeit.of(ZustandsWechsel.MIN, ZustandsWechsel.MAX);
	
	ZustandsWechsel ersterWechsel = ZustandsWechsel.MIN;
	ZustandsWechsel naechsterWechsel = ZustandsWechsel.MAX;
	
	private SystemkalenderGueltigkeit() {
	}

	public static SystemkalenderGueltigkeit of(ZustandsWechsel beginn, ZustandsWechsel wechsel) {
		SystemkalenderGueltigkeit gueltigkeit = new SystemkalenderGueltigkeit();
		gueltigkeit.ersterWechsel = beginn;
		gueltigkeit.naechsterWechsel = wechsel;
		return gueltigkeit;
	}
	
	public ZustandsWechsel getErsterWechsel() {
		return ersterWechsel;
	}

	public ZustandsWechsel getNaechsterWechsel() {
		return naechsterWechsel;
	}
	
	public boolean isZeitlichGueltig() {
		return ersterWechsel.isWirdGueltig();
	}
	
	@Override
	public String toString() {
		return ersterWechsel + " --> " + naechsterWechsel;
	}
}

/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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
 * Dambach-Werke GmbH
 * Elektronische Leitsysteme
 * Fritz-Minhardt-Str. 1
 * 76456 Kuppenheim
 * Phone: +49-7222-402-0
 * Fax: +49-7222-402-200
 * mailto: info@els.dambach.de
 */

package de.bsvrz.vew.syskal;

import java.time.LocalDateTime;

import de.bsvrz.vew.syskal.syskal.data.Gueltigkeit;
import de.bsvrz.vew.syskal.syskal.data.KalenderEintragDefinition;
import de.bsvrz.vew.syskal.syskal.data.ZustandsWechsel;

/**
 * Schnittstelle zum Erzeugen von SystemKalenderEinträgen. Bietet einen
 * einheitlichen Zugriff auf alle Typen von SystemKalendereinträgen *
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public interface SystemkalenderEintrag {
	Gueltigkeit getGueltigkeit(LocalDateTime zeitPunkt);
	ZustandsWechsel getZustandsWechsel(LocalDateTime von, LocalDateTime bis);
	String getName();
}

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

import java.time.LocalDateTime;

public class Intervall {

    public static Intervall of(LocalDateTime von, LocalDateTime bis) {
        return new Intervall(von, bis);
    }

    private LocalDateTime start;
    private LocalDateTime ende;

    private Intervall(LocalDateTime start, LocalDateTime ende) {
        super();
        this.start = start;
        this.ende = ende;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnde() {
        return ende;
    }
}

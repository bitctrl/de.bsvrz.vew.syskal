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

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestIntervall {

    private static final DateTimeFormatter DATE_PARSER = DateTimeFormatter.ofPattern("d.M.y H[:m[:s[.SSS]]]");

    public static Intervall of(String start, String ende) {
        return Intervall.of(LocalDateTime.parse(start, DATE_PARSER), LocalDateTime.parse(ende, DATE_PARSER));
    }

    public static void pruefeIntervalle(Intervall[] erwarteteIntervalle, List<Intervall> intervalle) {
        assertEquals("Erwartete Intervalle", erwarteteIntervalle.length, intervalle.size());

        for (int index = 0; index < intervalle.size(); index++) {
            assertEquals(index + ": Intervall-Anfang", erwarteteIntervalle[index].getStart(),
                    intervalle.get(index).getStart());
            assertEquals(index + ": Intervall-Ende", erwarteteIntervalle[index].getEnde(),
                    intervalle.get(index).getEnde());
        }
    }
}

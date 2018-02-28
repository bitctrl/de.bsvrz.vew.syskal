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
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class VordefinierterEintragTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void testeZustandsWechsel() {

        TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
        VorDefinierterEintrag mittwoch = (VorDefinierterEintrag) KalenderEintrag.parse(provider, "Mittwoch",
                "Mittwoch");

        LocalDateTime start = LocalDateTime.of(2018, 1, 24, 12, 10);
        LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 11, 10);

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("24.01.2018 00:00", true),
                TestWechsel.of("25.01.2018 00:00", false),
                TestWechsel.of("31.01.2018 00:00", true),
                TestWechsel.of("01.02.2018 00:00", false),
                TestWechsel.of("07.02.2018 00:00", true),
                TestWechsel.of("08.02.2018 00:00", false),
                TestWechsel.of("14.02.2018 00:00", true),
                TestWechsel.of("15.02.2018 00:00", false),
                TestWechsel.of("21.02.2018 00:00", true),
                TestWechsel.of("22.02.2018 00:00", false),
                TestWechsel.of("28.02.2018 00:00", true)
        };

        List<ZustandsWechsel> zustandsWechsel = mittwoch.getZustandsWechsel(start, ende);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalle() {

        TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
        VorDefinierterEintrag mittwoch = (VorDefinierterEintrag) KalenderEintrag.parse(provider, "Mittwoch",
                "Mittwoch");

        LocalDateTime start = LocalDateTime.of(2018, 1, 24, 12, 10);
        LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 11, 10);

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("24.01.2018 12:10", "25.01.2018 00:00"),
                TestIntervall.of("31.01.2018 00:00", "01.02.2018 00:00"),
                TestIntervall.of("07.02.2018 00:00", "08.02.2018 00:00"),
                TestIntervall.of("14.02.2018 00:00", "15.02.2018 00:00"),
                TestIntervall.of("21.02.2018 00:00", "22.02.2018 00:00"),
                TestIntervall.of("28.02.2018 00:00", "28.02.2018 11:10")
        };

        List<Intervall> intervalle = mittwoch.getIntervalle(start, ende);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

}

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

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class OderVerknuepfungTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider provider = new TestKalenderEintragProvider();
    private static OderVerknuepfung verknuepfung;

    @BeforeClass
    public static void init() {
        verknuepfung = (OderVerknuepfung) provider.parseAndAdd(provider, "MontagOderMittwoch",
                "ODER{Montag, Mittwoch}");
    }

    @Test
    public void testeZustandswechsel() {

        LocalDateTime start = LocalDateTime.of(2018, 5, 1, 0, 0);
        LocalDateTime ende = LocalDateTime.of(2018, 6, 1, 0, 0);

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("1.5.2018 00:00", false),
                TestWechsel.of("2.5.2018 00:00", true),
                TestWechsel.of("3.5.2018 00:00", false),
                TestWechsel.of("7.5.2018 00:00", true),
                TestWechsel.of("8.5.2018 00:00", false),
                TestWechsel.of("9.5.2018 00:00", true),
                TestWechsel.of("10.5.2018 00:00", false),
                TestWechsel.of("14.5.2018 00:00", true),
                TestWechsel.of("15.5.2018 00:00", false),
                TestWechsel.of("16.5.2018 00:00", true),
                TestWechsel.of("17.5.2018 00:00", false),
                TestWechsel.of("21.5.2018 00:00", true),
                TestWechsel.of("22.5.2018 00:00", false),
                TestWechsel.of("23.5.2018 00:00", true),
                TestWechsel.of("24.5.2018 00:00", false),
                TestWechsel.of("28.5.2018 00:00", true),
                TestWechsel.of("29.5.2018 00:00", false),
                TestWechsel.of("30.5.2018 00:00", true),
                TestWechsel.of("31.5.2018 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechselImBereich = verknuepfung.getZustandsWechsel(start, ende);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechselImBereich);
    }

    @Test
    public void testeIntervalle() {

        LocalDateTime start = LocalDateTime.of(2018, 5, 2, 11, 11);
        LocalDateTime ende = LocalDateTime.of(2018, 6, 1, 0, 0);

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("02.05.2018 11:11", "03.05.2018 00:00"),
                TestIntervall.of("07.05.2018 00:00", "08.05.2018 00:00"),
                TestIntervall.of("09.05.2018 00:00", "10.05.2018 00:00"),
                TestIntervall.of("14.05.2018 00:00", "15.05.2018 00:00"),
                TestIntervall.of("16.05.2018 00:00", "17.05.2018 00:00"),
                TestIntervall.of("21.05.2018 00:00", "22.05.2018 00:00"),
                TestIntervall.of("23.05.2018 00:00", "24.05.2018 00:00"),
                TestIntervall.of("28.05.2018 00:00", "29.05.2018 00:00"),
                TestIntervall.of("30.05.2018 00:00", "31.05.2018 00:00"),
        };

        List<Intervall> intervalle = verknuepfung.getIntervalle(start, ende);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

}

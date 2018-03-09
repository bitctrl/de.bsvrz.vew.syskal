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
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Prüffall 5 - Zeitbereichseintrag mit Zeitpunkten - Gültigkeitsintervalle und
 * Wechsel.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ZeitbereichseintragMitZeitPunktenTest {

     @Rule
     public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider eintragsProvider;

    private static LocalDateTime startTime;

    private static LocalDateTime endTime;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.parseAndAdd(eintragsProvider, "DatumZP",
                "<15.07.2017 10:30:00,000 - 07.09.2017 17:45:00,000>");

        startTime = LocalDateTime.of(2017, 7, 1, 12, 0);
        endTime = LocalDateTime.of(2018, 10, 1, 18, 0);
    }

    @Test
    public void testeZustandswechsel() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("DatumZP");

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("01.01.1000 00:00:00", false),
                TestWechsel.of("15.07.2017 10:30:00", true),
                TestWechsel.of("07.09.2017 17:45:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalle() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("DatumZP");

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("15.07.2017 10:30:00", "07.09.2017 17:45:00")
        };

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }
}

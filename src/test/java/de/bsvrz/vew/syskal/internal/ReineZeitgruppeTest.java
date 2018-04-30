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
 * Prüffall 7 - Reine Zeitgruppe - Gültigkeitsintervalle und Wechsel.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class ReineZeitgruppeTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider eintragsProvider;

    private static LocalDateTime startTime;

    private static LocalDateTime endTime;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.parseAndAdd(eintragsProvider, "BereichZG", "({11:55:00,000-12:25:00,000})");

        startTime = LocalDateTime.of(2020, 5, 23, 8, 23);
        endTime = LocalDateTime.of(2020, 5, 26, 12, 2);
    }

    @Test
    public void testeZustandswechsel() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("BereichZG");

        TestWechsel[] erwarteteWechsel = {
                
                TestWechsel.of("22.05.2020 12:25:00", false),
                TestWechsel.of("23.05.2020 11:55:00", true),
                TestWechsel.of("23.05.2020 12:25:00", false),
                TestWechsel.of("24.05.2020 11:55:00", true),
                TestWechsel.of("24.05.2020 12:25:00", false),
                TestWechsel.of("25.05.2020 11:55:00", true),
                TestWechsel.of("25.05.2020 12:25:00", false),
                TestWechsel.of("26.05.2020 11:55:00", true)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalle() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("BereichZG");

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("23.05.2020 11:55" , "23.05.2020 12:25"),
                TestIntervall.of("24.05.2020 11:55" , "24.05.2020 12:25"),
                TestIntervall.of("25.05.2020 11:55" , "25.05.2020 12:25"),
                TestIntervall.of("26.05.2020 11:55" , "26.05.2020 12:02")
        };

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }
}

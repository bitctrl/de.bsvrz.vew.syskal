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

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class OstersonntagTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider provider;
    private static Ostersonntag osterSonntag;

    @BeforeClass
    public static void init() {
        provider = new TestKalenderEintragProvider();
        osterSonntag = (Ostersonntag) provider.getKalenderEintrag("Ostersonntag");
    }

    @Test
    public void testeGetDatumImJahr() {

        String[] osterDatumAb2000 = {
                "23.4.2000",
                "15.4.2001",
                "31.3.2002",
                "20.4.2003",
                "11.4.2004",
                "27.3.2005",
                "16.4.2006",
                "8.4.2007",
                "23.3.2008",
                "12.4.2009",
                "4.4.2010",
                "24.4.2011",
                "8.4.2012",
                "31.3.2013",
                "20.4.2014",
                "5.4.2015",
                "27.3.2016",
                "16.4.2017",
                "1.4.2018",
                "21.4.2019",
                "12.4.2020" };

        for (int jahr = 2000; jahr < 2020; jahr++) {
            LocalDate date = Ostersonntag.getDatumImJahr(jahr);
            LocalDate pruefDatum = LocalDate.parse(osterDatumAb2000[jahr - 2000], DateTimeFormatter.ofPattern("d.M.u"));
            assertEquals("Osterdatum " + jahr, pruefDatum, date);
        }
    }

    @Test
    public void testeZustandsWechsel() {

        LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
        LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("21.4.2014 00:00", false),
                TestWechsel.of("5.4.2015 00:00", true),
                TestWechsel.of("6.4.2015 00:00", false),
                TestWechsel.of("27.3.2016 00:00", true),
                TestWechsel.of("28.3.2016 00:00", false),
                TestWechsel.of("16.4.2017 00:00", true),
                TestWechsel.of("17.4.2017 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechselImBereich = osterSonntag.getZustandsWechsel(start, ende);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechselImBereich);
    }

    @Test
    public void testeIntervalle() {

        LocalDateTime start = LocalDateTime.of(2015, 1, 1, 0, 0);
        LocalDateTime ende = LocalDateTime.of(2018, 2, 28, 12, 0);

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("05.04.2015 00:00", "06.04.2015 00:00"),
                TestIntervall.of("27.03.2016 00:00", "28.03.2016 00:00"),
                TestIntervall.of("16.04.2017 00:00", "17.04.2017 00:00")
        };

        List<Intervall> intervalle = osterSonntag.getIntervalle(start, ende);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

}

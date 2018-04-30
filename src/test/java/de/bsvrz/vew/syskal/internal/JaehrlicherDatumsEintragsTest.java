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
 * Prüffall 3 - Jährlicher Datumseintrag - Gültigkeitsintervalle und Wechsel.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class JaehrlicherDatumsEintragsTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    
    private static TestKalenderEintragProvider eintragsProvider;

    private static LocalDateTime startTime;

    private static LocalDateTime endTime;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.parseAndAdd(eintragsProvider, "Mai1", "01.05.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mai1unten", "01.05.2015,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mai1oben", "01.05.*,2017");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mai1beide", "01.05.2015,2017");

        startTime = LocalDateTime.of(2013, 2, 1, 12, 10);
        endTime = LocalDateTime.of(2019, 7, 31, 23, 52);

    }
    
    @Test
    public void testeZustandswechselMai1() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1");
                
        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("2.5.2012 00:00", false),
                TestWechsel.of("1.5.2013 00:00", true),
                TestWechsel.of("2.5.2013 00:00", false),
                TestWechsel.of("1.5.2014 00:00", true),
                TestWechsel.of("2.5.2014 00:00", false),
                TestWechsel.of("1.5.2015 00:00", true),
                TestWechsel.of("2.5.2015 00:00", false),
                TestWechsel.of("1.5.2016 00:00", true),
                TestWechsel.of("2.5.2016 00:00", false),
                TestWechsel.of("1.5.2017 00:00", true),
                TestWechsel.of("2.5.2017 00:00", false),
                TestWechsel.of("1.5.2018 00:00", true),
                TestWechsel.of("2.5.2018 00:00", false),
                TestWechsel.of("1.5.2019 00:00", true),
                TestWechsel.of("2.5.2019 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalleMai1() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1");

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("01.05.2013 00:00", "02.05.2013 00:00"),
                TestIntervall.of("01.05.2014 00:00", "02.05.2014 00:00"),
                TestIntervall.of("01.05.2015 00:00", "02.05.2015 00:00"),
                TestIntervall.of("01.05.2016 00:00", "02.05.2016 00:00"),
                TestIntervall.of("01.05.2017 00:00", "02.05.2017 00:00"),
                TestIntervall.of("01.05.2018 00:00", "02.05.2018 00:00"),
                TestIntervall.of("01.05.2019 00:00", "02.05.2019 00:00")
        };

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }
    
    @Test
    public void testeZustandswechselMai1unten() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1unten");
                
        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("1.1.1000 00:00", false),
                TestWechsel.of("1.5.2015 00:00", true),
                TestWechsel.of("2.5.2015 00:00", false),
                TestWechsel.of("1.5.2016 00:00", true),
                TestWechsel.of("2.5.2016 00:00", false),
                TestWechsel.of("1.5.2017 00:00", true),
                TestWechsel.of("2.5.2017 00:00", false),
                TestWechsel.of("1.5.2018 00:00", true),
                TestWechsel.of("2.5.2018 00:00", false),
                TestWechsel.of("1.5.2019 00:00", true),
                TestWechsel.of("2.5.2019 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalleMai1unten() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1unten");

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("01.05.2015 00:00", "02.05.2015 00:00"),
                TestIntervall.of("01.05.2016 00:00", "02.05.2016 00:00"),
                TestIntervall.of("01.05.2017 00:00", "02.05.2017 00:00"),
                TestIntervall.of("01.05.2018 00:00", "02.05.2018 00:00"),
                TestIntervall.of("01.05.2019 00:00", "02.05.2019 00:00")
        };

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

    @Test
    public void testeZustandswechselMai1oben() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1oben");
                
        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("2.5.2012 00:00", false),
                TestWechsel.of("1.5.2013 00:00", true),
                TestWechsel.of("2.5.2013 00:00", false),
                TestWechsel.of("1.5.2014 00:00", true),
                TestWechsel.of("2.5.2014 00:00", false),
                TestWechsel.of("1.5.2015 00:00", true),
                TestWechsel.of("2.5.2015 00:00", false),
                TestWechsel.of("1.5.2016 00:00", true),
                TestWechsel.of("2.5.2016 00:00", false),
                TestWechsel.of("1.5.2017 00:00", true),
                TestWechsel.of("2.5.2017 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalleMai1oben() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1oben");

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("01.05.2013 00:00", "02.05.2013 00:00"),
                TestIntervall.of("01.05.2014 00:00", "02.05.2014 00:00"),
                TestIntervall.of("01.05.2015 00:00", "02.05.2015 00:00"),
                TestIntervall.of("01.05.2016 00:00", "02.05.2016 00:00"),
                TestIntervall.of("01.05.2017 00:00", "02.05.2017 00:00")
        };

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

    @Test
    public void testeZustandswechselMai1beide() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1beide");
                
        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("1.1.1000 00:00", false),
                TestWechsel.of("1.5.2015 00:00", true),
                TestWechsel.of("2.5.2015 00:00", false),
                TestWechsel.of("1.5.2016 00:00", true),
                TestWechsel.of("2.5.2016 00:00", false),
                TestWechsel.of("1.5.2017 00:00", true),
                TestWechsel.of("2.5.2017 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalleMai1beide() {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Mai1beide");

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("01.05.2015 00:00", "02.05.2015 00:00"),
                TestIntervall.of("01.05.2016 00:00", "02.05.2016 00:00"),
                TestIntervall.of("01.05.2017 00:00", "02.05.2017 00:00")
        };

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

    
    
}

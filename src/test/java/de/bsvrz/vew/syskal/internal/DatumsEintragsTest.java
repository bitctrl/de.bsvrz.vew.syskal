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

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class DatumsEintragsTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void testeZustandswechselTagDerArbeit() {

        TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
        KalenderEintragImpl datumsEintrag = KalenderEintragImpl.parse(eintragsProvider, "Mai1", "1.5.*,*");

        LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 14, 27, 17);
        LocalDateTime endTime = LocalDateTime.of(2020, 3, 14, 14, 28, 17);

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("2.5.2000 00:00", false),
                TestWechsel.of("1.5.2001 00:00", true),
                TestWechsel.of("2.5.2001 00:00", false),
                TestWechsel.of("1.5.2002 00:00", true),
                TestWechsel.of("2.5.2002 00:00", false),
                TestWechsel.of("1.5.2003 00:00", true),
                TestWechsel.of("2.5.2003 00:00", false),
                TestWechsel.of("1.5.2004 00:00", true),
                TestWechsel.of("2.5.2004 00:00", false),
                TestWechsel.of("1.5.2005 00:00", true),
                TestWechsel.of("2.5.2005 00:00", false),
                TestWechsel.of("1.5.2006 00:00", true),
                TestWechsel.of("2.5.2006 00:00", false),
                TestWechsel.of("1.5.2007 00:00", true),
                TestWechsel.of("2.5.2007 00:00", false),
                TestWechsel.of("1.5.2008 00:00", true),
                TestWechsel.of("2.5.2008 00:00", false),
                TestWechsel.of("1.5.2009 00:00", true),
                TestWechsel.of("2.5.2009 00:00", false),
                TestWechsel.of("1.5.2010 00:00", true),
                TestWechsel.of("2.5.2010 00:00", false),
                TestWechsel.of("1.5.2011 00:00", true),
                TestWechsel.of("2.5.2011 00:00", false),
                TestWechsel.of("1.5.2012 00:00", true),
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

        List<ZustandsWechsel> zustandsWechsel = datumsEintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalleErsterMai() {

        TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
        KalenderEintragImpl datumsEintrag = KalenderEintragImpl.parse(eintragsProvider, "Mai1", "1.5.*,*");

        LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 14, 27, 17);
        LocalDateTime endTime = LocalDateTime.of(2020, 3, 14, 14, 28, 17);

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("01.05.2001 00:00", "02.05.2001 00:00"),
                TestIntervall.of("01.05.2002 00:00", "02.05.2002 00:00"),
                TestIntervall.of("01.05.2003 00:00", "02.05.2003 00:00"),
                TestIntervall.of("01.05.2004 00:00", "02.05.2004 00:00"),
                TestIntervall.of("01.05.2005 00:00", "02.05.2005 00:00"),
                TestIntervall.of("01.05.2006 00:00", "02.05.2006 00:00"),
                TestIntervall.of("01.05.2007 00:00", "02.05.2007 00:00"),
                TestIntervall.of("01.05.2008 00:00", "02.05.2008 00:00"),
                TestIntervall.of("01.05.2009 00:00", "02.05.2009 00:00"),
                TestIntervall.of("01.05.2010 00:00", "02.05.2010 00:00"),
                TestIntervall.of("01.05.2011 00:00", "02.05.2011 00:00"),
                TestIntervall.of("01.05.2012 00:00", "02.05.2012 00:00"),
                TestIntervall.of("01.05.2013 00:00", "02.05.2013 00:00"),
                TestIntervall.of("01.05.2014 00:00", "02.05.2014 00:00"),
                TestIntervall.of("01.05.2015 00:00", "02.05.2015 00:00"),
                TestIntervall.of("01.05.2016 00:00", "02.05.2016 00:00"),
                TestIntervall.of("01.05.2017 00:00", "02.05.2017 00:00"),
                TestIntervall.of("01.05.2018 00:00", "02.05.2018 00:00"),
                TestIntervall.of("01.05.2019 00:00", "02.05.2019 00:00")
        };

        List<Intervall> intervalle = datumsEintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

    @Test
    public void testeZustandswechselFebruar29() {

        TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
        KalenderEintragImpl datumsEintrag = KalenderEintragImpl.parse(eintragsProvider, "Februar29", "29.2.*,*");

        LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 14, 27, 17);
        LocalDateTime endTime = LocalDateTime.of(2020, 3, 14, 14, 28, 17);

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("1.3.2000 00:00", false),
                TestWechsel.of("29.2.2004 00:00", true),
                TestWechsel.of("1.3.2004 00:00", false),
                TestWechsel.of("29.2.2008 00:00", true),
                TestWechsel.of("1.3.2008 00:00", false),
                TestWechsel.of("29.2.2012 00:00", true),
                TestWechsel.of("1.3.2012 00:00", false),
                TestWechsel.of("29.2.2016 00:00", true),
                TestWechsel.of("1.3.2016 00:00", false),
                TestWechsel.of("29.2.2020 00:00", true),
                TestWechsel.of("1.3.2020 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = datumsEintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeIntervalleFebruar29() {

        TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
        KalenderEintragImpl datumsEintrag = KalenderEintragImpl.parse(eintragsProvider, "Februar29", "29.2.*,*");

        LocalDateTime startTime = LocalDateTime.of(2000, 12, 24, 14, 27, 17);
        LocalDateTime endTime = LocalDateTime.of(2020, 3, 14, 14, 28, 17);

        Intervall[] erwarteteIntervalle = {
                TestIntervall.of("29.02.2004 00:00", "01.03.2004 00:00"),
                TestIntervall.of("29.02.2008 00:00", "01.03.2008 00:00"),
                TestIntervall.of("29.02.2012 00:00", "01.03.2012 00:00"),
                TestIntervall.of("29.02.2016 00:00", "01.03.2016 00:00"),
                TestIntervall.of("29.02.2020 00:00", "01.03.2020 00:00")
        };

        List<Intervall> intervalle = datumsEintrag.getIntervalle(startTime, endTime);
        TestIntervall.pruefeIntervalle(erwarteteIntervalle, intervalle);
    }

    @Test
    public void testeGueltigkeitFalscherEintrag() {

        TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
        KalenderEintragImpl eintrag = KalenderEintragImpl.parse(eintragsProvider, "Testdatum", "10.10.2020,2000");

        LocalDateTime abfrageZeitpunkt = LocalDateTime.of(2010, 10, 10, 14, 27, 17);
        SystemkalenderGueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(abfrageZeitpunkt);
        assertEquals(SystemkalenderGueltigkeit.NICHT_GUELTIG, gueltigkeit);
    }
}

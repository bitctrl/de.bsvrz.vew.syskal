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

package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class SystemkalenderEintragTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider eintragsProvider;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        eintragsProvider = new TestKalenderEintragProvider();

        eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich1",
                "Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>"));
        eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich2",
                "Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>"));
        eintragsProvider
                .addEintrag(
                        KalenderEintrag.parse(eintragsProvider, "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>"));
        eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich4",
                "Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})"));
        eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TDDEalt", "17.06.1963,1989"));
        eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "TDDEneu", "03.10.1990,*"));
        eintragsProvider.addEintrag(
                KalenderEintrag.parse(eintragsProvider, "Tag der deutschen Einheit", "ODER{TDDEalt,TDDEneu}*,*"));
        eintragsProvider.addEintrag(KalenderEintrag.parse(eintragsProvider, "Bereich5",
                "Bereich5:=<01.09.2009-30.09.2009>({08:00:00,000-16:00:00,000})"));
    }

    @Test
    public void testeGueltigkeit() throws Exception {

        LocalDateTime zeitpunkt = LocalDateTime.now();

        KalenderEintrag skeMo = eintragsProvider.getKalenderEintrag("Montag");
        KalenderEintrag skeDi = eintragsProvider.getKalenderEintrag("Dienstag");
        KalenderEintrag skeMi = eintragsProvider.getKalenderEintrag("Mittwoch");
        KalenderEintrag skeDo = eintragsProvider.getKalenderEintrag("Donnerstag");
        KalenderEintrag skeFr = eintragsProvider.getKalenderEintrag("Freitag");
        KalenderEintrag skeSa = eintragsProvider.getKalenderEintrag("Samstag");
        KalenderEintrag skeSo = eintragsProvider.getKalenderEintrag("Sonntag");

        for (int idx = 0; idx < 7; idx++) {

            DayOfWeek dow = zeitpunkt.getDayOfWeek();

            switch (dow) {
            case SATURDAY:
                assertTrue("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;
            case SUNDAY:
                assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertTrue("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;
            case MONDAY:
                assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertTrue("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;
            case TUESDAY:
                assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertTrue("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;
            case WEDNESDAY:
                assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertTrue("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;
            case THURSDAY:
                assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertTrue("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;
            case FRIDAY:
                assertFalse("Test 0", skeSa.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 1", skeSo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 2", skeMo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 3", skeDi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 4", skeMi.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertFalse("Test 5", skeDo.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                assertTrue("Test 6", skeFr.getZeitlicheGueltigkeit(zeitpunkt).isZeitlichGueltig());
                break;

            default:
                fail("Test 0-6");

            }

            zeitpunkt = zeitpunkt.plusDays(1);
        }
    }

    @Test
    public void berechneZustandsWechselBereich4() throws Exception {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Bereich4");

        LocalDateTime startTime = LocalDateTime.of(2008, 1, 15, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2008, 1, 16, 23, 59, 59).plusNanos(TimeUnit.MILLISECONDS.toNanos(999));

        TestWechsel[] erwarteteWechsel = {

                TestWechsel.of("1.1.1000 00:00", false), TestWechsel.of("15.1.2008 09:00", true),
                TestWechsel.of("15.1.2008 11:59:59.999", false), TestWechsel.of("15.1.2008 15:30", true),
                TestWechsel.of("15.1.2008 17:59:59.999", false), TestWechsel.of("16.1.2008 09:00", true),
                TestWechsel.of("16.1.2008 11:59:59.999", false), TestWechsel.of("16.1.2008 15:30", true),
                TestWechsel.of("16.1.2008 17:59:59.999", false) };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void berechneZustandsWechselTagDerDeutschenEinheit() throws Exception {

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Tag der deutschen Einheit");

        LocalDateTime startTime = LocalDateTime.of(1960, 6, 17, 23, 59, 59);
        LocalDateTime endTime = LocalDateTime.of(2010, 12, 31, 23, 59, 59)
                .plusNanos(TimeUnit.MILLISECONDS.toNanos(999));

        TestWechsel[] erwarteteWechsel = {

                TestWechsel.of("1.1.1000 00:00", false), TestWechsel.of("17.6.1963 00:00", true),
                TestWechsel.of("18.6.1963 00:00", false), TestWechsel.of("17.6.1964 00:00", true),
                TestWechsel.of("18.6.1964 00:00", false), TestWechsel.of("17.6.1965 00:00", true),
                TestWechsel.of("18.6.1965 00:00", false), TestWechsel.of("17.6.1966 00:00", true),
                TestWechsel.of("18.6.1966 00:00", false), TestWechsel.of("17.6.1967 00:00", true),
                TestWechsel.of("18.6.1967 00:00", false), TestWechsel.of("17.6.1968 00:00", true),
                TestWechsel.of("18.6.1968 00:00", false), TestWechsel.of("17.6.1969 00:00", true),
                TestWechsel.of("18.6.1969 00:00", false), TestWechsel.of("17.6.1970 00:00", true),
                TestWechsel.of("18.6.1970 00:00", false), TestWechsel.of("17.6.1971 00:00", true),
                TestWechsel.of("18.6.1971 00:00", false), TestWechsel.of("17.6.1972 00:00", true),
                TestWechsel.of("18.6.1972 00:00", false), TestWechsel.of("17.6.1973 00:00", true),
                TestWechsel.of("18.6.1973 00:00", false), TestWechsel.of("17.6.1974 00:00", true),
                TestWechsel.of("18.6.1974 00:00", false), TestWechsel.of("17.6.1975 00:00", true),
                TestWechsel.of("18.6.1975 00:00", false), TestWechsel.of("17.6.1976 00:00", true),
                TestWechsel.of("18.6.1976 00:00", false), TestWechsel.of("17.6.1977 00:00", true),
                TestWechsel.of("18.6.1977 00:00", false), TestWechsel.of("17.6.1978 00:00", true),
                TestWechsel.of("18.6.1978 00:00", false), TestWechsel.of("17.6.1979 00:00", true),
                TestWechsel.of("18.6.1979 00:00", false), TestWechsel.of("17.6.1980 00:00", true),
                TestWechsel.of("18.6.1980 00:00", false), TestWechsel.of("17.6.1981 00:00", true),
                TestWechsel.of("18.6.1981 00:00", false), TestWechsel.of("17.6.1982 00:00", true),
                TestWechsel.of("18.6.1982 00:00", false), TestWechsel.of("17.6.1983 00:00", true),
                TestWechsel.of("18.6.1983 00:00", false), TestWechsel.of("17.6.1984 00:00", true),
                TestWechsel.of("18.6.1984 00:00", false), TestWechsel.of("17.6.1985 00:00", true),
                TestWechsel.of("18.6.1985 00:00", false), TestWechsel.of("17.6.1986 00:00", true),
                TestWechsel.of("18.6.1986 00:00", false), TestWechsel.of("17.6.1987 00:00", true),
                TestWechsel.of("18.6.1987 00:00", false), TestWechsel.of("17.6.1988 00:00", true),
                TestWechsel.of("18.6.1988 00:00", false), TestWechsel.of("17.6.1989 00:00", true),
                TestWechsel.of("18.6.1989 00:00", false), TestWechsel.of("3.10.1990 00:00", true),
                TestWechsel.of("4.10.1990 00:00", false), TestWechsel.of("3.10.1991 00:00", true),
                TestWechsel.of("4.10.1991 00:00", false), TestWechsel.of("3.10.1992 00:00", true),
                TestWechsel.of("4.10.1992 00:00", false), TestWechsel.of("3.10.1993 00:00", true),
                TestWechsel.of("4.10.1993 00:00", false), TestWechsel.of("3.10.1994 00:00", true),
                TestWechsel.of("4.10.1994 00:00", false), TestWechsel.of("3.10.1995 00:00", true),
                TestWechsel.of("4.10.1995 00:00", false), TestWechsel.of("3.10.1996 00:00", true),
                TestWechsel.of("4.10.1996 00:00", false), TestWechsel.of("3.10.1997 00:00", true),
                TestWechsel.of("4.10.1997 00:00", false), TestWechsel.of("3.10.1998 00:00", true),
                TestWechsel.of("4.10.1998 00:00", false), TestWechsel.of("3.10.1999 00:00", true),
                TestWechsel.of("4.10.1999 00:00", false), TestWechsel.of("3.10.2000 00:00", true),
                TestWechsel.of("4.10.2000 00:00", false), TestWechsel.of("3.10.2001 00:00", true),
                TestWechsel.of("4.10.2001 00:00", false), TestWechsel.of("3.10.2002 00:00", true),
                TestWechsel.of("4.10.2002 00:00", false), TestWechsel.of("3.10.2003 00:00", true),
                TestWechsel.of("4.10.2003 00:00", false), TestWechsel.of("3.10.2004 00:00", true),
                TestWechsel.of("4.10.2004 00:00", false), TestWechsel.of("3.10.2005 00:00", true),
                TestWechsel.of("4.10.2005 00:00", false), TestWechsel.of("3.10.2006 00:00", true),
                TestWechsel.of("4.10.2006 00:00", false), TestWechsel.of("3.10.2007 00:00", true),
                TestWechsel.of("4.10.2007 00:00", false), TestWechsel.of("3.10.2008 00:00", true),
                TestWechsel.of("4.10.2008 00:00", false), TestWechsel.of("3.10.2009 00:00", true),
                TestWechsel.of("4.10.2009 00:00", false), TestWechsel.of("3.10.2010 00:00", true),
                TestWechsel.of("4.10.2010 00:00", false) };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }
}

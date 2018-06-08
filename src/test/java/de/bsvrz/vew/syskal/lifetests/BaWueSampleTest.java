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

package de.bsvrz.vew.syskal.lifetests;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Test für eine konkrete Konfiguration aus VRZ3 - BaWue.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class BaWueSampleTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3600);

    private static TestKalenderEintragProvider eintragsProvider;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.parseAndAdd(eintragsProvider, "Karfreitag",
                "Ostersonntag - 2 Tage");

        eintragsProvider.parseAndAdd(eintragsProvider, "1_Weihnachtstag", "1_Weihnachtstag:=25.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2_Weihnachtstag", "2_Weihnachtstag:=26.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2012_Ferien_Weihnachten",
                "2012_Ferien_Weihnachten:=<24.12.2012-05.01.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2012_Ferien", "2012_Ferien:=ODER{2012_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Ostern",
                "2013_Ferien_Ostern:=<25.03.2013-05.04.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Pfingsten",
                "2013_Ferien_Pfingsten:=<21.05.2013-01.06.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Sommer",
                "2013_Ferien_Sommer:=<25.07.2013-07.09.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien",
                "2013_Ferien:=ODER{2013_Ferien_Ostern,2013_Ferien_Pfingsten,2013_Ferien_Sommer}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien_Weihnachten",
                "2017_Ferien_Weihnachten:=<22.12.2017-07.01.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Herbst",
                "2018_Ferien_Herbst:=<27.10.2018-04.11.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Ostern",
                "2018_Ferien_Ostern:=<24.03.2018-08.04.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Pfingsten",
                "2018_Ferien_Pfingsten:=<19.05.2018-03.06.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Sommer",
                "2018_Ferien_Sommer:=<26.07.2018-09.09.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Weihnachten",
                "2018_Ferien_Weihnachten:=<20.12.2018-06.01.2019>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien",
                "2018_Ferien:=ODER{2018_Ferien_Ostern,2018_Ferien_Pfingsten,2018_Ferien_Sommer,2018_Ferien_Herbst,2018_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Abendspitze", "Abendspitze:=({15:00:00,000-19:00:00,000})");
        eintragsProvider.parseAndAdd(eintragsProvider, "Allerheiligen", "Allerheiligen:=1.11.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Christi_Himmelfahrt",
                "Christi_Himmelfahrt:=Ostersonntag+39 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Ferien", "Dienstag_Ferien:=UND{Ferien,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag", "Dienstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Ferien",
                "Donnerstag_Ferien:=UND{Ferien,Donnerstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag", "Donnerstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ferien",
                "Ferien:=ODER{2012_Ferien,2013_Ferien,2018_Ferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Ferien", "Freitag_Ferien:=UND{Ferien,Freitag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag", "Freitag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Fronleichnam", "Fronleichnam:=Ostersonntag+60 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Hauptverkehrszeit",
                "Hauptverkehrszeit:=ODER{Morgenspitze,Abendspitze}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Hl_Drei_Könige", "Hl_Drei_Könige:=6.1.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Ferien", "Mittwoch_Ferien:=UND{Ferien,Mittwoch}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch", "Mittwoch");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Ferien", "Montag_Ferien:=UND{Ferien,Montag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag", "Montag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Morgenspitze", "Morgenspitze:=({06:00:00,000-10:00:00,000})");
        eintragsProvider.parseAndAdd(eintragsProvider, "Neujahr", "Neujahr:=1.1.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1 Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostersonntag", "Ostersonntag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstmontag", "Pfingstmontag:=Ostersonntag+50 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstsonntag", "Pfingstsonntag:=Ostersonntag+49 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Ferien", "Samstag_Ferien:=UND{Ferien,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag", "Samstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sommerfest_Test",
                "Sommerfest_Test:=<14.06.2013 14:00:00,000-16.06.2013 18:00:00,000>({16:00:00,000-17:00:00,000})");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Ferien", "Sonntag_Ferien:=UND{Ferien,Sonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag", "Sonntag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Tag_der_Arbeit", "Tag_der_Arbeit:=1.5.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Tag_der_Deutschen_Einheit",
                "Tag_der_Deutschen_Einheit:=3.10.1990,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Winterfest_Test",
                "Winterfest_Test:=UND{2013_Ferien_Sommer-3 Tage,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Verweis_Test",
                "Verweis_Test:=2013_Ferien_Sommer-3 Tage");

    }

    @Test
    public void testeWinterfest_Test() {

        LocalDateTime startTime = LocalDateTime.of(2018, 4, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2018, 5, 1, 0, 0);

        
        
        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Winterfest_Test");

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("04.09.2013 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }

    @Test
    public void testeVerweis_Test() {

        LocalDateTime startTime = LocalDateTime.of(2012, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2015, 1, 1, 0, 0);

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Verweis_Test");

        TestWechsel[] erwarteteWechsel = {
                TestWechsel.of("01.01.1000 00:00", false),
                TestWechsel.of("22.07.2013 00:00", true),
                TestWechsel.of("05.09.2013 00:00", false)
        };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }
}

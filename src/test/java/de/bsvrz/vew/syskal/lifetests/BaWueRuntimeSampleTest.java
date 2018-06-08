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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;

/**
 * Test für eine konkrete Konfiguration aus VRZ3 - BaWue.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class BaWueRuntimeSampleTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3600);

    private static TestKalenderEintragProvider eintragsProvider;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();

        eintragsProvider.parseAndAdd(eintragsProvider, "1_Feiertag",
                "1_Feiertag:=ODER{1_Weihnachtstag,Ostersonntag,Pfingstsonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "1_Weihnachtstag", "1_Weihnachtstag:=25.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2_Feiertag",
                "2_Feiertag:=ODER{2_Weihnachtstag,Ostermontag,Pfingstmontag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2_Weihnachtstag", "2_Weihnachtstag:=26.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2012_Ferien", "2012_Ferien:=ODER{2012_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2012_Ferien_Weihnachten", "2012_Ferien_Weihnachten:=<24.12.2012-05.01.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2012_Winter_Drittel", "2012_Winter_Drittel:=<01.11.2012-28.02.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien",
                "2013_Ferien:=ODER{2013_Ferien_Ostern,2013_Ferien_Pfingsten,2013_Ferien_Sommer,2013_Ferien_Herbst,2013_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Herbst", "2013_Ferien_Herbst:=<27.10.2013-03.11.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Ostern", "2013_Ferien_Ostern:=<24.03.2013-07.04.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Pfingsten", "2013_Ferien_Pfingsten:=<21.05.2013-02.06.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Sommer", "2013_Ferien_Sommer:=<25.07.2013-08.09.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Ferien_Weihnachten", "2013_Ferien_Weihnachten:=<23.12.2013-05.01.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Frühling_Drittel", "2013_Frühling_Drittel:=<01.03.2013-30.06.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_SommerHerbst_Drittel", "2013_SommerHerbst_Drittel:=<01.07.2013-31.10.2013>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2013_Winter_Drittel", "2013_Winter_Drittel:=<01.11.2013-28.02.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Ferien",
                "2014_Ferien:=ODER{2014_Ferien_Ostern,2014_Ferien_Pfingsten,2014_Ferien_Sommer,2014_Ferien_Herbst,2014_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Ferien_Herbst", "2014_Ferien_Herbst:=<26.10.2014-02.11.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Ferien_Ostern", "2014_Ferien_Ostern:=<13.04.2014-27.04.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Ferien_Pfingsten", "2014_Ferien_Pfingsten:=<10.06.2014-22.06.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Ferien_Sommer", "2014_Ferien_Sommer:=<31.07.2014-14.09.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Ferien_Weihnachten", "2014_Ferien_Weihnachten:=<22.12.2014-05.01.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Frühling_Drittel", "2014_Frühling_Drittel:=<01.03.2014-30.06.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_SommerHerbst_Drittel", "2014_SommerHerbst_Drittel:=<01.07.2014-31.10.2014>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2014_Winter_Drittel", "2014_Winter_Drittel:=<01.11.2014-28.02.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Ferien",
                "2015_Ferien:=ODER{2015_Ferien_Ostern,2015_Ferien_Pfingsten,2015_Ferien_Sommer,2015_Ferien_Herbst,2015_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Ferien_Herbst", "2015_Ferien_Herbst:=<02.11.2015-08.11.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Ferien_Ostern", "2015_Ferien_Ostern:=<29.03.2015-12.04.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Ferien_Pfingsten", "2015_Ferien_Pfingsten:=<26.05.2015-07.06.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Ferien_Sommer", "2015_Ferien_Sommer:=<30.07.2015-13.09.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Ferien_Weihnachten", "2015_Ferien_Weihnachten:=<23.12.2015-05.01.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Frühling_Drittel", "2015_Frühling_Drittel:=<01.03.2015-30.06.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_SommerHerbst_Drittel", "2015_SommerHerbst_Drittel:=<01.07.2015-31.10.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2015_Winter_Drittel", "2015_Winter_Drittel:=<01.11.2015-29.02.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Ferien",
                "2016_Ferien:=ODER{2016_Ferien_Ostern,2016_Ferien_Pfingsten,2016_Ferien_Sommer,2016_Ferien_Herbst,2016_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Ferien_Herbst", "2016_Ferien_Herbst:=<30.10.2016-06.11.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Ferien_Ostern", "2016_Ferien_Ostern:=<24.03.2016-03.04.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Ferien_Pfingsten", "2016_Ferien_Pfingsten:=<17.05.2016-29.05.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Ferien_Sommer", "2016_Ferien_Sommer:=<28.07.2016-11.09.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Ferien_Weihnachten", "2016_Ferien_Weihnachten:=<27.12.2016-08.01.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Frühling_Drittel", "2016_Frühling_Drittel:=<01.03.2016-30.06.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_SommerHerbst_Drittel", "2016_SommerHerbst_Drittel:=<01.07.2016-31.10.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2016_Winter_Drittel", "2016_Winter_Drittel:=<01.11.2016-28.02.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien",
                "2017_Ferien:=ODER{2017_Ferien_Ostern,2017_Ferien_Pfingsten,2017_Ferien_Sommer,2017_Ferien_Herbst,2017_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien_Herbst", "2017_Ferien_Herbst:=<28.10.2017-05.11.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien_Ostern", "2017_Ferien_Ostern:=<08.04.2017-23.04.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien_Pfingsten", "2017_Ferien_Pfingsten:=<02.06.2017-18.06.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien_Sommer", "2017_Ferien_Sommer:=<27.07.2017-10.09.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Ferien_Weihnachten", "2017_Ferien_Weihnachten:=<22.12.2017-07.01.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Frühling_Drittel", "2017_Frühling_Drittel:=<01.03.2017-30.06.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_SommerHerbst_Drittel", "2017_SommerHerbst_Drittel:=<01.07.2017-31.10.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2017_Winter_Drittel", "2017_Winter_Drittel:=<01.11.2017-28.02.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien",
                "2018_Ferien:=ODER{2018_Ferien_Ostern,2018_Ferien_Pfingsten,2018_Ferien_Sommer,2018_Ferien_Herbst,2018_Ferien_Weihnachten}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Herbst", "2018_Ferien_Herbst:=<27.10.2018-04.11.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Ostern", "2018_Ferien_Ostern:=<24.03.2018-08.04.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Pfingsten", "2018_Ferien_Pfingsten:=<19.05.2018-03.06.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Sommer", "2018_Ferien_Sommer:=<26.07.2018-09.09.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Weihnachten", "2018_Ferien_Weihnachten:=<20.12.2018-06.01.2019>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Frühling_Drittel", "2018_Frühling_Drittel:=<01.03.2018-30.06.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_SommerHerbst_Drittel", "2018_SommerHerbst_Drittel:=<01.07.2018-31.10.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Winter_Drittel", "2018_Winter_Drittel:=<01.11.2018-28.02.2019>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Abendspitze", "Abendspitze:=({15:00:00,000-19:00:00,000})");
        eintragsProvider.parseAndAdd(eintragsProvider, "Allerheiligen", "Allerheiligen:=1.11.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Christi_Himmelfahrt", "Christi_Himmelfahrt:=Ostersonntag+39 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag", "Dienstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Ferien", "Dienstag_Ferien:=UND{Ferien,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Frühling_Drittel",
                "Dienstag_Frühling_Drittel:=UND{Frühling_Drittel,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_SommerHerbst_Drittel",
                "Dienstag_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Winter_Drittel", "Dienstag_Winter_Drittel:=UND{Winter_Drittel,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag", "Donnerstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Ferien", "Donnerstag_Ferien:=UND{Ferien,Donnerstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Frühling_Drittel",
                "Donnerstag_Frühling_Drittel:=UND{Frühling_Drittel,Donnerstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_SommerHerbst_Drittel",
                "Donnerstag_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Donnerstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Winter_Drittel",
                "Donnerstag_Winter_Drittel:=UND{Winter_Drittel,Donnerstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ferien",
                "Ferien:=ODER{2012_Ferien,2013_Ferien,2014_Ferien,2015_Ferien,2016_Ferien,2017_Ferien,2018_Ferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag", "Freitag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Ferien", "Freitag_Ferien:=UND{Ferien,Freitag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Frühling_Drittel", "Freitag_Frühling_Drittel:=UND{Frühling_Drittel,Freitag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_SommerHerbst_Drittel",
                "Freitag_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Freitag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Winter_Drittel", "Freitag_Winter_Drittel:=UND{Winter_Drittel,Freitag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Fronleichnam", "Fronleichnam:=Ostersonntag+60 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Frühling_Drittel",
                "Frühling_Drittel:=ODER{2013_Frühling_Drittel,2014_Frühling_Drittel,2015_Frühling_Drittel,2016_Frühling_Drittel,2017_Frühling_Drittel,2018_Frühling_Drittel}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Gründonnerstag", "Gründonnerstag:=Ostersonntag-3 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Hauptverkehrszeit", "Hauptverkehrszeit:=ODER{Morgenspitze,Abendspitze}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Heiligabend", "Heiligabend:=24.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Hl_Drei_Könige", "Hl_Drei_Könige:=6.1.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Karsamstag", "Karsamstag:=Ostersonntag-1 Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch", "Mittwoch");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Ferien", "Mittwoch_Ferien:=UND{Ferien,Mittwoch}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Frühling_Drittel",
                "Mittwoch_Frühling_Drittel:=UND{Frühling_Drittel,Mittwoch}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_SommerHerbst_Drittel",
                "Mittwoch_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Mittwoch}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Winter_Drittel", "Mittwoch_Winter_Drittel:=UND{Winter_Drittel,Mittwoch}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag", "Montag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Ferien", "Montag_Ferien:=UND{Ferien,Montag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Frühling_Drittel", "Montag_Frühling_Drittel:=UND{Frühling_Drittel,Montag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_SommerHerbst_Drittel",
                "Montag_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Montag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Winter_Drittel", "Montag_Winter_Drittel:=UND{Winter_Drittel,Montag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Morgenspitze", "Morgenspitze:=({06:00:00,000-10:00:00,000})");
        eintragsProvider.parseAndAdd(eintragsProvider, "Neujahr", "Neujahr:=1.1.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1 Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostersonntag", "Ostersonntag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstmontag", "Pfingstmontag:=Ostersonntag+50 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstsonntag", "Pfingstsonntag:=Ostersonntag+49 Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag", "Samstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Ferien", "Samstag_Ferien:=UND{Ferien,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Frühling_Drittel", "Samstag_Frühling_Drittel:=UND{Frühling_Drittel,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_SommerHerbst_Drittel",
                "Samstag_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Winter_Drittel", "Samstag_Winter_Drittel:=UND{Winter_Drittel,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Silvester", "Silvester:=31.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "SommerHerbst_Drittel",
                "SommerHerbst_Drittel:=ODER{2013_SommerHerbst_Drittel,2014_SommerHerbst_Drittel,2015_SommerHerbst_Drittel,2016_SommerHerbst_Drittel,2017_SommerHerbst_Drittel,2018_SommerHerbst_Drittel}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag", "Sonntag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Ferien", "Sonntag_Ferien:=UND{Ferien,Sonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Frühling_Drittel", "Sonntag_Frühling_Drittel:=UND{Frühling_Drittel,Sonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_SommerHerbst_Drittel",
                "Sonntag_SommerHerbst_Drittel:=UND{SommerHerbst_Drittel,Sonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Winter_Drittel", "Sonntag_Winter_Drittel:=UND{Winter_Drittel,Sonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Tag_der_Arbeit", "Tag_der_Arbeit:=1.5.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Tag_der_Deutschen_Einheit", "Tag_der_Deutschen_Einheit:=3.10.1990,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Winter_Drittel",
                "Winter_Drittel:=ODER{2012_Winter_Drittel,2013_Winter_Drittel,2014_Winter_Drittel,2015_Winter_Drittel,2016_Winter_Drittel,2017_Winter_Drittel,2018_Winter_Drittel}*,*");

//        eintragsProvider.getKalenderEintraege().forEach(ke->ke.recalculateVerweise(eintragsProvider));
        for( KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            eintrag.recalculateVerweise(eintragsProvider);
        }
    }

    @Test
    public void testeAufloesung() {
        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            System.err.println(eintrag);
            Assert.assertFalse(eintrag.getName() + " ist nicht gültig: " + eintrag.getFehler(), eintrag.hasFehler());
        }
    }
}

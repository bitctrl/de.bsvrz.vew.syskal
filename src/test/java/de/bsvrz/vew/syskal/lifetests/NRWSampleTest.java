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

import static org.junit.Assert.assertNotNull;

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
public class NRWSampleTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider eintragsProvider;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();

        eintragsProvider.parseAndAdd(eintragsProvider, "Montag", " Montag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag", "Dienstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch", "Mittwoch");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag", "Donnerstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag", "Freitag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag", "Samstag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag", "Sonntag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostersonntag", "Ostersonntag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Tag", "Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "MoBisSa",
                "MoBisSa:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Werktag", "Werktag:=UND{MoBisSa,NICHT Feiertag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "DienstagNachEinemFeiertag",
                "DienstagNachEinemFeiertag:=UND{TagNachEinemFeiertag,Dienstag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "MittwochNachEinemFeiertag",
                "MittwochNachEinemFeiertag:=UND{TagNachEinemFeiertag,Mittwoch,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "DonnerstagNachEinemFeiertag",
                "DonnerstagNachEinemFeiertag:=UND{TagNachEinemFeiertag,Donnerstag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "FreitagNachEinemFeiertag",
                "FreitagNachEinemFeiertag:=UND{TagNachEinemFeiertag,Freitag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "SamstagNachEinemFeiertag",
                "SamstagNachEinemFeiertag:=UND{TagNachEinemFeiertag,Samstag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorEinemFeiertag",
                "TagVorEinemFeiertag:=ODER{TagVorOstern,TagVorPfingsten,TagVorWeihnachten,TagVorNeujahrstag,TagVorKarfreitag,TagVorMaifeiertag,TagVorChristiHimmelfahrt,TagVorFronleichnam,TagVorTagDerEinheit,TagVorAllerheiligen}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "SonntagVorEinemFeiertag",
                "SonntagVorEinemFeiertag:=UND{TagVorEinemFeiertag,Sonntag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "MontagVorEinemFeiertag",
                "MontagVorEinemFeiertag:=UND{TagVorEinemFeiertag,Montag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "DienstagVorEinemFeiertag",
                "DienstagVorEinemFeiertag:=UND{TagVorEinemFeiertag,Dienstag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "MittwochVorEinemFeiertag",
                "MittwochVorEinemFeiertag:=UND{TagVorEinemFeiertag,Mittwoch,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "DonnerstagVorEinemFeiertag",
                "DonnerstagVorEinemFeiertag:=UND{TagVorEinemFeiertag,Donnerstag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "FreitagVorEinemFeiertag",
                "FreitagVorEinemFeiertag:=UND{TagVorEinemFeiertag,Freitag,Werktag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachEinemFeiertag",
                "TagNachEinemFeiertag:=ODER{TagNachOstern,TagNachPfingsten,TagNachWeihnachten,TagNachNeujahrstag,TagNachKarfreitag,TagNachMaifeiertag,TagNachChristiHimmelfahrt,TagNachFronleichnam,TagNachTagDerEinheit,TagNachAllerheiligen}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Feiertag",
                "Feiertag:=ODER{Karfreitag,Ostersonntag,Ostermontag,Pfingstsonntag,Pfingstmontag,Fronleichnam,ChristiHimmelfahrt,Allerheiligen,TagDerEinheit,1Weihnachtstag,2Weihnachtstag,Neujahrstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montagsfeiertag", "Montagsfeiertag:=UND{Feiertag,Montag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstagsfeiertag",
                "Dienstagsfeiertag:=UND{Feiertag,Dienstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwochsfeiertag",
                "Mittwochsfeiertag:=UND{Feiertag,Mittwoch}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstagsfeiertag",
                "Donnerstagsfeiertag:=UND{Feiertag,Donnerstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitagsfeiertag",
                "Freitagsfeiertag:=UND{Feiertag,Freitag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstagsfeiertag",
                "Samstagsfeiertag:=UND{Feiertag,Samstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostern", "Ostern:=ODER{Ostersonntag,Ostermontag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingsten", "Pfingsten:=ODER{Pfingstsonntag,Pfingstmontag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Weihnachten",
                "Weihnachten:=ODER{1Weihnachtstag,2Weihnachtstag}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Neujahrstag", "Neujahrstag:=01.01.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Karfreitag", "Karfreitag:=Ostersonntag-2Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ostermontag", "Ostermontag:=Ostersonntag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Maifeiertag", "Maifeiertag:=01.05.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "ChristiHimmelfahrt", "ChristiHimmelfahrt:=Ostersonntag+39Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstsonntag", "Pfingstsonntag:=Ostermontag+48Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstmontag", "Pfingstmontag:=Pfingstsonntag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "Fronleichnam", "Fronleichnam:=Ostersonntag+60Tage");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagDerEinheit", "TagDerEinheit:=03.10.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Allerheiligen", "Allerheiligen:=01.11.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "1Weihnachtstag", "1Weihnachtstag:=25.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "2Weihnachtstag", "2Weihnachtstag:=26.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Heiligabend", "Heiligabend:=24.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Silvester", "Silvester:=31.12.*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorOstern", "TagVorOstern:=Ostersonntag-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorPfingsten", "TagVorPfingsten:=Pfingstsonntag-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorWeihnachten", "TagVorWeihnachten:=1Weihnachtstag-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorNeujahrstag", "TagVorNeujahrstag:=Neujahrstag-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorKarfreitag", "TagVorKarfreitag:=Karfreitag-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorMaifeiertag", "TagVorMaifeiertag:=Maifeiertag-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorChristiHimmelfahrt",
                "TagVorChristiHimmelfahrt:=ChristiHimmelfahrt-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorFronleichnam", "TagVorFronleichnam:=Fronleichnam-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorTagDerEinheit",
                "TagVorTagDerEinheit:=TagDerEinheit-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorAllerheiligen",
                "TagVorAllerheiligen:=Allerheiligen-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorSilvester", "TagVorSilvester:=Silvester-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachOstern", "TagNachOstern:=Ostermontag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachPfingsten", "TagNachPfingsten:=Pfingstsonntag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachWeihnachten", "TagNachWeihnachten:=1Weihnachtstag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachNeujahrstag", "TagNachNeujahrstag:=Neujahrstag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachKarfreitag", "TagNachKarfreitag:=Karfreitag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachMaifeiertag", "TagNachMaifeiertag:=Maifeiertag+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachChristiHimmelfahrt",
                "TagNachChristiHimmelfahrt:=ChristiHimmelfahrt+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachFronleichnam", "TagNachFronleichnam:=Fronleichnam+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachTagDerEinheit",
                "TagNachTagDerEinheit:=TagDerEinheit+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachAllerheiligen",
                "TagNachAllerheiligen:=Allerheiligen+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachSilvester", "TagNachSilvester:=Silvester+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchultagVorDenFerien",
                "LetzterSchultagVorDenFerien:=ODER{TagVorDenWeihnachtsferien,TagVorDenSommerferien,TagVorDenOsterferien,TagVorDenHerbstferien,TagVorDenPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagAmFerienanfang",
                "ErsterSchulfreierTagAmFerienanfang:=ODER{ErsterSchulfreierTagDerWeihnachtsferien,ErsterSchulfreierTagDerSommerferien,ErsterSchulfreierTagDerOsterferien,ErsterSchulfreierTagDerHerbstferien,ErsterSchulfreierTagDerPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagAmFerienende",
                "LetzterSchulfreierTagAmFerienende:=ODER{LetzterSchulfreierTagDerWeihnachtsferien,LetzterSchulfreierTagDerSommerferien,LetzterSchulfreierTagDerOsterferien,LetzterSchulfreierTagDerHerbstferien,LetzterSchulfreierTagDerPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ferien",
                "Ferien:=ODER{Weihnachtsferien,Osterferien,Sommerferien,Herbstferien,Pfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "FerienInAnderemBundesland", "");
        eintragsProvider.parseAndAdd(eintragsProvider, "Weihnachtsferien2015",
                "Weihnachtsferien2015:=<23.12.2015-06.01.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sommerferien2015", "Sommerferien2015:=<29.06.2015-11.08.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Osterferien2015", "Osterferien2015:=<30.03.2015-11.04.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Herbstferien2015", "Herbstferien2015:=<05.10.2015-17.10.2015>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Winterferien2015", "Winterferien2015:=<01.02.2009-08.02.2009>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstferien2015", "Pfingstferien2015:=26.05.2015,2015");
        eintragsProvider.parseAndAdd(eintragsProvider, "Weihnachtsferien2016",
                "Weihnachtsferien2016:=<23.12.2016-06.01.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sommerferien2016", "Sommerferien2016:=<11.07.2016-23.08.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Osterferien2016", "Osterferien2016:=<21.03.2016-02.04.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Herbstferien2016", "Herbstferien2016:=<10.10.2016-21.10.2016>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Winterferien2016", "Winterferien2016:=<01.02.2009-08.02.2009>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstferien2016", "Pfingstferien2016:=17.05.2016,2016");
        eintragsProvider.parseAndAdd(eintragsProvider, "Weihnachtsferien2017",
                "Weihnachtsferien2017:=<27.12.2017-06.01.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sommerferien2017", "Sommerferien2017:=<11.07.2017-29.08.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Osterferien2017", "Osterferien2017:=<10.04.2017-22.04.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Herbstferien2017", "Herbstferien2017:=<23.10.2017-04.11.2017>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Winterferien2017", "Winterferien2017:=<01.02.2009-08.02.2009>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstferien2017", "Pfingstferien2017:=06.06.2017,2017");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sommerferien",
                "Sommerferien:=ODER{Sommerferien2015,Sommerferien2016,Sommerferien2017}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Osterferien",
                "Osterferien:=ODER{Osterferien2015,Osterferien2016,Osterferien2017}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Herbstferien",
                "Herbstferien:=ODER{Herbstferien2015,Herbstferien2016,Herbstferien2017}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Winterferien",
                "Winterferien:=ODER{Winterferien2015,Winterferien2016,Winterferien2017}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Pfingstferien",
                "Pfingstferien:=ODER{Pfingstferien2015,Pfingstferien2016,Pfingstferien2017}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenWeihnachtsferien",
                "TagVorDenWeihnachtsferien:=Weihnachtsferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenSommerferien",
                "TagVorDenSommerferien:=Sommerferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenOsterferien",
                "TagVorDenOsterferien:=Osterferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenHerbstferien",
                "TagVorDenHerbstferien:=Herbstferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenWinterferien",
                "TagVorDenWinterferien:=Winterferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenPfingstferien",
                "TagVorDenPfingstferien:=Pfingstferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachDenWeihnachtsferien",
                "TagNachDenWeihnachtsferien:=Weihnachtsferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachDenSommerferien",
                "TagNachDenSommerferien:=Sommerferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachDenOsterferien",
                "TagNachDenOsterferien:=Osterferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagNachDenHerbstferien",
                "TagNachDenHerbstferien:=Herbstferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenWinterferien",
                "TagNachDenWinterferien:=Winterferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "TagVorDenPfingstferien",
                "TagNachDenPfingstferien:=Pfingstferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagDerWeihnachtsferien",
                "ErsterSchulfreierTagDerWeihnachtsferien:=TagVorDenWeihnachtsferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagDerSommerferien",
                "ErsterSchulfreierTagDerSommerferien:=TagVorDenSommerferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagDerOsterferien",
                "ErsterSchulfreierTagDerOsterferien:=TagVorDenOsterferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagDerHerbstferien",
                "ErsterSchulfreierTagDerHerbstferien:=TagVorDenHerbstferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagDerWinterferien",
                "ErsterSchulfreierTagDerWinterferien:=TagVorDenWinterferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagDerPfingstferien",
                "ErsterSchulfreierTagDerPfingstferien:=TagVorDenPfingstferien+1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagDerWeihnachtsferien",
                "LetzterSchulfreierTagDerWeihnachtsferien:=TagNachDenWeihnachtsferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagDerSommerferien",
                "LetzterSchulfreierTagDerSommerferien:=TagNachDenSommerferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagDerOsterferien",
                "LetzterSchulfreierTagDerOsterferien:=TagNachDenOsterferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagDerHerbstferien",
                "LetzterSchulfreierTagDerHerbstferien:=TagNachDenHerbstferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagDerWinterferien",
                "LetzterSchulfreierTagDerWinterferien:=TagNachDenWinterferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagDerPfingstferien",
                "LetzterSchulfreierTagDerPfingstferien:=TagNachDenPfingstferien-1Tag");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchultagVorDenFerien",
                "LetzterSchultagVorDenFerien:=ODER{TagVorDenWeihnachtsferien,TagVorDenSommerferien,TagVorDenOsterferien,TagVorDenHerbstferien,TagVorDenPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchultagNachDenFerien",
                "ErsterSchultagNachDenFerien:=ODER{TagNachDenWeihnachtsferien,TagNachDenSommerferien,TagNachDenOsterferien,TagNachDenHerbstferien,TagNachDenPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "ErsterSchulfreierTagAmFerienanfang",
                "ErsterSchulfreierTagAmFerienanfang:=ODER{ErsterSchulfreierTagDerWeihnachtsferien,ErsterSchulfreierTagDerSommerferien,ErsterSchulfreierTagDerOsterferien,ErsterSchulfreierTagDerHerbstferien,ErsterSchulfreierTagDerPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "LetzterSchulfreierTagAmFerienende",
                "LetzterSchulfreierTagAmFerienende:=ODER{LetzterSchulfreierTagDerWeihnachtsferien,LetzterSchulfreierTagDerSommerferien,LetzterSchulfreierTagDerOsterferien,LetzterSchulfreierTagDerHerbstferien,LetzterSchulfreierTagDerPfingstferien}*,*");
        eintragsProvider.parseAndAdd(eintragsProvider, "Messe", "Messe:=<10.10.2009-16.10.2009>");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sportereignis", "Sportereignis:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Großveranstaltung", "Großveranstaltung:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Baustelle", "Baustelle:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sperrung", "Sperrung:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Unfall", "Unfall:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "UnzureichendeSichtverhältnisse",
                "UnzureichendeSicherverhältnisse:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "EingeschränkteSichtverhältnisse",
                "EingeschränkteSichtverhältnisse:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "GefährlicherFahrbahnzustandGlatt",
                "GefährlicherFahrbahnzustandGlatt:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "SchwierigerFahrbahnzustandNass",
                "SchwierigerFahrbahnzustandNass:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Nebel", "Nebel:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ozonalarm", "Ozonalarm:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "SonderereignisMitErhöhtemVerkehr",
                "SonderereignisMitErhöhtemVerkehr:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "SonderereignisMitGeringeremVerkehr",
                "SonderereignisMitGeringeremVerkehr:=01.01.2009,2009");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonderereignis", "Sonderereignis:=01.01.2009,2009");

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            eintrag.recalculateVerweise(eintragsProvider);
        }

    }

    @Test
    public void testeRekursionenUndUngueltigeEintrage() {

        LocalDateTime startTime = LocalDateTime.of(2018, 5, 13, 11, 14);
        LocalDateTime endTime = LocalDateTime.of(2018, 6, 13, 11, 14);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            assertNotNull(eintrag.getZustandsWechsel(startTime, endTime));
        }
    }

    @Test
    public void testeRekursionenUndUngueltigeEintraegeOstern() {

        LocalDateTime startTime = LocalDateTime.of(2018, 5, 13, 11, 14);
        LocalDateTime endTime = LocalDateTime.of(2018, 6, 13, 11, 14);

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Ostern");
        assertNotNull(eintrag.getZustandsWechsel(startTime, endTime));
    }

    @Test
    public void testeRekursionenUndUngueltigeEintraegeWerktag() {

        LocalDateTime startTime = LocalDateTime.of(2018, 5, 13, 11, 14);
        LocalDateTime endTime = LocalDateTime.of(2018, 6, 13, 11, 14);

        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag("Werktag");
        assertNotNull(eintrag.getZustandsWechsel(startTime, endTime));
    }

    
    
}

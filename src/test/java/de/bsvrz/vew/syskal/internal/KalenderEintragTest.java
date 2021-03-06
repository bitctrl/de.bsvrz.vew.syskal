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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;

public class KalenderEintragTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider provider;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        provider = new TestKalenderEintragProvider();

        provider.addEintrag(KalenderEintrag.parse(provider, "Bereich1",
                "Bereich1:=<01.01.2008 00:00:00,000-31.01.2008 23:59:59,999>"));
        provider.addEintrag(KalenderEintrag.parse(provider, "Bereich2",
                "Bereich2:=<15.01.2008 00:00:00,000-15.02.2008 23:59:59,999>"));
        provider.addEintrag(KalenderEintrag.parse(provider, "Bereich3", "Bereich3:=<15.01.2008-15.02.2008>"));
        provider.addEintrag(KalenderEintrag.parse(provider, "Bereich4",
                "Bereich4:=<15.01.2008-15.02.2008>({09:00:00,000-11:59:59,999}{15:30:00,000-17:59:59,999})"));

    }

    @Test
    public void testParseSystemkalenderEintrag() {

        assertFalse("Test 1", provider.parseAndAdd(provider, "Ske1", "Montag+1Tag").hasFehler());
        assertFalse("Test 2", provider.parseAndAdd(provider, "Ske2", "Dienstag-1Tag").hasFehler());
        assertFalse("Test 3", provider.parseAndAdd(provider, "Ske3", "Mittwoch+1Tag").hasFehler());
        assertFalse("Test 4", provider.parseAndAdd(provider, "Ske4", "Donnerstag+2Tage").hasFehler());
        assertFalse("Test 5", provider.parseAndAdd(provider, "Ske5", "UND{Bereich1,Bereich2}*,*").hasFehler());
        assertFalse("Test 6", provider.parseAndAdd(provider, "Ske6", "ODER{Bereich1,Bereich2}*,*").hasFehler());
        assertFalse("Test 7", provider.parseAndAdd(provider, "Ske7", "ODER{Bereich1,NICHT Bereich2}*,*").hasFehler());
        assertFalse("Test 8", provider.parseAndAdd(provider, "Ske8", "UND{NICHT Bereich1,Bereich2}*,2008").hasFehler());
        assertFalse("Test 9", provider.parseAndAdd(provider, "Ske9", "ODER{Bereich4,Bereich2}*,*").hasFehler());
        assertFalse("Test 10", provider.parseAndAdd(provider, "Ske10", "UND{Bereich3,Bereich1}*,*").hasFehler());
        assertFalse("Test 11", provider.parseAndAdd(provider, "Ske11", "UND{Ske5,Ske6}*,*").hasFehler());

        assertTrue("Test 12", provider.parseAndAdd(provider, "Ske12", "Sontag+1Tag").hasFehler());
        assertTrue("Test 13", provider.parseAndAdd(provider, "Ske13", "Dienstag2-1Tag").hasFehler());
        assertTrue("Test 14", provider.parseAndAdd(provider, "Ske14", "aMittwoch+1Tag").hasFehler());
        assertTrue("Test 15", provider.parseAndAdd(provider, "Ske15", "OTER{Bereich1,Bereich2}*,*").hasFehler());
        assertTrue("Test 16", provider.parseAndAdd(provider, "Ske16", "ODER{BereichXyz,Bereich2}*,*").hasFehler());
        assertTrue("Test 17", provider.parseAndAdd(provider, "Ske17", "ODER{Bereich4,Bereich2*,*").hasFehler());
        assertFalse("Test 18", provider.parseAndAdd(provider, "Ske18", "Ske15").hasFehler());
        assertFalse("Test 19", provider.parseAndAdd(provider, "Ske19", "Ske16+1Tag").hasFehler());
    }

    @Test
    public void testeNieGueltig() {
        provider.addEintrag(KalenderEintrag.parse(provider, "NieGueltig",
                "NieGueltig:=UND{Montag,Dienstag}"));

        KalenderEintrag eintrag = provider.getKalenderEintrag("NieGueltig");
        SystemkalenderGueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(LocalDateTime.now());

        assertFalse(gueltigkeit.isZeitlichGueltig());
        assertEquals(SystemKalender.MIN_DATETIME, gueltigkeit.getErsterWechsel().getZeitPunkt());
        assertFalse(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
        assertEquals(SystemKalender.MAX_DATETIME, gueltigkeit.getNaechsterWechsel().getZeitPunkt());

    }

    @Test
    public void testeImmerGueltig() {
        provider.addEintrag(KalenderEintrag.parse(provider, "ImmerGueltig",
                "ImmerGueltig:=ODER{Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag,Sonntag}"));

        KalenderEintrag eintrag = provider.getKalenderEintrag("ImmerGueltig");
        SystemkalenderGueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(LocalDateTime.now());

        assertTrue(gueltigkeit.isZeitlichGueltig());
        assertEquals(SystemKalender.MIN_DATETIME, gueltigkeit.getErsterWechsel().getZeitPunkt());
        assertTrue(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
        assertEquals(SystemKalender.MAX_DATETIME, gueltigkeit.getNaechsterWechsel().getZeitPunkt());

    }
    
    @Test 
    public void testeRekursion() {
        provider.addEintrag(KalenderEintrag.parse(provider, "EintragA",
                "Montag"));
        provider.addEintrag(KalenderEintrag.parse(provider, "EintragB",
                "Dienstag"));
        provider.addEintrag(KalenderEintrag.parse(provider, "EintragB",
                "UND{EintragA, EintragB}"));
        
        KalenderEintrag eintrag = provider.getKalenderEintrag("EintragB");
        SystemkalenderGueltigkeit gueltigkeit = eintrag.getZeitlicheGueltigkeit(LocalDateTime.now());

        assertFalse(gueltigkeit.isZeitlichGueltig());
        assertEquals(SystemKalender.MIN_DATETIME, gueltigkeit.getErsterWechsel().getZeitPunkt());
        assertFalse(gueltigkeit.getNaechsterWechsel().isWirdGueltig());
        assertEquals(SystemKalender.MAX_DATETIME, gueltigkeit.getNaechsterWechsel().getZeitPunkt());
    }
}

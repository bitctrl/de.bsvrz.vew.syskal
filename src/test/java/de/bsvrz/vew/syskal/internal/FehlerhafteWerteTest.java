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
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Prüffall 14 - Fehlerhafte Werte - Gültigkeitsintervalle und Wechsel.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class FehlerhafteWerteTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider eintragsProvider;

    private static LocalDateTime startTime;

    private static LocalDateTime endTime;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.parseAndAdd(eintragsProvider, "Ske12",
                "{Sontag + 1 Tag}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ske13", "{Dienstag2 – 1 Tag}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ske14", "{aMittwoch + 1 Tag}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ske20", "{Samstag + 2 tag}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ske21", "OTER{Montag, Mittwoch}");
        eintragsProvider.parseAndAdd(eintragsProvider, "NieGueltig", "UND{Montag, Dienstag}");

        startTime = LocalDateTime.of(2018, 1, 17, 2, 8);
        endTime = LocalDateTime.of(2018, 1, 19, 21, 42);
    }

    @Test
    public void testeUngueltigeEintrage() {

        pruefeEintrag("Ske12");
        pruefeEintrag("Ske13");
        pruefeEintrag("Ske14");
        pruefeEintrag("Ske20");
        pruefeEintrag("Ske21");
    }

    private void pruefeEintrag(String name) {
        
        KalenderEintrag eintrag = eintragsProvider.getKalenderEintrag(name);
        assertTrue("Eintrag ist fehlerhaft", eintrag.hasFehler());
        
        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        assertEquals("Anzahl der Zustandswechsel",  1, zustandsWechsel.size());
        assertFalse("Zustand ist ungültig", zustandsWechsel.get(0).isWirdGueltig());
        assertEquals("Minimaler Zeitpunkt",  SystemKalender.MIN_DATETIME, zustandsWechsel.get(0).getZeitPunkt());

        List<Intervall> intervalle = eintrag.getIntervalle(startTime, endTime);
        assertTrue("Keine gültigen Intervalle", intervalle.isEmpty());
    }

    @Test
    public void testeNieGueltig() {

        KalenderEintrag nieGueltig = eintragsProvider.getKalenderEintrag("NieGueltig");
        assertFalse(nieGueltig.hasFehler());

        List<ZustandsWechsel> zustandsWechsel = nieGueltig.getZustandsWechsel(startTime, endTime);
        assertEquals("Anzahl der Zustandswechsel",  1, zustandsWechsel.size());
        assertFalse("Zustand ist ungültig", zustandsWechsel.get(0).isWirdGueltig());
        assertEquals("Minimaler Zeitpunkt",  SystemKalender.MIN_DATETIME, zustandsWechsel.get(0).getZeitPunkt());

        List<Intervall> intervalle = nieGueltig.getIntervalle(startTime, endTime);
        assertTrue("Keine gültigen Intervalle", intervalle.isEmpty());
    }
}

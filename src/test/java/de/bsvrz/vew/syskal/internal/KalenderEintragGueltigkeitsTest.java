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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;

@RunWith(Parameterized.class)
public class KalenderEintragGueltigkeitsTest {

//    @Rule
//    public Timeout globalTimeout = Timeout.seconds(5);

    protected static final TestKalenderEintragProvider PROVIDER = new TestKalenderEintragProvider();

    @Parameter(0)
    public String eintragsName;

    @Parameter(1)
    public LocalDateTime abfrageZeitPunkt;

    @Parameter(2)
    public boolean vorherigeGueltigkeit;

    @Parameter(3)
    public LocalDateTime vorherigerZeitPunkt;

    @Parameter(4)
    public boolean aktuelleGueltigkeit;

    @Parameter(5)
    public LocalDateTime initialerZeitPunkt;

    @Parameter(6)
    public boolean zielGueltigkeit;

    @Parameter(7)
    public LocalDateTime wechselZeitPunkt;

    @Parameters(name = "{0}-{1}")
    public static Collection<Object[]> getData() throws IOException {

        Class<?> clazz = KalenderEintragGueltigkeitsTest.class;
        Collection<Object[]> testDaten;

        try (BufferedReader stream = new BufferedReader(new InputStreamReader(
                clazz.getResourceAsStream(clazz.getSimpleName() + ".testdaten"), Charset.forName("UTF-8")))) {
            testDaten = liesTestDaten(stream);
        }

        return testDaten;
    }

    protected static Collection<Object[]> liesTestDaten(BufferedReader reader) throws IOException {

        List<Object[]> result = new ArrayList<>();

        String line = null;
        do {
            line = reader.readLine();
            if (line != null) {
                line = line.trim();
                if (line.startsWith("|")) {

                    line = line.substring("|".length());

                    List<Object> lineResult = new ArrayList<>();

                    String[] splits = line.split("\\|");

                    int idx = 0;
                    lineResult.add(splits[idx++].trim());
                    lineResult.add(LocalDateTime.parse(splits[idx++].trim(),
                            DateTimeFormatter.ofPattern("d.M.y H[:m[:s[.SSS]]]")));
                    lineResult.add(splits[idx++].trim().equalsIgnoreCase("true"));
                    lineResult.add(LocalDateTime.parse(splits[idx++].trim(),
                            DateTimeFormatter.ofPattern("d.M.y H[:m[:s[.SSS]]]")));
                    lineResult.add(splits[idx++].trim().equalsIgnoreCase("true"));
                    lineResult.add(LocalDateTime.parse(splits[idx++].trim(),
                            DateTimeFormatter.ofPattern("d.M.y H[:m[:s[.SSS]]]")));
                    lineResult.add(splits[idx++].trim().equalsIgnoreCase("true"));
                    lineResult.add(LocalDateTime.parse(splits[idx++].trim(),
                            DateTimeFormatter.ofPattern("d.M.y H[:m[:s[.SSS]]]")));

                    result.add(lineResult.toArray());
                } else if (!line.startsWith("#") && !line.isEmpty()) {
                    String[] splits = line.split("=");
                    PROVIDER.parseAndAdd(PROVIDER, splits[0].trim(), splits[1].trim());
                }
            }
        } while (line != null);

        return result;
    }

    @Test
    public void testeGueltigkeit() {

        KalenderEintrag eintrag = PROVIDER.getKalenderEintrag(eintragsName);
        SystemkalenderGueltigkeit gueltigKeit = eintrag.getZeitlicheGueltigkeit(abfrageZeitPunkt);

        assertEquals("Aktuelle Gueltigkeit", aktuelleGueltigkeit, gueltigKeit.isZeitlichGueltig());
        assertEquals("Initialer Zeitpunkt", initialerZeitPunkt, gueltigKeit.getErsterWechsel().getZeitPunkt());
        assertEquals("Zielgültigkeit", zielGueltigkeit, gueltigKeit.getNaechsterWechsel().isWirdGueltig());
        assertEquals("Wechselzeitpunkt", wechselZeitPunkt, gueltigKeit.getNaechsterWechsel().getZeitPunkt());
    }

    @Test
    public void testeVorherigeGueltigkeit() {

        KalenderEintrag eintrag = PROVIDER.getKalenderEintrag(eintragsName);
        SystemkalenderGueltigkeit gueltigKeit = eintrag.getZeitlicheGueltigkeitVor(abfrageZeitPunkt);

        assertEquals("Vorherige Gueltigkeit", vorherigeGueltigkeit, gueltigKeit.isZeitlichGueltig());
        assertEquals("Vorheriger Zeitpunkt", vorherigerZeitPunkt, gueltigKeit.getErsterWechsel().getZeitPunkt());
        assertEquals("Aktuelle Gültigkeit", aktuelleGueltigkeit, gueltigKeit.getNaechsterWechsel().isWirdGueltig());
        assertEquals("Initialer Zeitpunkt", initialerZeitPunkt, gueltigKeit.getNaechsterWechsel().getZeitPunkt());
    }

}

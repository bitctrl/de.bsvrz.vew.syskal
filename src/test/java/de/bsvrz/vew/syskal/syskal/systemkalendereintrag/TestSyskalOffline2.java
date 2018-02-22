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

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;
import de.bsvrz.vew.syskal.internal.KalenderEintragImpl;

public class TestSyskalOffline2 {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void beispiele2() {

        TestKalenderEintragProvider eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.addEintrag(KalenderEintragImpl.parse(eintragsProvider, "Tag", "Tag"));

        KalenderEintragImpl eintrag = eintragsProvider.getKalenderEintrag("Tag");
        LocalDateTime startTime = LocalDateTime.of(2009, 10, 1, 14, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2009, 10, 5, 14, 0, 0);

        TestWechsel[] erwarteteWechsel = { TestWechsel.of("1.1.1000 00:00", false) };

        List<ZustandsWechsel> zustandsWechsel = eintrag.getZustandsWechsel(startTime, endTime);
        TestWechsel.pruefeWechsel(erwarteteWechsel, zustandsWechsel);
    }
}

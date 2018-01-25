/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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
 * Dambach-Werke GmbH
 * Elektronische Leitsysteme
 * Fritz-Minhardt-Str. 1
 * 76456 Kuppenheim
 * Phone: +49-7222-402-0
 * Fax: +49-7222-402-200
 * mailto: info@els.dambach.de
 */

package de.bsvrz.vew.syskal.syskal.erinnerungsfunktion;

import java.util.Date;
import java.util.Map;
import java.util.Timer;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.syskal.benachrichtigungsfunktion.BenachrichtigeFunktion;
import de.bsvrz.vew.syskal.syskal.systemkalendereintrag.AlterSystemkalenderEintrag;

/**
 * Kommentar
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 */
public class ErinnerungsFunktion {

	/**
	 * Das Timer Objekt
	 */
	private Timer timer = new Timer();

	/**
	 * Das Ereignis, wird dem Sender-Instanz ÃÂ¼bergeben
	 */
	private AlterSystemkalenderEintrag ske;

	/**
	 * Konstruktor der Klasse
	 * 
	 * @param ske
	 *            das Ereignis, welches den Zustand meldet
	 * @param b
	 *            Schalter, der den Wecker sofort(false) oder zum definerten
	 *            Zeitpunkt(true) losgehen laesst
	 */
	public ErinnerungsFunktion(AlterSystemkalenderEintrag ske, Boolean b) {
		this.ske = ske;

		Long now = new Date().getTime();

		if (b) {

			for (Map.Entry<Long, Boolean> me : ske.getListeZustandsWechsel().entrySet()) {
				if (me.getKey() > now) {

					Long l = me.getKey();
					load(l);

				}
			}

		} else {

			load(now);

		}

	}

	/**
	 * stellt den Timer
	 * 
	 * @param l
	 *            der Zeitstempel
	 */
	public void load(Long l) {
		Date date = new Date();
		date.setTime(l);

		timer.schedule(new BenachrichtigeFunktion(ske, l), date);

	}

	/**
	 * loescht den Timer
	 */
	public void unload() {

		timer.cancel();

	}
}
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

package de.bsvrz.vew.syskal.syskal.benachrichtigungsfunktion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import de.bsvrz.vew.syskal.SystemkalenderEintrag;
import de.bsvrz.vew.syskal.syskal.systemkalendereintrag.AlterSystemkalenderEintrag;

/**
 * Die Klasse zum Versenden der Ereigniszustaende. Erweitert die Klasse
 * TimerTask. Die run() Methode wird ausgefuehrt wenn die im ReminderService
 * eingestellte Zeit abgelaufen ist. Sie implentiert zusÃÂ¤tzlich das
 * ClientSenderInterface welches die Methoden zum Versenden der Daten
 * bereitstellt.
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 */
public class BenachrichtigeFunktion extends TimerTask {
	/**
	 * Die Zeit bis die Daten versendet werden sollen
	 */
	private AlterSystemkalenderEintrag ske;

	/**
	 * Die Zeit bis die Daten versendet werden sollen
	 */
	private Long timeNow;

	private static final BenachrichtigeListenerVerwaltung MULTI = new BenachrichtigeListenerVerwaltung();

	/**
	 * Konstruktor der Klasse
	 * 
	 */

	public BenachrichtigeFunktion() {

	}

	/**
	 * Konstruktor der Klasse, mit Zeitangabe des Zustandswechsels
	 * 
	 * @param ske
	 *            das Ereignis, welches den Zustand meldet
	 * @param now
	 *            Die Zeit bis die Daten versendet werden sollen
	 * 
	 */
	public BenachrichtigeFunktion(AlterSystemkalenderEintrag ske, Long now) {
		this.ske = ske;
		this.timeNow = now;
	}

	@Override
	public void run() {
		Date d = new Date();
		d.setTime(timeNow);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,SSS");

		String datum = format.format(d);

		String meldung;
		if (ske.isGueltig(timeNow)) {
			meldung = datum + " " + ske.getPid() + "(" + ske.getName() + ")" + " : gültig";
		} else {
			meldung = datum + " " + ske.getPid() + "(" + ske.getName() + ")" + " : nicht gültig";
		}

		BenachrichtigeEvent event = new BenachrichtigeEvent(this, meldung);

		MULTI.meldeAnAlle(event);

	}

	public static BenachrichtigeListenerVerwaltung getBenachrichtigeListenerVerwaltung() {
		return MULTI;
	}

}

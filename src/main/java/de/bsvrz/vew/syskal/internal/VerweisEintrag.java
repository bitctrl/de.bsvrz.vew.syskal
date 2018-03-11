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

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

/**
 * Repräsentation eines Eintrags, der durch die Erweiterung eines bereits
 * bestehenden gebildet wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class VerweisEintrag extends KalenderEintrag {

	private static final Debug LOGGER = Debug.getLogger();

	/** der definierende Eintrag mit zusätzlichen Erweiterungen. */
	private Verweis verweis;

	public static VerweisEintrag of(Verweis verweis) {
		return new VerweisEintrag(verweis);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param provider
	 *            die Verwaltung aller bekannten Systemkalendereinträge zur
	 *            Verifizierung von Referenzen
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            der definierende Text
	 */
	public VerweisEintrag(KalenderEintragProvider provider, final String name, final String definition) {
		super(name, definition);
		try {
			verweis = new Verweis(provider, definition);
			if (verweis.isUngueltig()) {
				addFehler(verweis.getName() + " ist ungültig");
			}
		} catch (final ParseException | NumberFormatException e) {
			String message = "Fehler beim Parsen des Eintrags: " + definition + ": " + e.getLocalizedMessage();
			LOGGER.warning(message);
			verweis = null;
			addFehler(message);
		}
	}

	VerweisEintrag(Verweis verweis) {
		super(verweis.getName(), verweis.toString());
		this.verweis = verweis;
	}

	@Override
	public EintragsArt getEintragsArt() {
		return EintragsArt.ABGELEITET;
	}

	/**
	 * liefert den potentiellen Offset (in Tagen) um den der Basiseintrag verschoben
	 * werden soll.
	 * 
	 * @return der Offset
	 */
	public int getOffset() {
		int result = 0;
		if (verweis != null) {
			result = verweis.getOffset();
		}
		return result;
	}

	/**
	 * liefert den Name des verwendeten Eintrags. Wenn kein Eintrag zugewiesen
	 * wurde, wird ein Leerstring geliefert.
	 * 
	 * @return der Name oder ein Leerstring
	 */
	public String getVerweisName() {
		String result = "";
		if (verweis != null) {
			result = verweis.getName();
		}
		return result;
	}

	/**
	 * ermittelt, ob der verwendete Eintrag vom aktuellen negiert wird.
	 * 
	 * @return <code>true</code>, ween er negiert wird
	 */
	public boolean isNegiert() {
		boolean result = false;
		if (verweis != null) {
			result = verweis.isNegiert();
		}
		return result;
	}

	/**
	 * setzt den Verweis, der diesen definieren soll.
	 * 
	 * @param provider
	 *            die Verwaltung aller bekannten Systemkalendereinträge zur
	 *            Verifizierung von Referenzen
	 * @param name
	 *            der name des Verweises
	 * @param offset
	 *            der Offset, um den originale Eintrag verschoben werden soll (in
	 *            Tagen)
	 * @param negiert
	 *            true, wenn der originale Eintrag negiert werden soll
	 * @throws ParseException
	 *             der Name des Verweises ist ungültig
	 */
	public void setVerweis(KalenderEintragProvider provider, final String name, final int offset, final boolean negiert)
			throws ParseException {
		verweis = new Verweis(provider, name, offset, negiert);
	}

	Verweis getVerweis() {
		return verweis;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(getName());
		buffer.append(":=");
		buffer.append(verweis);
		return buffer.toString();
	}

	@Override
	public boolean isGueltig(LocalDateTime zeitPunkt) {
		if (verweis.isUngueltig()) {
			return false;
		}

		KalenderEintrag referenzEintrag = verweis.getReferenzEintrag();
		int tagesOffset = verweis.getOffset();
		boolean gueltig = referenzEintrag.isGueltig(zeitPunkt.plusDays(tagesOffset));

		if (verweis.isNegiert()) {
			return !gueltig;
		}

		return gueltig;
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {

		if (verweis.isUngueltig()) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		KalenderEintrag referenzEintrag = verweis.getReferenzEintrag();
		int tagesOffset = verweis.getOffset();
		SystemkalenderGueltigkeit gueltigKeit = referenzEintrag
				.getZeitlicheGueltigkeit(zeitpunkt.minusDays(tagesOffset));

		LocalDateTime wechselZeit = gueltigKeit.getNaechsterWechsel().getZeitPunkt();
		if (tagesOffset > 0) {
			if (!SystemKalender.MAX_DATETIME.minusDays(tagesOffset).isBefore(wechselZeit)) {
				wechselZeit = wechselZeit.plusDays(tagesOffset);
			} else {
				wechselZeit = SystemKalender.MAX_DATETIME;
			}
		} else {
			wechselZeit = wechselZeit.plusDays(tagesOffset);
		}

		LocalDateTime aktivierungsZeit = gueltigKeit.getErsterWechsel().getZeitPunkt();
		if (SystemKalender.MIN_DATETIME != aktivierungsZeit) {
			aktivierungsZeit = aktivierungsZeit.plusDays(tagesOffset);
		}

		if (verweis.isNegiert()) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(aktivierungsZeit, !gueltigKeit.isZeitlichGueltig()),
					ZustandsWechsel.of(wechselZeit, !gueltigKeit.getNaechsterWechsel().isWirdGueltig()));
		}
		return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(aktivierungsZeit, gueltigKeit.isZeitlichGueltig()),
				ZustandsWechsel.of(wechselZeit, gueltigKeit.getNaechsterWechsel().isWirdGueltig()));
	}

	@Override
	public SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitpunkt) {
		if (verweis.isUngueltig()) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		KalenderEintrag referenzEintrag = verweis.getReferenzEintrag();
		int tagesOffset = verweis.getOffset();
		SystemkalenderGueltigkeit gueltigKeit = referenzEintrag
				.getZeitlicheGueltigkeitVor((zeitpunkt.minusDays(tagesOffset)));

		LocalDateTime wechselZeit = gueltigKeit.getNaechsterWechsel().getZeitPunkt();
		if (tagesOffset > 0) {
			if (!SystemKalender.MAX_DATETIME.minusDays(tagesOffset).isBefore(wechselZeit)) {
				wechselZeit = wechselZeit.plusDays(tagesOffset);
			} else {
				wechselZeit = SystemKalender.MAX_DATETIME;
			}
		} else {
			wechselZeit = wechselZeit.plusDays(tagesOffset);
		}

		LocalDateTime aktivierungsZeit = gueltigKeit.getErsterWechsel().getZeitPunkt();
		if (SystemKalender.MIN_DATETIME != aktivierungsZeit) {
			aktivierungsZeit = aktivierungsZeit.plusDays(tagesOffset);
		}

		if (verweis.isNegiert()) {
			return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(aktivierungsZeit, !gueltigKeit.isZeitlichGueltig()),
					ZustandsWechsel.of(wechselZeit, !gueltigKeit.getNaechsterWechsel().isWirdGueltig()));
		}
		return SystemkalenderGueltigkeit.of(ZustandsWechsel.of(aktivierungsZeit, gueltigKeit.isZeitlichGueltig()),
				ZustandsWechsel.of(wechselZeit, gueltigKeit.getNaechsterWechsel().isWirdGueltig()));
	}

	@Override
	public boolean benutzt(KalenderEintrag referenz) {
		if (verweis == null) {
			return false;
		}
		return verweis.getName().equals(referenz.getName());
	}

	@Override
	public  Set<KalenderEintrag> getAufgeloesteVerweise() {
		if(!isNegiert() && getOffset() == 0) {
			KalenderEintrag referenz = getVerweis().getReferenzEintrag();
			return referenz.getAufgeloesteVerweise();
			
		}
		return Collections.singleton(this);
	}
}

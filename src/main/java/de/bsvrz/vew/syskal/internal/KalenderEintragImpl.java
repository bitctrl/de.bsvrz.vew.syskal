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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalenderEintrag;
import de.bsvrz.vew.syskal.SystemkalenderGueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public abstract class KalenderEintragImpl implements KalenderEintrag {

	private static final Debug LOGGER = Debug.getLogger();

	/** das Pattern eines Datumsbereiches im Definitionsstring. */
	private static final Pattern DATUMSBEREICH_PATTERN = Pattern.compile("<.*>");

	/** das Pattern eines Zeitgrenzenbereiches im Definitionsstring. */
	private static final Pattern ZEITBEREICHSLISTE_PATTERN = Pattern.compile("\\(.*\\)");

	/** das Pattern eines Zeitbereiches oder einer Verknüpfungsliste. */
	protected static final Pattern ZEITBEREICH_PATTERN = Pattern.compile("\\{.[^\\{]*\\}");

	/**
	 * zerlegt den übergebenen Definitionsstring. Die Funktion initialisiert die
	 * Datenstrukturen der Klasse und wird nur vom Konstruktor aufgerufen, d.h. ein
	 * mehrfacher Aufruf könnte zu falschen Daten führen!
	 * 
	 * @param provider
	 *            die Verwaltung aller bekannten Systemkalendereinträge zur
	 *            Verifizierung von Referenzen
	 * @param name
	 *            der Name des Dateneintrages
	 * @param definition
	 *            der Definitionsstring
	 * @return das Ergebnis ist ein Systemkalendereintrag, dessen konkreter Typ vom
	 *         Inhalt der Definition abhängt
	 */
	public static KalenderEintragImpl parse(KalenderEintragProvider provider, final String name,
			final String definition) {

		KalenderEintragImpl result = null;

		result = VorDefinierterEintrag.getEintrag(name);
		if (result != null) {
			return result;
		}

		String rest = entferneNamensPrefix(name, definition);

		final List<ZeitGrenze> parseZeitBereiche = new ArrayList<>();

		Matcher mat = KalenderEintragImpl.ZEITBEREICHSLISTE_PATTERN.matcher(rest);

		boolean zeitBereichsfehler = false;

		while (mat.find()) {
			final String bereich = mat.group();
			rest = rest.replace(bereich, "");
			final String zeitBereich = bereich.substring(1, bereich.length() - 1);
			final Matcher zeitMat = KalenderEintragImpl.ZEITBEREICH_PATTERN.matcher(zeitBereich);
			while (zeitMat.find()) {
				String zb = zeitMat.group();
				zb = zb.substring(1, zb.length() - 1);
				try {
					parseZeitBereiche.add(new ZeitGrenze(zb));
				} catch (final ParseException e) {
					LOGGER.warning(e.getLocalizedMessage());
					zeitBereichsfehler = true;
				}
			}
		}

		// und bzw. oder Einträge ermitteln

		if (rest.toLowerCase().startsWith("und")) {
			result = new UndVerknuepfung(provider, name, rest.substring("und".length()));
		} else if (rest.toLowerCase().startsWith("oder")) {
			result = new OderVerknuepfung(provider, name, rest.substring("oder".length()));
		} else {
			mat = KalenderEintragImpl.DATUMSBEREICH_PATTERN.matcher(rest);
			if (mat.find()) {
				result = new ZeitBereichsEintrag(name, mat.group().substring(1, mat.group().length() - 1));
				final String bereich = mat.group();
				rest = rest.replace(bereich, "");
			} else {
				if (rest.trim().length() == 0) {
					result = new ZeitBereichsEintrag(name, "");
				} else {
					if (rest.contains(",")) {
						result = new DatumsEintrag(name, rest);
					} else {
						result = new VerweisEintrag(provider, name, rest);
					}
				}
			}

			result.komprimiereZeitBereiche(parseZeitBereiche.stream().sorted().collect(Collectors.toList()));
		}

		result.definition = definition;

		if (zeitBereichsfehler) {
			result.setFehler(true);
		}

		return result;
	}

	private void komprimiereZeitBereiche(List<ZeitGrenze> grenzen) {

		ZeitGrenze aktuell = null;

		for (ZeitGrenze grenze : grenzen) {
			if (aktuell == null) {
				aktuell = grenze;
				continue;
			}

			if (grenze.getStart().isAfter(aktuell.getEnde())) {
				zeitGrenzen.add(aktuell);
				aktuell = grenze;
			} else {
				if (!grenze.getEnde().isBefore(aktuell.getEnde())) {
					aktuell = new ZeitGrenze(aktuell.getStart(), grenze.getEnde());
				}
			}
		}

		if (aktuell != null) {
			zeitGrenzen.add(aktuell);
		}
	}

	private static String entferneNamensPrefix(final String name, final String definition) {

		final String[] parts = definition.split(":=");

		if (parts.length < 2) {
			return parts[0].trim();
		}

		final String defName = parts[0].trim();
		if (!defName.equals(name)) {
			LOGGER.warning("Für den Systemkalendereintrag " + name + " ist der abweichende Name: \""
					+ defName + "\" definiert!");
		}
		return parts[1].trim();
	}

	private String definition;

	private String name;

	/** die Zeitgrenzen, die den Kalendereintrag zeitlich einschränken können. */
	private final List<ZeitGrenze> zeitGrenzen = new ArrayList<>();

	/** der Definitionseintrag konnte nicht korrekt eingelesen werden. */
	private boolean fehler;

	protected KalenderEintragImpl(String name, String definition) {
		this.name = name;
		this.definition = definition;
	}

	/**
	 * fügt eine Zeitgrenze hinzu.
	 * 
	 * @param grenze
	 *            die neue Zeitgrenze
	 */
	public void addZeitGrenze(final ZeitGrenze grenze) {
		zeitGrenzen.add(grenze);
	}

	protected abstract SystemkalenderGueltigkeit berechneZeitlicheGueltigkeit(LocalDateTime zeitpunkt);

	protected abstract SystemkalenderGueltigkeit berechneZeitlicheGueltigkeitsVor(LocalDateTime zeitpunkt);

	/**
	 * liefert die Zeichenkette mit der initialen Definitionszeichenkette des
	 * Eintrags.
	 * 
	 * @return die Definition als Zeichenkette
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * liefert die Art des Dateneintrags.
	 * 
	 * @return die Art
	 */
	public abstract EintragsArt getEintragsArt();

	public String getName() {
		return name;
	}

	/**
	 * liefert die Liste der für den Eintrag definierten Zeitgrenzen.
	 * 
	 * TODO Klärung, welche Eintragsarten die Zuordnung von Zeitgrenzen erlauben!
	 * 
	 * @return die Liste der definierten Grenzen
	 */
	public List<ZeitGrenze> getZeitGrenzen() {
		return zeitGrenzen;
	}

	@Override
	public boolean isGueltig(LocalDateTime zeitPunkt) {
		return getZeitlicheGueltigkeit(zeitPunkt).isZeitlichGueltig();
	}

	@Override
	public final SystemkalenderGueltigkeit getZeitlicheGueltigkeit(LocalDateTime zeitpunkt) {
		if (fehler) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		return berechneZeitlicheGueltigkeit(zeitpunkt);
	}

	@Override
	public SystemkalenderGueltigkeit getZeitlicheGueltigkeitVor(LocalDateTime zeitPunkt) {
		if (fehler) {
			return SystemkalenderGueltigkeit.NICHT_GUELTIG;
		}

		return berechneZeitlicheGueltigkeitsVor(zeitPunkt);
	}

	@Override
	public final List<ZustandsWechsel> getZustandsWechsel(LocalDateTime start, LocalDateTime ende) {

		if (isFehler()) {
			return Collections.singletonList(ZustandsWechsel.zuUnGueltig(start));
		}

		List<ZustandsWechsel> result = new ArrayList<>();

		SystemkalenderGueltigkeit gueltigkeit = getZeitlicheGueltigkeit(start);
		result.add(ZustandsWechsel.of(start, gueltigkeit.isZeitlichGueltig()));

		LocalDateTime aktuellerZeitPunkt = start;
		do {
			ZustandsWechsel wechsel = gueltigkeit.getNaechsterWechsel();
			aktuellerZeitPunkt = wechsel.getZeitPunkt();

			if (!aktuellerZeitPunkt.isAfter(ende)) {
				result.add(wechsel);
				gueltigkeit = getZeitlicheGueltigkeit(wechsel.getZeitPunkt());
			}

		} while (!aktuellerZeitPunkt.isAfter(ende));

		return result;
	}

	/**
	 * ermittelt, ob der Eintrag fehlerhaft eingelesen wurde.
	 * 
	 * @return true, wenn der Definitionseintrag nicht korrekt interpretiert werden
	 *         konnte
	 */
	public boolean isFehler() {
		return fehler;
	}

	/**
	 * setzt den Fehlerstatus des Eintrags.
	 * 
	 * @param state
	 *            der Status
	 */
	protected void setFehler(final boolean state) {
		fehler = state;
	}

	abstract boolean benutzt(SystemKalenderEintrag referenz);

	@Override
	public boolean isVerwendbar() {
		return !isFehler();
	}
}

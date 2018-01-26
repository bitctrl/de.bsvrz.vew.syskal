/*
 * Rahmenwerk-Plug-in "Systemkalender"
 * Copyright (C) 2009 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weissenfelser Strasse 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.vew.syskal.syskal.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Repräsentation der Daten eines {@link KalenderEintragDefinition}, der durch die
 * logische Verknüpfung mehrerer anderer Einträge definiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public abstract class LogischerVerkuepfungsEintrag extends
		KalenderEintragDefinition {

	/** die Liste der Verweise, die den Eintrag definieren. */
	private final List<Verweis> verweise = new ArrayList<>();

	/**
	 * ein optionales Anfangsjahr, mit dem der Gültigkeitsbereich des Eintrags
	 * eingeschränkt werden kann.
	 */
	private int startJahr;

	/**
	 * ein optionales Endjahr, mit dem der Gültigkeitsbereich des Eintrags
	 * eingeschränkt werden kann.
	 */
	private int endJahr;

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            der definierende Textstring des Eintrags
	 */
	LogischerVerkuepfungsEintrag(KalenderEintragProvider provider, final String name, final String definition) {
		super(name, definition);

		if (definition != null) {
			String rest = definition;

			final Matcher mat = KalenderEintragDefinition.ZEITBEREICH_PATTERN
					.matcher(rest);
			while (mat.find()) {
				String elemente = mat.group();
				rest = rest.replace(elemente, "").trim();
				elemente = elemente.substring(1, elemente.length() - 1);
				final String[] verweisDefinitionen = elemente.split(",");
				for (final String def : verweisDefinitionen) {
					try {
						Verweis verweis = new Verweis(provider, def);
						setFehler(verweis.isUngueltig() | isFehler());
						verweise.add(verweis);
					} catch (final ParseException e) {
						setFehler(true);
					}
				}
			}

			if (verweise.size() <= 0) {
				setFehler(true);
			}

			final String[] parts = rest.split(",");
			if (parts.length > 0) {
				if (!"*".equals(parts[0].trim())) {
					try {
						startJahr = Integer.parseInt(parts[0]);
					} catch (final NumberFormatException e) {
						Debug.getLogger().finest(e.getLocalizedMessage());
						// Jahr wird als nicht gesetzt angenommen
					}
				}
			}
			if (parts.length > 1) {
				if (!"*".equals(parts[1].trim())) {
					try {
						endJahr = Integer.parseInt(parts[1]);
					} catch (final NumberFormatException e) {
						Debug.getLogger().finest(e.getLocalizedMessage());
						// Jahr wird als nicht gesetzt angenommen
					}
				}
			}
		}
	}

	/**
	 * fügt einen zu verknüpfenden Verweis hinzu.
	 * 
	 * @param verweis
	 *            der neue Verweis
	 */
	public final void addVerweis(final Verweis verweis) {
		if (verweis != null) {
			verweise.add(verweis);
		}
	}

	@Override
	public EintragsArt getEintragsArt() {
		return EintragsArt.VERKNUEPFT;
	}

	/**
	 * liefert das optional beschränkende Endjahr. 0 steht für ein
	 * unbeschränktes Ende.
	 * 
	 * @return das Jahr
	 */
	public int getEndJahr() {
		return endJahr;
	}

	/**
	 * liefert das optional beschränkende Anfangsjahr. 0 steht für einen
	 * unbeschränkten Anfang.
	 * 
	 * @return das Jahr
	 */
	public int getStartJahr() {
		return startJahr;
	}

	/**
	 * liefert die Art der logischen Verknüpfung als Textstring.
	 * 
	 * @return den Text
	 */
	public abstract String getVerknuepfungsArt();

	/**
	 * liefert die Liste der Verweise, die den Eintrag definieren.
	 * 
	 * @return die Liste
	 */
	public List<Verweis> getVerweise() {
		return verweise;
	}

	/**
	 * setzt das optionale Endjahr des Eintrags.
	 * 
	 * @param endJahr
	 *            das Jahr
	 */
	public void setEndJahr(final int endJahr) {
		this.endJahr = endJahr;
	}

	/**
	 * setzt das optionale Anfangsjahr des Eintrags.
	 * 
	 * @param startJahr
	 *            das Jahr
	 */
	public void setStartJahr(final int startJahr) {
		this.startJahr = startJahr;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(getName());
		buffer.append(":=");
		buffer.append(getVerknuepfungsArt());
		buffer.append('{');
		int idx = 0;
		for (final Verweis verweis : verweise) {
			if (idx > 0) {
				buffer.append(',');
			}
			buffer.append(verweis);
			idx++;
		}
		buffer.append('}');

		if (startJahr > 0) {
			buffer.append(startJahr);
		} else {
			buffer.append('*');
		}
		buffer.append(',');
		if (endJahr > 0) {
			buffer.append(endJahr);
		} else {
			buffer.append('*');
		}

		return buffer.toString();
	}
}

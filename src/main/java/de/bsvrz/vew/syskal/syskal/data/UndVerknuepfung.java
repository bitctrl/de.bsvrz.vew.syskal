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

import java.time.LocalDateTime;

/**
 * Repräsentation einer logischen Verknüpfung mehrere Systemkalendereinträge mit
 * UND.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class UndVerknuepfung extends LogischerVerkuepfungsEintrag {

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            der Name des Eintrags
	 * @param definition
	 *            der definierende String des Eintrags
	 */
	public UndVerknuepfung(KalenderEintragProvider provider, final String name, final String definition) {
		super(provider, name, definition);
	}

	@Override
	public String getVerknuepfungsArt() {
		return "UND";
	}

	@Override
	public Gueltigkeit getGueltigKeit(LocalDateTime zeitpunkt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ZustandsWechsel getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {
		// TODO Auto-generated method stub
		return null;
	}
}

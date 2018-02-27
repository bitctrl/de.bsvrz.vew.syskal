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
import java.util.Locale;

import de.bsvrz.vew.syskal.KalenderEintrag;

/**
 * Repräsentation eines Verweises auf einen anderen Systemkalendereintrag.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class Verweis {

    /** der Name des Verweis. */
    private String name;

    /**
     * der Offset (in Tagen) um den der originale Eintrag verschoben werden
     * soll.
     */
    private int offset;

    /** definiert, ob der originale Eintrag negiert werden soll. */
    private boolean negiert;

    private final boolean ungueltig;

    private KalenderEintrag referenzEintrag;

    public static Verweis of(KalenderEintragProvider provider, String name, int offset, boolean negiert) {
        try {
            return new Verweis(provider, name, offset, negiert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Konstruktor.
     * 
     * @param provider
     *            die Verwaltung aller bekannten Systemkalendereinträge zur
     *            Verifizierung von Referenzen
     * @param def
     *            der Text, der den Verweis definiert
     * @throws ParseException
     *             der übergeben Text kann nicht als Verweis interprtiert werden
     */
    public Verweis(KalenderEintragProvider provider, final String def) throws ParseException {
        if (def != null) {
            String rest = def;

            final String negationsFilter = rest.toLowerCase(Locale.getDefault());
            if (negationsFilter.contains("nicht")) {
                negiert = true;
                rest = rest.substring(negationsFilter.indexOf("nicht") + "nicht".length()).trim();
            }

            final String[] parts = rest.split("[+-]");
            name = parts[0].trim();
            if (parts.length > 1) {
                final String wert = parts[1].trim().replaceAll("[TAGEtage() ]", "");
                offset = Integer.parseInt(wert);
                if (rest.contains("-")) {
                    offset *= -1;
                }
            }

            if (name.length() < 0) {
                throw new ParseException("Der String \"" + def + "\" kann nicht als Verweis interpretiert werden!", 0);
            }

            referenzEintrag = provider.getKalenderEintrag(name);
            ungueltig = referenzEintrag == null;

        } else {
            throw new ParseException("Der String \"null\" kann nicht als Verweis interpretiert werden!", 0);
        }
    }

    /**
     * Konstruktor.
     * 
     * @param provider
     *            die Verwaltung aller bekannten Systemkalendereinträge zur
     *            Verifizierung von Referenzen
     * @param name
     *            der Name
     * @param offset
     *            der Offset des originaleintrags
     * @param negiert
     *            Negation des Originaleintrags
     * @throws ParseException
     *             der Name des Verweises ist ungültig
     */
    public Verweis(KalenderEintragProvider provider, final String name, final int offset, final boolean negiert)
            throws ParseException {
        this.name = name.trim();
        this.offset = offset;
        this.negiert = negiert;
        if (name.length() <= 0) {
            throw new ParseException("Der Verweisname darf nicht leer sein!", 0);
        }
        ungueltig = provider.getKalenderEintrag(name) == null;
    }

    /**
     * liefert den Name des Verweiseintrags.
     * 
     * @return den Name
     */
    public String getName() {
        return name;
    }

    /**
     * liefert den Offset auf den Verweiseintrag.
     * 
     * @return den Offset (in Tagen)
     */
    public int getOffset() {
        return offset;
    }

    public KalenderEintrag getReferenzEintrag() {
        return referenzEintrag;
    }

    /**
     * ermittelt, ob der Verweiseintrag negiert werden soll.
     * 
     * @return <code>true</code>, wenn negiert werden soll
     */
    public boolean isNegiert() {
        return negiert;
    }

    public boolean isUngueltig() {
        return ungueltig;
    }

    /**
     * setzt den Name des Eintrags.
     * 
     * @param name
     *            der neue Name
     * @throws ParseException
     *             der übergebene Name ist nicht zulässig
     */
    public void setName(final String name) throws ParseException {
        final String neuerName = name.trim();
        if (neuerName.length() <= 0) {
            throw new ParseException("Ein leerer Name für einen Verweis ist nicht zulässig!", 0);
        }
        this.name = neuerName;
    }

    /**
     * definiert den Negationszustand des Verweises.
     * 
     * @param negiert
     *            der Zustand
     */
    public void setNegiert(final boolean negiert) {
        this.negiert = negiert;
    }

    /**
     * setzt den Offset des Verweises.
     * 
     * @param offset
     *            der Offset
     */
    public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();

        if (negiert) {
            buffer.append("NICHT ");
        }

        buffer.append(name);

        if (offset != 0) {
            if (offset > 0) {
                buffer.append('+');
            }
            buffer.append(offset);

            if (Math.abs(offset) == 1) {
                buffer.append(" Tag");
            } else {
                buffer.append(" Tage");
            }
        }
        return buffer.toString();
    }
}

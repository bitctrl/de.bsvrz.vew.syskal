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

import javax.swing.event.EventListenerList;

/**
 * Verwaltungsklasse, welche die benötigten ListenerfunktionalitÃÂ¤ten bereitstellt
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class BenachrichtigeListenerVerwaltung
{
  /**
   * Liste der Listener
   */
  private EventListenerList listeners = new EventListenerList();

  public int addBenachrichtigeListenerStelle(BenachrichtigeListener listener)
  {
    listeners.add(BenachrichtigeListener.class, listener);
    return listeners.getListenerCount();
  }

  public void addBenachrichtigeListener(BenachrichtigeListener listener)
  {
    listeners.add(BenachrichtigeListener.class, listener);
  }

  public BenachrichtigeListener getBenachrichtigeListener(int count)
  {
    BenachrichtigeListener[] list = listeners.getListeners(BenachrichtigeListener.class);
    return list[count];
  }

  public void removeBenachrichtigeListener(BenachrichtigeListener listener)
  {
    listeners.remove(BenachrichtigeListener.class, listener);
  }

  public synchronized void meldeAnAlle(BenachrichtigeEvent e)
  {
    for (BenachrichtigeListener l : listeners.getListeners(BenachrichtigeListener.class))
      l.update(e);
  }

}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalenderException;

public class EintragsVerwaltung implements KalenderEintragProvider, ClientReceiverInterface, MutableSetChangeListener {

    // private ObservableSet<SystemKalenderEintrag> eintraege =
    // FXCollections.observableSet(new LinkedHashSet<>());
    private Map<SystemObject, SystemKalenderEintrag> eintraege = new LinkedHashMap<>();

    private ClientDavInterface dav;
    private DataDescription dataDescription;
    private MutableSet eintragsSet;
    private boolean connectionLost = false;

    public Collection<SystemKalenderEintrag> getSystemKalenderEintraege() throws SystemKalenderException {
        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }
        return Collections.unmodifiableCollection(eintraege.values());
    }

    public EintragsVerwaltung(ClientDavInterface dav, ConfigurationObject kalenderObject) {
        if (!kalenderObject.isOfType("typ.kalender")) {
            throw new IllegalStateException("Das Objekt " + kalenderObject + " ist nicht vom Typ \"typ.kalender\"!");
        }

        this.dav = dav;
        this.dav.addConnectionListener(connection -> invalidate());

        AttributeGroup attributeGroup = dav.getDataModel().getAttributeGroup("atg.systemKalenderEintrag");
        Aspect aspect = dav.getDataModel().getAspect("asp.parameterSoll");
        dataDescription = new DataDescription(attributeGroup, aspect);

        eintragsSet = kalenderObject.getMutableSet("SystemKalenderEinträge");
        eintragsSet.addChangeListener(this);

        addEintraege(eintragsSet.getElements());
    }

    @Override
    public KalenderEintrag getKalenderEintrag(String name) {
        Optional<SystemKalenderEintrag> systemKalenderEintrag = eintraege.values().stream()
                .filter(s -> name.equals(s.getName())).findFirst();
        if (systemKalenderEintrag.isPresent()) {
            return systemKalenderEintrag.get().getKalenderEintrag();
        }
        return null;
    }

    private void addEintraege(Collection<SystemObject> list) {

        for (SystemObject obj : list) {

            SystemKalenderEintrag neuerEintrag = new SystemKalenderEintrag(this, (DynamicObject) obj);
            SystemKalenderEintrag alterEintrag = eintraege.put(obj, neuerEintrag);
            if (alterEintrag != null) {
                dav.unsubscribeReceiver(this, alterEintrag.getSystemObject(), dataDescription);
            }
            dav.subscribeReceiver(this, obj, dataDescription, ReceiveOptions.normal(), ReceiverRole.receiver());
        }
    }

    private void berechneAbhaengigeKalenderEintraegeNeu(Collection<SystemKalenderEintrag> referenzen) {

        Collection<SystemKalenderEintrag> berechnungsListe = new ArrayList<>(eintraege.values());
        for (SystemKalenderEintrag eintrag : berechnungsListe) {
            if (referenzen.contains(eintrag)) {
                continue;
            }

            eintrag.aktualisiereVonReferenzen(referenzen);
        }
    }

    private void invalidate() {
        eintraege.clear();
        if (eintragsSet != null) {
            eintragsSet.removeChangeListener(this);
        }
        connectionLost = true;
    }

    private void removeEintraege(Collection<SystemObject> list) {

        Collection<SystemKalenderEintrag> alteEintrage = new ArrayList<>();
        for (SystemObject object : list) {
            SystemKalenderEintrag alterEintrag = eintraege.remove(object);
            if (alterEintrag != null) {
                alteEintrage.add(alterEintrag);
                dav.unsubscribeReceiver(this, object, dataDescription);
            }
        }
        berechneAbhaengigeKalenderEintraegeNeu(alteEintrage);
    }

    @Override
    public void update(ResultData[] results) {

        for (ResultData result : results) {
            if (result.hasData()) {
                SystemKalenderEintrag systemKalenderEintrag = eintraege.get(result.getObject());
                if (systemKalenderEintrag != null) {
                    systemKalenderEintrag.setDefinition(result.getData().getTextValue("Definition").getText());
                    berechneAbhaengigeKalenderEintraegeNeu(Collections.singleton(systemKalenderEintrag));
                }
            }
        }
    }

    @Override
    public void update(MutableSet set, SystemObject[] addedObjects, SystemObject[] removedObjects) {
        removeEintraege(Arrays.asList(removedObjects));
        addEintraege(Arrays.asList(addedObjects));
    }

    public SystemKalenderEintrag getSystemKalenderEintrag(SystemObject object) throws SystemKalenderException {
        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }
        return eintraege.get(object);
    }
}

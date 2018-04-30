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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.dynobj.DynObjektException;
import de.bsvrz.sys.funclib.dynobj.DynamischeObjekte;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemKalenderEintrag;
import de.bsvrz.vew.syskal.SystemKalenderException;
import de.bsvrz.vew.syskal.SystemKalenderListener;

/**
 * Modul zur Verwaltung der Einträge eines {@link SystemKalender}.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class EintragsVerwaltung implements KalenderEintragProvider, ClientReceiverInterface, MutableSetChangeListener {

    private Map<SystemObject, SystemKalenderEintrag> eintraege = new ConcurrentHashMap<>();

    private ClientDavInterface dav;
    private DataDescription parameterSollDescription;
    private DataDescription parameterVorgabeDescription;
    private MutableSet eintragsSet;
    private boolean connectionLost = false;

    private final PropertyChangeListener kalenderEintragsChangeListener = e -> fireEintragGeandert(
            (SystemKalenderEintrag) e.getSource());

    private List<SystemKalenderListener> kalenderListeners = new CopyOnWriteArrayList<>();

    private SystemKalender kalender;

    /**
     * liefert die Menge der verwalteten Systemkalendereinträge.
     * 
     * @return die Menge der Einträge
     * @throws SystemKalenderException
     *             die Systemkalendereinträge konnten nicht ermittelt werden,
     *             weil die Datenverteilerverbindung nicht mehr besteht
     */
    public Collection<SystemKalenderEintrag> getSystemKalenderEintraege() throws SystemKalenderException {
        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }
        return Collections.unmodifiableCollection(eintraege.values());
    }

    /**
     * Initialisiert die Verwaltung für den übergebenen Systemkalender, der das
     * angegebene Kalenderobjekt aus dem Modell der Datenverteilerverbindung
     * repräsentiert.
     * 
     * @param kalender
     *            der {@link SystemKalender}
     * @param dav
     *            die Datenverteilerverbindung
     * @param kalenderObject
     *            das Kalenderobjekt innerhalb des Datenverteilermodells
     */
    public EintragsVerwaltung(SystemKalender kalender, ClientDavInterface dav, ConfigurationObject kalenderObject) {
        if (!kalenderObject.isOfType("typ.kalender")) {
            throw new IllegalStateException("Das Objekt " + kalenderObject + " ist nicht vom Typ \"typ.kalender\"!");
        }

        this.kalender = kalender;
        this.dav = dav;
        this.dav.addConnectionListener(connection -> invalidate());

        AttributeGroup attributeGroup = dav.getDataModel().getAttributeGroup("atg.systemKalenderEintrag");
        Aspect aspect = dav.getDataModel().getAspect("asp.parameterSoll");
        parameterSollDescription = new DataDescription(attributeGroup, aspect);

        aspect = dav.getDataModel().getAspect("asp.parameterVorgabe");
        parameterVorgabeDescription = new DataDescription(attributeGroup, aspect);

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
                dav.unsubscribeReceiver(this, alterEintrag.getSystemObject(),
                        parameterSollDescription);
            }
            fireEintragAngelegt(neuerEintrag);
            neuerEintrag.addKalenderEintragChangeListener(kalenderEintragsChangeListener);
            dav.subscribeReceiver(this, obj, parameterSollDescription, ReceiveOptions.normal(),
                    ReceiverRole.receiver());
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
        fireKalenderGetrennt();
        connectionLost = true;
    }

    private void removeEintraege(Collection<SystemObject> list) {

        Collection<SystemKalenderEintrag> alteEintrage = new ArrayList<>();
        for (SystemObject object : list) {
            SystemKalenderEintrag alterEintrag = eintraege.remove(object);
            if (alterEintrag != null) {
                alterEintrag.removeKalenderEintragChangeListener(kalenderEintragsChangeListener);
                fireEintragEntfernt(alterEintrag);
                alteEintrage.add(alterEintrag);
                dav.unsubscribeReceiver(this, object, parameterSollDescription);
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

    /**
     * liefert den Systemkalendereintrg, der durch das übergebene Systemobjekt
     * definiert wird.
     * 
     * @param object
     *            das Systemobjekt
     * @return den Systemkalendereintrag oder null, wenn kein entsprechender
     *         Eintrag gefunden verwaltet wird.
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht ermittelt werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist
     */
    public SystemKalenderEintrag getSystemKalenderEintrag(SystemObject object) throws SystemKalenderException {
        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }
        return eintraege.get(object);
    }

    /**
     * speichert den übergebenen Systemkalendereintrag.
     * 
     * In Datenverteilermodell wird der entsprechende Eintrag aktualisiert oder
     * ein neuer angelegt.
     * 
     * @param eintrag
     *            der Eintrag, der gesichert werden soll
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht gespeichert werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist oder ein
     *             Fehler beim Anlegen oder Aktualisieren des gewünschten
     *             dynamischen Systemobjekts aufgetreten ist
     */
    public void sichereEintrag(SystemKalenderEintrag eintrag) throws SystemKalenderException {
        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }

        if (eintrag.getSystemObject() == null) {
            erzeugeSystemkalenderEintrag(eintrag);
        } else {
            aktualisiereEintrag(eintrag.getSystemObject(), eintrag);
        }
    }

    /**
     * löscht den übergebenen Systemkalendereintrag.
     * 
     * In Datenverteilermodell wird der entsprechende Eintrag entfernt.
     * 
     * @param eintrag
     *            der Eintrag, der gelöscht werden soll
     * @throws SystemKalenderException
     *             der Systemkalendereintrag kann nicht gelöscht werden, weil
     *             die Datenverteilerverbindung verloren gegangen ist oder ein
     *             Fehler beim Löschen des entsprechenden dynamischen
     *             Systemobjekts aufgetreten ist
     */
    public void loescheEintrag(SystemKalenderEintrag eintrag) throws SystemKalenderException {

        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }

        final DynamicObject objekt = (DynamicObject) eintrag.getSystemObject();
        if (objekt == null) {
            throw new SystemKalenderException(
                    "Der Systemkalendereintrag existiert nicht als dynamisches Objekt in der Datenverteilerkonfiguration");
        }

        final DynamischeObjekte dynamischeVerwaltung = DynamischeObjekte.getInstanz(dav);
        try {
            dynamischeVerwaltung.entferneObjektAusMenge(objekt, eintragsSet, true);
        } catch (final DynObjektException e) {
            throw new SystemKalenderException(
                    "Fehler beim Löschen des Systemkalendereintrags: " + eintrag.getSystemObject(), e);
        }
    }

    private void aktualisiereEintrag(SystemObject systemObject, SystemKalenderEintrag eintrag) {

        final Data daten = dav.createData(parameterVorgabeDescription.getAttributeGroup());
        daten.getTextValue("Definition").setText(eintrag.getKalenderEintrag().toString());
        final ResultData resultData = new ResultData(systemObject,
                parameterVorgabeDescription, System.currentTimeMillis(), daten);
        try {
            dav.subscribeSender(new ClientSenderInterface() {

                @Override
                public boolean isRequestSupported(SystemObject object, DataDescription dataDescription) {
                    return true;
                }

                @Override
                public void dataRequest(SystemObject object, DataDescription dataDescription, byte state) {
                    if (state == ClientSenderInterface.START_SENDING) {
                        try {
                            dav.sendData(resultData);
                        } catch (DataNotSubscribedException | SendSubscriptionNotConfirmed e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        dav.unsubscribeSender(this, object, dataDescription);
                    }
                }
            }, systemObject, parameterVorgabeDescription, SenderRole.sender());
        } catch (OneSubscriptionPerSendData e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void erzeugeSystemkalenderEintrag(SystemKalenderEintrag eintrag) throws SystemKalenderException {

        final DynamischeObjekte dynamischeVerwaltung = DynamischeObjekte.getInstanz(dav);
        SystemObject systemObject = dav.getDataModel().getObject(eintrag.getPid());
        if (systemObject == null) {
            try {
                systemObject = dynamischeVerwaltung.erzeugeObjekt(
                        (DynamicObjectType) dav.getDataModel().getType("typ.systemKalenderEintrag"), eintrag.getName(),
                        eintrag.getPid());
            } catch (final DynObjektException e) {
                throw new SystemKalenderException(e.getLocalizedMessage(), e);
            }
        }

        aktualisiereEintrag(systemObject, eintrag);

        if (!eintragsSet.getElements().contains(systemObject)) {
            try {
                dynamischeVerwaltung.fuegeObjektInMengeEin((DynamicObject) systemObject,
                        eintragsSet);
            } catch (final DynObjektException e) {
                throw new SystemKalenderException(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * entfernt alle Systemkalendereinträge aus dem verwalteten Kalender.
     * 
     * @throws SystemKalenderException
     *             die Kalender konnte nicht geleert werden, weil die
     *             Datenverteilerverbindung nicht mehr besteht oder ein Fehler
     *             beim Löschen eines dynamischen Objekts aufgetreten ist
     */
    public void leereSystemKalender() throws SystemKalenderException {

        final DynamischeObjekte dynamischeVerwaltung = DynamischeObjekte.getInstanz(dav);
        try {
            dynamischeVerwaltung.entferneAlleObjekteAusMenge(eintragsSet, true);
        } catch (DynObjektException e) {
            throw new SystemKalenderException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * entfernt alle Systemkalendereinträge, die in der
     * Datenverteilerkonfigration existieren und nicht im verwalteten Kalender
     * enthalten sind.
     * 
     * @throws SystemKalenderException
     *             die Kalender konnte nicht geleert werden, weil die
     *             Datenverteilerverbindung nicht mehr besteht oder ein Fehler
     *             beim Löschen eines dynamischen Objekts aufgetreten ist
     */
    public void bereinigeSystemKalender() throws SystemKalenderException {
        if (connectionLost) {
            throw new SystemKalenderException(
                    "Die für den Systemkalender verwendete Datenverteilerverbindung wurde geschlossen");
        }

        final DynamischeObjekte dynamischeVerwaltung = DynamischeObjekte.getInstanz(dav);
        try {
            dynamischeVerwaltung.loescheAlleNichtZugeordnetenObjekte(
                    (DynamicObjectType) dav.getDataModel().getType("typ.systemKalenderEintrag"), eintragsSet);
        } catch (DynObjektException e) {
            throw new SystemKalenderException(
                    e.getLocalizedMessage(), e);
        }
    }

    /**
     * fügt der Verwaltung einen Listener hinzu, der bei Änderungen im
     * Systemkalender benachrichtigt wird.
     * 
     * @param listener
     *            der Listener
     */
    public void addSystemKalenderListener(SystemKalenderListener listener) {
        kalenderListeners.add(listener);
    }

    /**
     * entfernt einen Listener von der Verwaltung, der bei Änderungen im
     * Systemkalender benachrichtigt wurde.
     * 
     * @param listener
     *            der Listener
     */
    public void removeSystemKalenderListener(SystemKalenderListener listener) {
        kalenderListeners.remove(listener);
    }

    private void fireEintragAngelegt(SystemKalenderEintrag eintrag) {
        for (SystemKalenderListener listener : kalenderListeners) {
            listener.eintragAngelegt(kalender, eintrag);
        }
    }

    private void fireEintragGeandert(SystemKalenderEintrag eintrag) {
        for (SystemKalenderListener listener : kalenderListeners) {
            listener.eintragGeandert(kalender, eintrag);
        }
    }

    private void fireEintragEntfernt(SystemKalenderEintrag eintrag) {
        for (SystemKalenderListener listener : kalenderListeners) {
            listener.eintragEntfernt(kalender, eintrag);
        }
    }

    private void fireKalenderGetrennt() {
        for (SystemKalenderListener listener : kalenderListeners) {
            listener.kalenderGetrennt(kalender);
        }
    }

}

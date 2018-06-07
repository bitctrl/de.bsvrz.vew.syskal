package de.bsvrz.vew.syskal.lifetests;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationAuthority;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.dynobj.DynObjektException;
import de.bsvrz.sys.funclib.dynobj.DynamischeObjekte;

public class KalenderTestDatenGenerator implements StandardApplication {

    private class KalenderRequest {

        private String name;
        private String definition;
        public String pid;

        public KalenderRequest(String name, String definition) {
            this.name = name;
            this.definition = definition;
            this.pid = "ske." + name.toLowerCase();
        }
    }

    KalenderRequest[] requests = { new KalenderRequest("1_Feiertag",
            "ODER{1_Weihnachtstag,Ostersonntag,Pfingstsonntag}*,*"),
            new KalenderRequest("1_Weihnachtstag", "25.12.*,*"),
            new KalenderRequest("2_Feiertag",
                    "ODER{2_Weihnachtstag,Ostermontag,Pfingstmontag}*,*"),
            new KalenderRequest("2_Weihnachtstag", "26.12.*,*"),
            new KalenderRequest("2012_Ferien", "ODER{2012_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2012_Ferien_Weihnachten", "<24.12.2012-05.01.2013>"),
            new KalenderRequest("2012_Winter_Drittel", "<01.11.2012-28.02.2013>"),
            new KalenderRequest("2013_Ferien",
                    "ODER{2013_Ferien_Ostern,2013_Ferien_Pfingsten,2013_Ferien_Sommer,2013_Ferien_Herbst,2013_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2013_Ferien_Herbst", "<27.10.2013-03.11.2013>"),
            new KalenderRequest("2013_Ferien_Ostern", "<24.03.2013-07.04.2013>"),
            new KalenderRequest("2013_Ferien_Pfingsten", "<21.05.2013-02.06.2013>"),
            new KalenderRequest("2013_Ferien_Sommer", "<25.07.2013-08.09.2013>"),
            new KalenderRequest("2013_Ferien_Weihnachten", "<23.12.2013-05.01.2014>"),
            new KalenderRequest("2013_Frühling_Drittel", "<01.03.2013-30.06.2013>"),
            new KalenderRequest("2013_SommerHerbst_Drittel", "<01.07.2013-31.10.2013>"),
            new KalenderRequest("2013_Winter_Drittel", "<01.11.2013-28.02.2014>"),
            new KalenderRequest("2014_Ferien",
                    "ODER{2014_Ferien_Ostern,2014_Ferien_Pfingsten,2014_Ferien_Sommer,2014_Ferien_Herbst,2014_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2014_Ferien_Herbst", "<26.10.2014-02.11.2014>"),
            new KalenderRequest("2014_Ferien_Ostern", "<13.04.2014-27.04.2014>"),
            new KalenderRequest("2014_Ferien_Pfingsten", "<10.06.2014-22.06.2014>"),
            new KalenderRequest("2014_Ferien_Sommer", "<31.07.2014-14.09.2014>"),
            new KalenderRequest("2014_Ferien_Weihnachten", "<22.12.2014-05.01.2015>"),
            new KalenderRequest("2014_Frühling_Drittel", "<01.03.2014-30.06.2014>"),
            new KalenderRequest("2014_SommerHerbst_Drittel", "<01.07.2014-31.10.2014>"),
            new KalenderRequest("2014_Winter_Drittel", "<01.11.2014-28.02.2015>"),
            new KalenderRequest("2015_Ferien",
                    "ODER{2015_Ferien_Ostern,2015_Ferien_Pfingsten,2015_Ferien_Sommer,2015_Ferien_Herbst,2015_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2015_Ferien_Herbst", "<02.11.2015-08.11.2015>"),
            new KalenderRequest("2015_Ferien_Ostern", "<29.03.2015-12.04.2015>"),
            new KalenderRequest("2015_Ferien_Pfingsten", "<26.05.2015-07.06.2015>"),
            new KalenderRequest("2015_Ferien_Sommer", "<30.07.2015-13.09.2015>"),
            new KalenderRequest("2015_Ferien_Weihnachten", "<23.12.2015-05.01.2016>"),
            new KalenderRequest("2015_Frühling_Drittel", "<01.03.2015-30.06.2015>"),
            new KalenderRequest("2015_SommerHerbst_Drittel", "<01.07.2015-31.10.2015>"),
            new KalenderRequest("2015_Winter_Drittel", "<01.11.2015-29.02.2016>"),
            new KalenderRequest("2016_Ferien",
                    "ODER{2016_Ferien_Ostern,2016_Ferien_Pfingsten,2016_Ferien_Sommer,2016_Ferien_Herbst,2016_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2016_Ferien_Herbst", "<30.10.2016-06.11.2016>"),
            new KalenderRequest("2016_Ferien_Ostern", "<24.03.2016-03.04.2016>"),
            new KalenderRequest("2016_Ferien_Pfingsten", "<17.05.2016-29.05.2016>"),
            new KalenderRequest("2016_Ferien_Sommer", "<28.07.2016-11.09.2016>"),
            new KalenderRequest("2016_Ferien_Weihnachten", "<27.12.2016-08.01.2017>"),
            new KalenderRequest("2016_Frühling_Drittel", "<01.03.2016-30.06.2016>"),
            new KalenderRequest("2016_SommerHerbst_Drittel", "<01.07.2016-31.10.2016>"),
            new KalenderRequest("2016_Winter_Drittel", "<01.11.2016-28.02.2017>"),
            new KalenderRequest("2017_Ferien",
                    "ODER{2017_Ferien_Ostern,2017_Ferien_Pfingsten,2017_Ferien_Sommer,2017_Ferien_Herbst,2017_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2017_Ferien_Herbst", "<28.10.2017-05.11.2017>"),
            new KalenderRequest("2017_Ferien_Ostern", "<08.04.2017-23.04.2017>"),
            new KalenderRequest("2017_Ferien_Pfingsten", "<02.06.2017-18.06.2017>"),
            new KalenderRequest("2017_Ferien_Sommer", "<27.07.2017-10.09.2017>"),
            new KalenderRequest("2017_Ferien_Weihnachten", "<22.12.2017-07.01.2018>"),
            new KalenderRequest("2017_Frühling_Drittel", "<01.03.2017-30.06.2017>"),
            new KalenderRequest("2017_SommerHerbst_Drittel", "<01.07.2017-31.10.2017>"),
            new KalenderRequest("2017_Winter_Drittel", "<01.11.2017-28.02.2018>"),
            new KalenderRequest("2018_Ferien",
                    "ODER{2018_Ferien_Ostern,2018_Ferien_Pfingsten,2018_Ferien_Sommer,2018_Ferien_Herbst,2018_Ferien_Weihnachten}*,*"),
            new KalenderRequest("2018_Ferien_Herbst", "<27.10.2018-04.11.2018>"),
            new KalenderRequest("2018_Ferien_Ostern", "<24.03.2018-08.04.2018>"),
            new KalenderRequest("2018_Ferien_Pfingsten", "<19.05.2018-03.06.2018>"),
            new KalenderRequest("2018_Ferien_Sommer", "<26.07.2018-09.09.2018>"),
            new KalenderRequest("2018_Ferien_Weihnachten", "<20.12.2018-06.01.2019>"),
            new KalenderRequest("2018_Frühling_Drittel", "<01.03.2018-30.06.2018>"),
            new KalenderRequest("2018_SommerHerbst_Drittel", "<01.07.2018-31.10.2018>"),
            new KalenderRequest("2018_Winter_Drittel", "<01.11.2018-28.02.2019>"),
            new KalenderRequest("Abendspitze", "({15:00:00,000-19:00:00,000})"),
            new KalenderRequest("Allerheiligen", "1.11.*,*"),
            new KalenderRequest("Christi_Himmelfahrt", "Ostersonntag+39 Tage"),
            new KalenderRequest("Dienstag", "Dienstag"),
            new KalenderRequest("Dienstag_Ferien", "UND{Ferien,Dienstag}*,*"),
            new KalenderRequest("Dienstag_Frühling_Drittel",
                    "UND{Frühling_Drittel,Dienstag}*,*"),
            new KalenderRequest("Dienstag_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Dienstag}*,*"),
            new KalenderRequest("Dienstag_Winter_Drittel", "UND{Winter_Drittel,Dienstag}*,*"),
            new KalenderRequest("Donnerstag", "Donnerstag"),
            new KalenderRequest("Donnerstag_Ferien", "UND{Ferien,Donnerstag}*,*"),
            new KalenderRequest("Donnerstag_Frühling_Drittel",
                    "UND{Frühling_Drittel,Donnerstag}*,*"),
            new KalenderRequest("Donnerstag_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Donnerstag}*,*"),
            new KalenderRequest("Donnerstag_Winter_Drittel",
                    "UND{Winter_Drittel,Donnerstag}*,*"),
            new KalenderRequest("Ferien",
                    "ODER{2012_Ferien,2013_Ferien,2014_Ferien,2015_Ferien,2016_Ferien,2017_Ferien,2018_Ferien}*,*"),
            new KalenderRequest("Freitag", "Freitag"),
            new KalenderRequest("Freitag_Ferien", "UND{Ferien,Freitag}*,*"),
            new KalenderRequest("Freitag_Frühling_Drittel", "UND{Frühling_Drittel,Freitag}*,*"),
            new KalenderRequest("Freitag_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Freitag}*,*"),
            new KalenderRequest("Freitag_Winter_Drittel", "UND{Winter_Drittel,Freitag}*,*"),
            new KalenderRequest("Fronleichnam", "Ostersonntag+60 Tage"),
            new KalenderRequest("Frühling_Drittel",
                    "ODER{2013_Frühling_Drittel,2014_Frühling_Drittel,2015_Frühling_Drittel,2016_Frühling_Drittel,2017_Frühling_Drittel,2018_Frühling_Drittel}*,*"),
            new KalenderRequest("Gründonnerstag", "Ostersonntag-3 Tage"),
            new KalenderRequest("Hauptverkehrszeit", "ODER{Morgenspitze,Abendspitze}*,*"),
            new KalenderRequest("Heiligabend", "24.12.*,*"),
            new KalenderRequest("Hl_Drei_Könige", "6.1.*,*"),
            new KalenderRequest("Karfreitag", "Ostersonntag-2 Tage"),
            new KalenderRequest("Karsamstag", "Ostersonntag-1 Tag"),
            new KalenderRequest("Mittwoch", "Mittwoch"),
            new KalenderRequest("Mittwoch_Ferien", "UND{Ferien,Mittwoch}*,*"),
            new KalenderRequest("Mittwoch_Frühling_Drittel",
                    "UND{Frühling_Drittel,Mittwoch}*,*"),
            new KalenderRequest("Mittwoch_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Mittwoch}*,*"),
            new KalenderRequest("Mittwoch_Winter_Drittel", "UND{Winter_Drittel,Mittwoch}*,*"),
            new KalenderRequest("Montag", "Montag"),
            new KalenderRequest("Montag_Ferien", "UND{Ferien,Montag}*,*"),
            new KalenderRequest("Montag_Frühling_Drittel", "UND{Frühling_Drittel,Montag}*,*"),
            new KalenderRequest("Montag_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Montag}*,*"),
            new KalenderRequest("Montag_Winter_Drittel", "UND{Winter_Drittel,Montag}*,*"),
            new KalenderRequest("Morgenspitze", "({06:00:00,000-10:00:00,000})"),
            new KalenderRequest("Neujahr", "1.1.*,*"),
            new KalenderRequest("Ostermontag", "Ostersonntag+1 Tag"),
            new KalenderRequest("Ostersonntag", "Ostersonntag"),
            new KalenderRequest("Pfingstmontag", "Ostersonntag+50 Tage"),
            new KalenderRequest("Pfingstsonntag", "Ostersonntag+49 Tage"),
            new KalenderRequest("Samstag", "Samstag"),
            new KalenderRequest("Samstag_Ferien", "UND{Ferien,Samstag}*,*"),
            new KalenderRequest("Samstag_Frühling_Drittel", "UND{Frühling_Drittel,Samstag}*,*"),
            new KalenderRequest("Samstag_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Samstag}*,*"),
            new KalenderRequest("Samstag_Winter_Drittel", "UND{Winter_Drittel,Samstag}*,*"),
            new KalenderRequest("Silvester", "31.12.*,*"),
            new KalenderRequest("SommerHerbst_Drittel",
                    "ODER{2013_SommerHerbst_Drittel,2014_SommerHerbst_Drittel,2015_SommerHerbst_Drittel,2016_SommerHerbst_Drittel,2017_SommerHerbst_Drittel,2018_SommerHerbst_Drittel}*,*"),
            new KalenderRequest("Sonntag", "Sonntag"),
            new KalenderRequest("Sonntag_Ferien", "UND{Ferien,Sonntag}*,*"),
            new KalenderRequest("Sonntag_Frühling_Drittel", "UND{Frühling_Drittel,Sonntag}*,*"),
            new KalenderRequest("Sonntag_SommerHerbst_Drittel",
                    "UND{SommerHerbst_Drittel,Sonntag}*,*"),
            new KalenderRequest("Sonntag_Winter_Drittel", "UND{Winter_Drittel,Sonntag}*,*"),
            new KalenderRequest("Tag_der_Arbeit", "1.5.*,*"),
            new KalenderRequest("Tag_der_Deutschen_Einheit", "3.10.1990,*"),
            new KalenderRequest("Winter_Drittel",
                    "ODER{2012_Winter_Drittel,2013_Winter_Drittel,2014_Winter_Drittel,2015_Winter_Drittel,2016_Winter_Drittel,2017_Winter_Drittel,2018_Winter_Drittel}*,*"),
    };
    private ExecutorService executor;
    private MutableSet kalenderMenge;
    private DynamicObjectType type;
    private DataDescription desc;
    private DynamischeObjekte dynObj;
    private ClientDavInterface connection;

    public class EintragsSender implements Runnable, ClientSenderInterface {

        private DynamicObject eintrag;
        private Data data;
        private Object lock = new Object();

        public EintragsSender(DynamicObject eintrag, Data data) {
            this.eintrag = eintrag;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                connection.subscribeSender(this, eintrag, desc, SenderRole.sender());
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (OneSubscriptionPerSendData e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // TODO Auto-generated method stub

        }

        @Override
        public void dataRequest(SystemObject object, DataDescription dataDescription, byte state) {
            if (state == ClientSenderInterface.START_SENDING) {
                try {
                    connection.sendData(new ResultData(eintrag, desc, connection.getTime(), data));
                    connection.unsubscribeSender(this, eintrag, desc);
                } catch (DataNotSubscribedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SendSubscriptionNotConfirmed e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                synchronized (lock) {
                    lock.notifyAll();
                }
            }

        }

        @Override
        public boolean isRequestSupported(SystemObject object, DataDescription dataDescription) {
            return true;
        }

    }

    @Override
    public void parseArguments(ArgumentList argumentList) throws Exception {
        // keine Parameter

    }

    @Override
    public void initialize(ClientDavInterface initialConnection) throws Exception {

        executor = Executors.newCachedThreadPool();
        this.connection = initialConnection;

        ConfigurationAuthority kalender = initialConnection.getLocalConfigurationAuthority();
        kalenderMenge = kalender.getMutableSet("SystemKalenderEinträge");

        DataModel dataModel = initialConnection.getDataModel();

        type = (DynamicObjectType) dataModel.getType("typ.systemKalenderEintrag");

        AttributeGroup atg = dataModel.getAttributeGroup("atg.systemKalenderEintrag");
        Aspect asp = dataModel.getAspect("asp.parameterVorgabe");
        desc = new DataDescription(atg, asp);

        dynObj = DynamischeObjekte.getInstanz(initialConnection);

        
        for (KalenderRequest request : requests) {
            erzeugeKalenderEintrag(request);
        }

        System.err.println("Komplett");
        System.exit(0);
    }

    private void erzeugeKalenderEintrag(KalenderRequest request) throws DynObjektException, InterruptedException, ExecutionException {
        
        DynamicObject kalenderEintrag = dynObj.erzeugeObjektInMenge(type, request.name, request.pid, kalenderMenge);

        Data data = connection.createData(desc.getAttributeGroup());
        data.getTextValue("Definition").setText(request.definition);

        Future<?> submit = executor.submit(new EintragsSender(kalenderEintrag, data));
        submit.get();
        System.err.println("Angelegt: " + request.name);

    }

    public static void main(String[] args) {
        StandardApplicationRunner.run(new KalenderTestDatenGenerator(), args);
    }
}

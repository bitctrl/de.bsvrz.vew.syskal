package de.bsvrz.vew.syskal.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import de.bsvrz.vew.syskal.Intervall;
import de.bsvrz.vew.syskal.KalenderEintrag;
import de.bsvrz.vew.syskal.TestIntervall;
import de.bsvrz.vew.syskal.TestKalenderEintragProvider;
import de.bsvrz.vew.syskal.TestWechsel;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public class KomplexUndTest {
    
//    @Rule
//    public Timeout globalTimeout = Timeout.seconds(5);

    private static TestKalenderEintragProvider eintragsProvider;

    private static LocalDateTime startTime;

    private static LocalDateTime endTime;

    @BeforeClass
    public static void init() {
        eintragsProvider = new TestKalenderEintragProvider();
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Frühling", "<01.03.2018 - 31.05.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Sommer", "<01.06.2018 - 31.08.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Ostern", "<24.03.2018 - 08.04.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien_Pfingsten", "<19.05.2018 - 03.06.2018>");
        eintragsProvider.parseAndAdd(eintragsProvider, "2018_Ferien",
                "ODER{2018_Ferien_Ostern, 2018_Ferien_Pfingsten}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Frühling", "ODER{2018_Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sommer", "ODER{2018_Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Ferien", "ODER{2018_Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Frühling", "UND{Montag, Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Frühling", "UND{Dienstag, Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Frühling", "UND{Mittwoch, Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Frühling", "UND{Donnersag, Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Frühling", "UND{Freitag, Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Frühling", "UND{Samstag, Frühling}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Frühling", "UND{Sonntag, Frühling}");

        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Sommer", "UND{Montag, Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Sommer", "UND{Dienstag, Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Sommer", "UND{Mittwoch, Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Sommer", "UND{Donnersag, Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Sommer", "UND{Freitag, Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Sommer", "UND{Samstag, Sommer}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Sommer", "UND{Sonntag, Sommer}");

        eintragsProvider.parseAndAdd(eintragsProvider, "Montag_Ferien", "UND{Montag, Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Dienstag_Ferien", "UND{Dienstag, Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Mittwoch_Ferien", "UND{Mittwoch, Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Donnerstag_Ferien", "UND{Donnersag, Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Freitag_Ferien", "UND{Freitag, Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Samstag_Ferien", "UND{Samstag, Ferien}");
        eintragsProvider.parseAndAdd(eintragsProvider, "Sonntag_Ferien", "UND{Sonntag, Ferien}");
    }

    @Test
    public void testeAbfrage_07_02_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 2, 7), LocalTime.MIDNIGHT);

        String[] namen = { "Mittwoch" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }

    @Test
    public void testeAbfrage_07_03_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 3, 7), LocalTime.MIDNIGHT);

        String[] namen = { "Mittwoch", "Mittwoch_Frühling", "Frühling", "2018_Frühling" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }

    @Test
    public void testeAbfrage_17_03_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 3, 17), LocalTime.MIDNIGHT);

        String[] namen = { "Samstag", "Samstag_Frühling", "Frühling", "2018_Frühling" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }
    
    @Test
    public void testeAbfrage_27_03_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 3, 27), LocalTime.MIDNIGHT);

        String[] namen = { "Dienstag", "Dienstag_Frühling", "Dienstag_Ferien", "Frühling", "2018_Frühling", "Ferien", "2018_Ferien", "2018_Ferien_Ostern" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }

    @Test
    public void testeAbfrage_07_04_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 4, 7), LocalTime.MIDNIGHT);

        String[] namen = { "Samstag", "Samstag_Frühling", "Samstag_Ferien", "Frühling", "2018_Frühling", "Ferien", "2018_Ferien", "2018_Ferien_Ostern" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }
    
    @Test
    public void testeAbfrage_17_04_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 4, 17), LocalTime.MIDNIGHT);

        String[] namen = { "Dienstag", "Dienstag_Frühling", "Frühling", "2018_Frühling" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }
    
    @Test
    public void testeAbfrage_27_05_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 5, 27), LocalTime.MIDNIGHT);

        String[] namen = { "Sonntag", "Sonntag_Frühling", "Sonntag_Ferien", "Frühling", "2018_Frühling", "Ferien", "2018_Ferien", "2018_Ferien_Pfingsten" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }
    @Test
    public void testeAbfrage_01_06_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 6, 1), LocalTime.MIDNIGHT);

        String[] namen = { "Freitag", "Freitag_Sommer", "Freitag_Ferien", "Sommer", "2018_Sommer", "Ferien", "2018_Ferien", "2018_Ferien_Pfingsten" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }

    @Test
    public void testeAbfrage_10_06_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 6, 10), LocalTime.MIDNIGHT);

        String[] namen = { "Sonntag", "Sonntag_Sommer", "Sommer", "2018_Sommer" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }
    
    @Test
    public void testeAbfrage_14_07_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 7, 14), LocalTime.MIDNIGHT);

        String[] namen = { "Samstag", "Samstag_Sommer", "Sommer", "2018_Sommer" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }

    @Test
    public void testeAbfrage_17_10_2018() {

        LocalDateTime zeitPunkt = LocalDateTime.of(LocalDate.of(2018, 10, 17), LocalTime.MIDNIGHT);

        String[] namen = { "Mittwoch" };
        List<String> gueltigeEintraege = Arrays.asList(namen);

        for (KalenderEintrag eintrag : eintragsProvider.getKalenderEintraege()) {
            if (gueltigeEintraege.contains(eintrag.getName())) {
                assertTrue(eintrag.getName() + " ist gültig", eintrag.isGueltig(zeitPunkt));
            } else {
                assertFalse(eintrag.getName() + " ist ungültig", eintrag.isGueltig(zeitPunkt));
            }
        }
    }
}

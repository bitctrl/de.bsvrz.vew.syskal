package de.bsvrz.vew.syskal.internal;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.vew.syskal.Gueltigkeit;
import de.bsvrz.vew.syskal.ZustandsWechsel;

public abstract class KalenderEintrag {

	/** das Pattern zum Ermitteln eines Datumsbereiches im Definitionsstring. */
	private static final Pattern DATUMSBEREICH_PATTERN = Pattern.compile("<.*>");

	/**
	 * das Pattern zum Ermitteln eines Zeitgrenzenbereiches im Definitionsstring.
	 */
	private static final Pattern ZEITBEREICHSLISTE_PATTERN = Pattern.compile("\\(.*\\)");

	/**
	 * das Pattern zum Ermitteln eines Zeitbereiches oder einer Verknüpfungsliste.
	 */
	protected static final Pattern ZEITBEREICH_PATTERN = Pattern.compile("\\{.[^\\{]*\\}");

	private String definition;
	private String name;

	protected KalenderEintrag(String name, String definition) {
		this.name = name;
		this.definition = definition;
	}
	
	public abstract Gueltigkeit isZeitlichGueltig(LocalDateTime zeitpunkt);
	
	public final List<ZustandsWechsel> getZustandsWechselImBereich(LocalDateTime start, LocalDateTime ende) {

		List<ZustandsWechsel> result = new ArrayList<>();

		Gueltigkeit gueltigkeit = isZeitlichGueltig(start);
		result.add(ZustandsWechsel.of(start, gueltigkeit.isZeitlichGueltig()));

		LocalDateTime aktuellerZeitPunkt = start;
		do {
			ZustandsWechsel wechsel = gueltigkeit.getNaechsterWechsel();
			aktuellerZeitPunkt = wechsel.getZeitPunkt();
			
			if(!aktuellerZeitPunkt.isAfter(ende)) {
				result.add(wechsel);
				gueltigkeit = isZeitlichGueltig(wechsel.getZeitPunkt());
			}
			
		} while(!aktuellerZeitPunkt.isAfter(ende));
		
		return result;
	}

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
	public static KalenderEintrag parse(KalenderEintragProvider provider, final String name,
			final String definition) {

		KalenderEintrag result = null;

		result = VorDefinierterEintrag.getEintrag(name);
		if( result != null) {
			return result;
		}
		
		String rest = definition;

		final String[] parts = definition.split(":=");

		if (parts.length < 2) {
			rest = parts[0].trim();
		} else {
			final String defName = parts[0].trim();
			if (!defName.equals(name)) {
				Debug.getLogger().warning("Für den Systemkalendereintrag " + name + " ist der abweichende Name: \""
						+ defName + "\" definiert!");
			}
			rest = parts[1].trim();
		}

		final List<ZeitGrenze> parseZeitBereiche = new ArrayList<>();

		Matcher mat = KalenderEintrag.ZEITBEREICHSLISTE_PATTERN.matcher(rest);

		boolean zeitBereichsfehler = false;

		while (mat.find()) {
			final String bereich = mat.group();
			rest = rest.replace(bereich, "");
			final String zeitBereich = bereich.substring(1, bereich.length() - 1);
			final Matcher zeitMat = KalenderEintrag.ZEITBEREICH_PATTERN.matcher(zeitBereich);
			while (zeitMat.find()) {
				String zb = zeitMat.group();
				zb = zb.substring(1, zb.length() - 1);
				try {
					parseZeitBereiche.add(new ZeitGrenze(zb));
				} catch (final ParseException e) {
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
			mat = KalenderEintrag.DATUMSBEREICH_PATTERN.matcher(rest);
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

			result.zeitGrenzen.addAll(parseZeitBereiche);
		}

		result.definition = definition;

		if (zeitBereichsfehler) {
			result.setFehler(true);
		}

		return result;
	}

	/** die Zeitgrenzen, die den Kalendereintrag zeitlich einschränken können. */
	private final List<ZeitGrenze> zeitGrenzen = new ArrayList<>();

	/** der Definitionseintrag konnte nicht korrekt eingelesen werden. */
	private boolean fehler;

	/**
	 * fügt eine Zeitgrenze hinzu.
	 * 
	 * @param grenze
	 *            die neue Zeitgrenze
	 */
	public void addZeitGrenze(final ZeitGrenze grenze) {
		zeitGrenzen.add(grenze);
	}

	/**
	 * liefert die Art des Dateneintrags.
	 * 
	 * @return dte Art
	 */
	public abstract EintragsArt getEintragsArt();

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
	 * liefert die Liste der für den Eintrag definierten Zeitgrenzen.
	 * 
	 * TODO Klärung, welche Eintragsarten die Zuordnung von Zeitgrenzen erlauben!
	 * 
	 * @return die Liste der definierten Grenzen
	 */
	public List<ZeitGrenze> getZeitGrenzen() {
		return zeitGrenzen;
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

	public String getName() {
		return name;
	}
}

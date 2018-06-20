Versionsverlauf
===============

## [Noch nicht veröffentlicht]

- bei ungünstigen Anfragen für Zustandswechsel in logischen Verknüpfungen
  konnte es zu einer Endlosschleife kommen
- bei Anfrage zu Kalendereinträgen mit Verweisen wurde die Gültigkeit der referenzierten
  Einträge nicht in jedem Fall korrekt geprüft

## [Version 2.0.2]

- die ermittelten Gültigkeiten für Systemkalendereinträge werden gecached, um die
  Ermittlung von Zustandsübergängen bei vielen Anfragen zu beschleunigen.

## [Version 2.0.1]

- Systemkalendereinträge mit Zeitverschiebungen in logischen Verknüpfungen
  konnten zu Endlosschleifen oder falschen Ergebnissen führen
- beim Einfügen neuer Systemkalendereinträge wird die Gültigkeit von 
  referenzierenden Einträgen neu berechnet
- beim Neuberechnen der Systemkalenderreferenzen konnte es zu Problemen kommen, 
  wenn im Definitionsstring der obsolete Präfix "{name}:=" enthalten war

## [Version 2.0.0]

- Neuimplementierung unter Verwendung des bestehenden Datenverteiler-Modells zut Wahrung der Kompatibilität 
  mit vorhandenen Konfigurationen
- Erstellung einer neuen API für die Auswertung von Systemkalendereinträgen durch Anwender der Bibliothek

## [Version 1.2.8]

- NERZ: Umstellung auf Gradle, Build durch FTB und Bereitstellung auf NERZ-Repositories

## Version 1.2.7

- Übernahme vor Umstellung auf Gradle


[Noch nicht veröffentlicht]: https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.vew.syskal/compare/v2.0.2...HEAD
[Version 2.0.2]: https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.vew.syskal/compare/v2.0.1...v2.0.2
[Version 2.0.1]: https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.vew.syskal/compare/v2.0.0...v2.0.1
[Version 2.0.0]: https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.vew.syskal/compare/v1.2.8...v2.0.0
[Version 1.2.8]: https://gitlab.nerz-ev.de/ERZ/SWE_de.bsvrz.vew.syskal/compare/v1.2.7...v1.2.8

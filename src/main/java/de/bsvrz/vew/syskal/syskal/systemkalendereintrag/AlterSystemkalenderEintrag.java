package de.bsvrz.vew.syskal.syskal.systemkalendereintrag;

import java.util.SortedMap;

import de.bsvrz.vew.syskal.syskal.erinnerungsfunktion.ErinnerungsFunktion;

public interface AlterSystemkalenderEintrag {

	SortedMap<Long, Boolean> getListeZustandsWechsel();

	boolean isGueltig(Long timeNow);

	String getPid();

	String getName();

	void setErinnerungsFunktion(Boolean f);

	boolean isGueltigVonBis(long von, long bis);

	SortedMap<Long, Long> berecheneIntervallVonBis(Long von, Long bis);

	SortedMap<Long, Long> berechneIntervall(Long von, Long bis, int jahr);

	boolean pruefeEintrag();

	void setPid(String pid);

	void setDefinition(String definition);

	void setListeZustandsWechsel(SortedMap<Long, Boolean> liste);

	void setObjektListeZustandsWechsel(ListeZustandsWechsel liste);

	SortedMap<Long, Boolean> berecheneZustandsWechselVonBis(Long von, Long bis);

	ErinnerungsFunktion getErinnerungsFunktion();

	void setName(String name);

	String getDefinition();

	ListeZustandsWechsel getObjektListeZustandsWechsel();

	SortedMap<Long, Boolean> berechneZustandsWechselZustand(Long von, Long bis, int jahr);

	SortedMap<Long, Boolean> berechneZustandsWechsel(Long von, Long bis, int jahr);

	SortedMap<Long, Boolean> berechneZustandsWechsel(int jahr);

}

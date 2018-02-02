package de.bsvrz.vew.syskal;

import java.time.LocalDateTime;

public interface ZustandsWechsel {

	LocalDateTime getZeitPunkt();
	boolean isWirdGueltig();
}
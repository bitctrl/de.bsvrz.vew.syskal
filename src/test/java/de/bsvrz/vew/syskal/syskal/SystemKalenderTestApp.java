package de.bsvrz.vew.syskal.syskal;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.vew.syskal.SystemKalender;
import de.bsvrz.vew.syskal.SystemKalenderException;

public class SystemKalenderTestApp implements StandardApplication {

	@Override
	public void parseArguments(ArgumentList argumentList) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(ClientDavInterface connection) throws Exception {
		SystemKalender kalender = new SystemKalender(connection, connection.getLocalConfigurationAuthority());
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			listEntries(kalender);
		}, 1, 5, TimeUnit.SECONDS);
	}

	private void listEntries(SystemKalender kalender) {
		try {
			kalender.getEintraege().stream().forEach(e -> System.out.println(e));
		} catch (SystemKalenderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		StandardApplicationRunner.run(new SystemKalenderTestApp(), args);
	}
}

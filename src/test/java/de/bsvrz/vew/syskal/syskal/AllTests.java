package de.bsvrz.vew.syskal.syskal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.bsvrz.vew.syskal.syskal.systemkalendereintrag.ParserTest;
import de.bsvrz.vew.syskal.syskal.systemkalendereintrag.SystemkalenderEintragTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ParserTest.class, SystemkalenderEintragTest.class, SystemTest.class })
/**
 * Kommentar
 * 
 * @version $Revision: 1.1 $ / $Date: 2009/09/24 12:49:16 $ / ($Author: Pittner $)
 * 
 * @author Dambach-Werke GmbH
 * @author Timo Pittner
 * 
 */
public class AllTests
{
}

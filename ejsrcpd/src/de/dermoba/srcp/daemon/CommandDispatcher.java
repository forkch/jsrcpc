/*
 * $RCSfile: CommandDispatcher.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.3  2005/10/16 14:27:40  harders
 Übergang zu sourceforge

 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.3  2002/01/28 20:58:43  osc3
 added traces

 Revision 1.2  2002/01/24 21:11:17  osc3
 further improvements for handling fragmented commands
 data is read byte by byte now

 Revision 1.1.1.1  2002/01/08 18:21:42  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.daemon;

/**
 *
 * @author  osc, Kurt Harders
 * @version $Revision: 1.1 $
 */

import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPNotSupportedException;
import de.dermoba.srcp.common.exception.SRCPUnknownCommandException;
import de.dermoba.srcp.common.exception.SRCPWrongValueException;

/** object for assigning commands to the busses */
public class CommandDispatcher {
    public static final String CMD_SET = "SET";
    public static final String CMD_CHECK = "CHECK";
    public static final String CMD_WAIT = "WAIT";
    public static final String CMD_INIT = "INIT";
    public static final String CMD_TERM = "TERM";
    public static final String CMD_RESET = "RESET";
    public static final String CMD_VERIFY = "VERIFY";
    public static final String CMD_INFO = "INFO";
    public static final String CMD_GET = "GET";

    public static final String CMD_GO = "GO";

	public CommandDispatcher () {
	}

	public void doCommand (TokenizedLine tokens,Session session) throws SRCPException {
		String verb = null;
		int busNumber = 0;
		try {
			verb = tokens.nextStringToken();
			busNumber = tokens.nextIntToken();
			if ( verb.equals(CMD_TERM) ) {
				SRCPDaemon.getBus(busNumber).term(tokens,session);
				return;
			}
			if ( verb.equals(CMD_INIT) ) {
				SRCPDaemon.getBus(busNumber).init(tokens,session);
				return;
			}
			if ( verb.equals(CMD_GET) ) {
				SRCPDaemon.getBus(busNumber).get(tokens,session);
				return;
			}
			if ( verb.equals(CMD_RESET) ) {
				SRCPDaemon.getBus(busNumber).reset(tokens,session);
				return;
			}
			if ( verb.compareTo(CMD_SET)==0 ) {
				SRCPDaemon.getBus(busNumber).set(tokens,session);
				return;
			}
			if ( verb.equals(CMD_CHECK) ) {
				SRCPDaemon.getBus(busNumber).check(tokens,session);
				return;
			}
			if ( verb.equals(CMD_WAIT) ) {
				SRCPDaemon.getBus(busNumber).wait(tokens,session);
				return;
			}
			if ( verb.equals(CMD_VERIFY) ) {
				SRCPDaemon.getBus(busNumber).verify(tokens,session);
				return;
			}
		}
		catch ( ArrayIndexOutOfBoundsException ae ) {
			throw new SRCPNotSupportedException();
		}
		catch ( NumberFormatException ne ) {
			throw new SRCPWrongValueException();
		}
		catch ( SRCPException se ) {
		 	throw se;
		}
		throw new SRCPUnknownCommandException();
	}
}

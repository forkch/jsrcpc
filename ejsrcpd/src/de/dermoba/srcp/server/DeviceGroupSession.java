/*
 * $RCSfile: DeviceGroupSession.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.4  2005/10/16 14:27:40  harders
 Übergang zu sourceforge

 Revision 1.3  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.2  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.3  2002/01/23 21:00:29  osc3
 improved command input handling to deal with fragmented data

 Revision 1.2  2002/01/17 21:14:18  osc3
 adapted for SRCP 0.8.1

 Revision 1.1.1.1  2002/01/08 18:21:46  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.server;

/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
 */

import de.dermoba.srcp.common.Globals;
import de.dermoba.srcp.common.SRCPMessage;
import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPListToLongException;
import de.dermoba.srcp.common.exception.SRCPListToShortException;
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.SRCPDaemon;
import de.dermoba.srcp.daemon.Session;

class DeviceGroupSession extends DeviceGroupBus0
{

	DeviceGroupSession (Bus b) {
		super(b);
		identifier = DEV_SESSION;
	}

	public void term(TokenizedLine tokens,Session session) throws SRCPException {
		handleTermAccess();
		int sessionID = tokens.nextIntToken();
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		SRCPDaemon.getSessionByID(sessionID).stopThread();
		session.send(new SRCPMessage());
	}
	
	/** return the session info;
	  * SRCP syntax: GET 0 SESSION <sessionid> */
	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		int intID;
		try {
			intID = tokens.nextIntToken();
		}
		catch (Exception e) {
			throw new SRCPListToShortException();
		}
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage(Globals.INFO_MIN_CODE, SRCPDaemon.getSessionByID(intID).getInfo()));
	}
}

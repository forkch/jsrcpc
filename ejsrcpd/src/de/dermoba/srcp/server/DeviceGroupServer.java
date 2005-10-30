/*
 * $RCSfile: DeviceGroupServer.java,v $
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

 Revision 1.2  2002/01/23 21:00:29  osc3
 improved command input handling to deal with fragmented data

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
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.DefaultDeviceGroup;
import de.dermoba.srcp.daemon.SRCPDaemon;
import de.dermoba.srcp.daemon.Session;

class DeviceGroupServer extends DefaultDeviceGroup
{

	/** build a "SERVER" device group for BUS 0 */
	DeviceGroupServer (Bus pBus) {
		super();
		identifier = DEV_SERVER;
		bus=pBus;
	}

	/** return server state */
	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage(Globals.INFO_MIN_CODE,identifier + " " + SRCPDaemon.getState()));
	}

	/** terminate the whole SRCP server including all sessions */
	public void term(TokenizedLine tokens,Session session) throws SRCPException {
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage());
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(102, "SERVER"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		SRCPDaemon.stopServer();
	}

	/** perform a reset on the whole SRCP server */
	public void reset(TokenizedLine tokens,Session session) throws SRCPException {
		handleWriteAccess(session);
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		SRCPDaemon.resetServer();
		session.send(new SRCPMessage());
	}
}

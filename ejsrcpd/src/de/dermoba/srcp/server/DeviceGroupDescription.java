/*
 * $RCSfile: DeviceGroupDescription.java,v $
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

 Revision 1.4  2002/01/30 20:28:25  osc3
 fix for handling groups wiht required devices

 Revision 1.3  2002/01/23 21:00:29  osc3
 improved command input handling to deal with fragmented data

 Revision 1.2  2002/01/17 21:14:18  osc3
 adapted for SRCP 0.8.1

 Revision 1.1.1.1  2002/01/08 18:21:44  osc3
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
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.Session;
class DeviceGroupDescription extends DeviceGroupBus0
{
	DeviceGroupDescription (Bus b) {
		super(b);
		identifier = DEV_DESCRIPTION;
		bus = b;
	}

	/**
	 * GET <bus> DESCRIPTION
	   GET <bus> DESCRIPTION <devicegroup> [<address>]
	 */
	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		if (tokens.hasMoreElements()) {
			String devg = tokens.nextStringToken();
			bus.getDeviceGroupByName(devg).description(tokens, session);
		} else {
			send(session);
		}
	}

	private void send(Session session) {
		StringBuffer strOutBuffer = new StringBuffer();
		strOutBuffer.append(bus.getId());
		strOutBuffer.append(" ");
		strOutBuffer.append(DEV_DESCRIPTION);
		strOutBuffer.append(" ");
		for (int intIndex=0;intIndex<bus.getDeviceGroupCount();intIndex++) {
			// do not list ourself
			if (!bus.getDeviceGroup(intIndex).getIdentifier().equals(DEV_DESCRIPTION)) {
				strOutBuffer.append(bus.getDeviceGroup(intIndex).getIdentifier());
				strOutBuffer.append(" ");
			}
		}
		session.send(new SRCPMessage(Globals.INFO_MIN_CODE,strOutBuffer.toString()));
	}
}

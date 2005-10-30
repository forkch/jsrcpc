/*
 * $RCSfile: ServerBus.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.1.1.1  2002/01/08 18:21:42  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.server;

/**
 *
 * @author  osc
 * @version $Revision: 1.1 $
 */

import de.dermoba.srcp.daemon.DefaultBus;

/** SRCP bus 0 is the internal server management bus */
public class ServerBus extends DefaultBus {
	
	public ServerBus (int pid) {
		super();
		id = pid;
		deviceGroups.add(new DeviceGroupServer(this));
		deviceGroups.add(new DeviceGroupSession(this));
		deviceGroups.add(new DeviceGroupTime(this));
		deviceGroups.add(new DeviceGroupDescription(this));
	}
}

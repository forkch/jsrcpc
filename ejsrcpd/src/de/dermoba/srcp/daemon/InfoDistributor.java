/*
 * $RCSfile: InfoDistributor.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.7  2002/01/28 20:59:42  osc3
 added flexible line end style

 Revision 1.6  2002/01/24 21:11:17  osc3
 further improvements for handling fragmented commands
 data is read byte by byte now

 Revision 1.5  2002/01/23 20:57:15  osc3
 added an excpetion output message

 Revision 1.4  2002/01/21 18:15:59  osc3
 introduced constants for info numbers

 Revision 1.3  2002/01/16 18:03:47  osc3
 replaced Instruction with Operand
 added bus as a parameter

 Revision 1.2  2002/01/13 21:21:17  osc3
 re-organised info push for the different device groups

 Revision 1.1.1.1  2002/01/08 18:21:47  osc3
 import of jsrcpd
 */

package de.dermoba.srcp.daemon;

import java.util.ArrayList;
import java.util.Iterator;

import de.dermoba.srcp.common.SRCPMessage;

/** 
 * The InfoDistributor gets the data via the InfoReceiver interface from the
 * implementations of the HardwareImplementation interface. It distributes
 * this information to all INFO-sessions registered as InfoListener.
 * 
 * @author  osc, harders
 * @version $Revision: 1.1 $
 */
public class InfoDistributor {

	protected ArrayList<InfoListener> infoListeners=null;

	public InfoDistributor() {
		infoListeners = new ArrayList<InfoListener>();
	}
	
	public void addInfoListener(InfoListener il) {
		infoListeners.add(il);
	}

	/** pass a well-formatted info to the info listeners */
	public void send(SRCPMessage message) {
		synchronized(infoListeners) {
			for (Iterator<InfoListener> i = infoListeners.iterator(); i.hasNext();) {
				i.next().infoDataArrived(message);
			}
		}
	}
}

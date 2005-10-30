/*
 * $RCSfile: DefaultBus.java,v $
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

 Revision 1.2  2002/01/09 19:15:37  osc3
 group not on the bus throws ExcUnsupportedDeviceGroup now

 Revision 1.1.1.1  2002/01/08 18:21:41  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.daemon;

/**
 *
 * @author  osc, Kurt Harders
 * @version $Revision: 1.1 $
 */

import java.util.ArrayList;

import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPUnsupportedDeviceGroupException;

public class DefaultBus implements Bus
{
 	protected ArrayList<DeviceGroup> deviceGroups = null;
	protected int id;

	public DefaultBus() {
		deviceGroups = new ArrayList<DeviceGroup>();
	}
	
	public void setID(int pid) {
		id = pid;
	}
	
	public DeviceGroup getDeviceGroupByName (String ident) throws SRCPException {
		int i;
		DeviceGroup devg;
		for(i=0;i<deviceGroups.size();i++) {
			devg = deviceGroups.get(i);
			if ( devg.getIdentifier().equals(ident)) {
				return devg;
			}
		}
		throw new SRCPUnsupportedDeviceGroupException();
	}

	public void set(TokenizedLine tokens, Session session) throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).set(tokens,session,false);
	}

	public void check(TokenizedLine tokens, Session session) throws SRCPException{
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).set(tokens,session,true);
	}

	public void init(TokenizedLine tokens, Session session) throws SRCPException{
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).init(tokens,session);
	}

	public void term(TokenizedLine tokens, Session session )throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).term(tokens,session);
	}

	public void get(TokenizedLine tokens, Session session) throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).get(tokens,session);
	}

	public void wait(TokenizedLine tokens, Session session) throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).wait(tokens,session);
	}

	public void reset(TokenizedLine tokens, Session session) throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).reset(tokens,session);
	}

	public void verify(TokenizedLine tokens, Session session) throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).verify(tokens,session);
	}
	
	public void description(TokenizedLine tokens, Session session) throws SRCPException {
		String groupName = tokens.nextStringToken();
		getDeviceGroupByName(groupName).description(tokens,session);
	}

	public int getDeviceGroupCount() {
		return deviceGroups.size();
	}

	public DeviceGroup getDeviceGroup(int index) {
		return deviceGroups.get(index);
	}

	public int getId() {
		return id;
	}
}

/*
 * $RCSfile: DefaultDeviceGroup.java,v $
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

 Revision 1.1.1.1  2002/01/08 18:21:43  osc3
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
import de.dermoba.srcp.common.exception.SRCPTemporarlyProhibitedException;
import de.dermoba.srcp.common.exception.SRCPUnsupportedOperationException;

public class DefaultDeviceGroup implements DeviceGroup
{
	protected Bus bus = null;
	protected String identifier;
	protected Session initializer = null;
	protected boolean readOnly = true;

	public DefaultDeviceGroup () {
	}
	
	public void set(TokenizedLine tokens,Session session,boolean blnCheck) throws SRCPException {
		handleWriteAccess(session);
		defaultHandler();
	}

	public void init(TokenizedLine tokens,Session session)throws SRCPException {
		handleInitAccess(session);
		defaultHandler();
	}

	public void term(TokenizedLine tokens,Session session) throws SRCPException {
		handleTermAccess();
		defaultHandler();
	}

	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		handleReadAccess();
		defaultHandler();
	}

	public void wait(TokenizedLine tokens,Session session)throws SRCPException {
		handleReadAccess();
		defaultHandler();
	}

	public void reset(TokenizedLine tokens,Session session) throws SRCPException {
		handleWriteAccess(session);
		defaultHandler();
	}

	public void verify(TokenizedLine tokens,Session session)throws SRCPException {
		handleReadAccess();
		throw new SRCPUnsupportedOperationException();
	}

	public void description(TokenizedLine tokens,Session session) throws SRCPException {
		defaultHandler();
	}

	public Bus getBus() {
		return bus;
	}
	
	public String getIdentifier () {
		return identifier;
	}	
	
	public void handleInitAccess(Session session) throws SRCPException {
		if (initializer != null && session != initializer && readOnly) {
			throw new SRCPTemporarlyProhibitedException();
		}
		initializer = session;
		readOnly = true;
	}
	
	public void handleTermAccess() {
		initializer = null;
		readOnly = true;
	}
	
	public void handleWriteAccess(Session session) throws SRCPException {
		if (session != initializer && readOnly) {
			throw new SRCPTemporarlyProhibitedException();
		}
	}
	
	public void handleReadAccess() throws SRCPException {
		if (initializer == null) {
			throw new SRCPTemporarlyProhibitedException();
		}
		readOnly = false;
	}
	
	public boolean isOptional() {
		return false;
	}

	protected void defaultHandler() throws SRCPException {
		if (isOptional()) {
			throw new SRCPNotSupportedException ();
		}
		else {
			throw new SRCPUnsupportedOperationException();
		}
	}
}

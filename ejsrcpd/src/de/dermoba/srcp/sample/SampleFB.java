/*
 * $RCSfile: SampleFB.java,v $
 *
 * History
 $Log: not supported by cvs2svn $
 Revision 1.2  2005/08/08 08:02:36  harders
 Stand nach Urlaub

 Revision 1.1  2005/07/09 13:11:58  harders
 balkon050709

 Revision 1.1  2005/06/30 14:41:31  harders
 Aufgeräumte erste Version

 Revision 1.5  2002/01/24 21:11:17  osc3
 further improvements for handling fragmented commands
 data is read byte by byte now

 Revision 1.4  2002/01/23 21:00:29  osc3
 improved command input handling to deal with fragmented data

 Revision 1.3  2002/01/17 21:14:18  osc3
 adapted for SRCP 0.8.1

 Revision 1.2  2002/01/16 18:03:47  osc3
 replaced Instruction with Operand
 added bus as a parameter

 Revision 1.1.1.1  2002/01/08 18:21:46  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.sample;

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
import de.dermoba.srcp.common.exception.SRCPUnsufficientDataException;
import de.dermoba.srcp.common.exception.SRCPWrongValueException;
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.SRCPDaemon;
import de.dermoba.srcp.daemon.Session;

class SampleFB extends SampleDeviceGroup
{
	SampleFB (Bus pbus) {
		super(pbus);
		identifier = DEV_FB;
	}
	
	/** SRCP syntax: INIT <bus> FB [<parameter>.. ] */
	public void init(TokenizedLine tokens,Session session) throws SRCPException {
		handleInitAccess(session);
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage());
		sendInfo(1);
	}

	/** SRCP syntax: GET <bus> FB <addr> */
	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		handleReadAccess();
		int addr= -1;
		int value = 0;
		if (tokens.hasMoreElements()) {
			addr = tokens.nextIntToken();
		} else {
			throw new SRCPListToShortException();
		}
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		sendReply(session, addr, value);
	}
	
	/** SRCP syntax: TERM <bus> FB */
	public void term(TokenizedLine tokens,Session session) throws SRCPException {
		handleTermAccess();
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage());
		sendInfo(2);
	}

	/** SRCP syntax: WAIT <bus> FB <adr> <value> <timeout> */
	public void wait(TokenizedLine tokens, Session session) throws SRCPException {
		handleReadAccess();
		int addr= -1;
		int value = 0;
		int timeout = 0;
		try {
			addr = tokens.nextIntToken();
			value = tokens.nextIntToken();
			timeout = tokens.nextIntToken();
		} catch (SRCPUnsufficientDataException e) {
			throw new SRCPListToShortException();
		} catch (NumberFormatException e) {
			throw new SRCPWrongValueException();
		}
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		sendReply(session, addr, value);
		sendInfo(addr, value);
	}

	/** SRCP syntax: RESET <bus> FB */
	public void reset(TokenizedLine tokens,Session session) throws SRCPException {
		handleWriteAccess(session);
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage());
		sendInfo(1);
	}
	
	public boolean isOptional() {
		return true;
	}
	
	private void sendReply(Session session, int addr, int value) {
		session.send(new SRCPMessage(Globals.INFO_MIN_CODE, bus.getId() + " FB " + addr + " " + value));
	}
	
	private void sendInfo(int addr, int value) {
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(Globals.INFO_MIN_CODE, bus.getId() + " FB " + addr + " " + value));
	}
	
	private void sendInfo(int offset) {
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(Globals.INFO_MIN_CODE + offset, bus.getId() + " FB"));
	}
}

/*
 * $RCSfile: SampleGL.java,v $
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
import de.dermoba.srcp.common.exception.SRCPListToShortException;
import de.dermoba.srcp.common.exception.SRCPWrongValueException;
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.SRCPDaemon;
import de.dermoba.srcp.daemon.Session;

class SampleGL extends SampleDeviceGroup
{
	SampleGL (Bus pbus) {
		super(pbus);
		identifier = DEV_GL;
	}

	/** SRCP syntax: INIT <bus> GL <addr> <protocol> [<parameter>.. ] */
	public void init(TokenizedLine tokens,Session session) throws SRCPException {
		handleInitAccess(session);
		String protocol = null;
		int addr = -1;
		try {
			addr = tokens.nextIntToken();
			protocol = tokens.nextStringToken();
		}
		catch (Exception e) {
		 	throw new SRCPListToShortException();
		}
		init (session, addr, protocol);
	}

	/** SRCP syntax SET <bus> GL <addr> <drivemode> <V> <V_max> <f1> .. <fn> */
	public void set(TokenizedLine tokens,Session session, boolean check) throws SRCPException {
		handleReadAccess();
		int addr = -1;
		String drivemode = null;
		int v = -1;
		int vmax = -1;
		GLFunctions fun = new GLFunctions();
		try {
			addr = tokens.nextIntToken();
			drivemode = tokens.nextStringToken();
			v = tokens.nextIntToken();
			vmax = tokens.nextIntToken();
		}
		catch (Exception e) {
		 	throw new SRCPListToShortException();
		}
		if (v < 0 || v > vmax) {
			throw new SRCPWrongValueException();
		}
		session.send(new SRCPMessage());
		sendInfo(addr, drivemode, v, vmax, fun);
	}

	/** SRCP syntax: GET <bus> GL <addr> */
	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		handleReadAccess();
		int addr = -1;
		String drivemode = "=";
		int v = 0;
		int vmax = 1;
		GLFunctions fun = new GLFunctions();
		try {
			addr=tokens.nextIntToken();
		}
		catch (Exception e) {
		 	throw new SRCPListToShortException();
		}
		sendReply(session, addr, drivemode, v, vmax, fun);
	}

	/** SRCP syntax: TERM <bus> GL <addr> */
	public void term(TokenizedLine tokens,Session session) throws SRCPException {
		handleTermAccess();
		int addr = -1;
		try {
			addr=tokens.nextIntToken();
		}
		catch (Exception e) {
		 	throw new SRCPListToShortException();
		}
		session.send(new SRCPMessage());
		sendInfo(addr);
	}

	/** SRCP syntax: RESET <bus> GL <addr> <protocol> [<parameter>.. ] */
	public void reset(TokenizedLine tokens,Session session) throws SRCPException {
		handleWriteAccess(session);
		String protocol = null;
		int addr = -1;
		try {
			addr = tokens.nextIntToken();
			protocol = tokens.nextStringToken();
		}
		catch (Exception e) {
		 	throw new SRCPListToShortException();
		}
		init(session, addr, protocol);
	}

	private void sendReply(Session session, int addr, String drivemode, int v, int vmax, GLFunctions fun) {
		session.send(new SRCPMessage(Globals.INFO_MIN_CODE, bus.getId() + " GL " + addr + " " + drivemode + " " + v + " " + vmax + fun));
	}
	
	private void sendInfo(int addr, String drivemode, int v, int vmax, GLFunctions fun) {
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(Globals.INFO_MIN_CODE, bus.getId() + " GL " + addr + " " + drivemode + " " + v + " " + vmax + fun));
	}
	
	private void sendInfo(int addr, String protocol) {
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(Globals.INFO_MIN_CODE + 1, bus.getId() + " GL " + addr + " " + protocol));
	}
	
	private void sendInfo(int addr) {
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(Globals.INFO_MIN_CODE + 2, bus.getId() + " GL " + addr));
	}

	private void init(Session session, int addr, String protocol) throws SRCPException {
		if ( protocol.equals(PROTOCOL_ANALOG)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_FLEISCHMANN)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_LOCONET)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_MOTOROLA)) {
			if ( (addr < 1) || (addr>256)) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_DCC)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_BY_SERVER)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_SELECTRIX)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		if ( protocol.equals(PROTOCOL_ZIMO)) {
			if (addr != 0) {
				throw new SRCPWrongValueException();
			}
			session.send(new SRCPMessage());
			sendInfo(addr, protocol);
			return;
		}
		throw new SRCPWrongValueException();
	}

	public boolean isOptional() {
		return true;
	}
}

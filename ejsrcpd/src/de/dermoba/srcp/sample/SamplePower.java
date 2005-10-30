/*
 * $RCSfile: SamplePower.java,v $
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
import de.dermoba.srcp.daemon.Bus;
import de.dermoba.srcp.daemon.SRCPDaemon;
import de.dermoba.srcp.daemon.Session;

class SamplePower extends SampleDeviceGroup
{
	SamplePower (Bus pbus) {
		super(pbus);
		identifier = DEV_POWER;
	}

	/** SRCP syntax: INIT <bus> POWER [<parameter>.. ] */
	public void init(TokenizedLine tokens,Session session) throws SRCPException {
		handleInitAccess(session);
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage());
	}

	/** SRCP syntax SET <bus> POWER ON/OFF */
	public void set(TokenizedLine tokens,Session session,boolean check) throws SRCPException {
		handleWriteAccess(session);
		String value = tokens.nextStringToken();
		session.send(new SRCPMessage());
		SRCPDaemon.getInfoDistributor().send(new SRCPMessage(Globals.INFO_MIN_CODE,Integer.toString(bus.getId())+" POWER " + value));
	}

	/** SRCP syntax: GET <bus> POWER <addr> */
	public void get(TokenizedLine tokens,Session session) throws SRCPException {
		handleReadAccess();
		int adresse= -1;
		int value = 0;
		if (!tokens.hasMoreElements()) {
			adresse = tokens.nextIntToken();
		}
		adresse = tokens.nextIntToken();
		session.send(new SRCPMessage(Globals.INFO_MIN_CODE,Integer.toString(bus.getId())+" POWER "+ adresse + " " + value));
	}
	
	/** SRCP syntax: TERM <bus> POWER */
	public void term(TokenizedLine tokens,Session session) throws SRCPException {
		handleTermAccess();
		if (tokens.hasMoreElements()) {
			throw new SRCPListToLongException();
		}
		session.send(new SRCPMessage());
	}
}

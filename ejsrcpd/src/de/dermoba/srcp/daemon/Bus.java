/*
 * $RCSfile: Bus.java,v $
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

 Revision 1.1.1.1  2002/01/08 18:21:42  osc3
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

/** interface for all SRCP bus objects */
public interface Bus
{
	// list of verbs
	public void set(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void check(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void init(TokenizedLine objStrTok,Session objThread ) throws SRCPException;
	public void term(TokenizedLine objStrTok,Session objThread )throws SRCPException;
	public void get(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void wait(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void reset(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void verify(TokenizedLine objStrTok,Session objThread) throws SRCPException;

	public void description(TokenizedLine tokens, Session session) throws SRCPException;

	// setters
	public void setID(int id);
	
	// getters
	public int getDeviceGroupCount();
	public DeviceGroup getDeviceGroupByName(String strIdentifier) throws SRCPException;
	public DeviceGroup getDeviceGroup(int intIndex);
	public int getId ();
}


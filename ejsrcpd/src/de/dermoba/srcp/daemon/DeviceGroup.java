/*
 * $RCSfile: DeviceGroup.java,v $
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

 Revision 1.1.1.1  2002/01/08 18:21:46  osc3
 import of jsrcpd


*/

package de.dermoba.srcp.daemon;

import de.dermoba.srcp.common.TokenizedLine;
import de.dermoba.srcp.common.exception.SRCPException;

/**
 *
 * @author  osc, Kurt Harders
 * @version $Revision: 1.1 $
 */


public interface DeviceGroup
{
    public static final String DEV_GL = "GL";
    public static final String DEV_GA = "GA";
    public static final String DEV_FB = "FB";
    public static final String DEV_SM = "SM";
    public static final String DEV_TIME = "TIME";
    public static final String DEV_POWER = "POWER";
    public static final String DEV_SERVER = "SERVER";
    public static final String DEV_SESSION = "SESSION";
    public static final String DEV_LOCK = "LOCK";
    public static final String DEV_DESCRIPTION = "DESCRIPTION";

	public static final String PROTOCOL_ANALOG = "A";
	public static final String PROTOCOL_FLEISCHMANN = "F";
	public static final String PROTOCOL_LOCONET = "L";
	public static final String PROTOCOL_MOTOROLA = "M";
	public static final String PROTOCOL_DCC = "N";
	public static final String PROTOCOL_BY_SERVER = "P";
	public static final String PROTOCOL_SELECTRIX = "S";
	public static final String PROTOCOL_ZIMO = "Z";

	// list of verbs
	public void set(TokenizedLine objStrTok,Session objThread,boolean blnCheck) throws SRCPException;
	public void init(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void term(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void get(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void wait(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void reset(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void verify(TokenizedLine objStrTok,Session objThread) throws SRCPException;
	public void description(TokenizedLine tokens, Session session) throws SRCPException;

	public Bus getBus();
	public String getIdentifier();
	public boolean isOptional();

}

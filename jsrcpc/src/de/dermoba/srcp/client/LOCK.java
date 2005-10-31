/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.client;

public class LOCK {
    private Session session;
    private int bus;

    public LOCK(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax GET <bus> LOCK <devicegroup> <addr> */
    public String get(String pDevicegroup, int pAddr) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " LOCK " 
            + " " + pDevicegroup + " " + pAddr);
    }

    /** SRCP syntax: SET <bus> LOCK ON|OFF [<freetext>]*/
    public void set(String pDevicegroup, int pAddr, int pDuration) 
        throws SRCPException {

        session.getCommandChannel().send("SET " + bus + " LOCK " + pDevicegroup 
            + " " + pAddr + " " + pDuration);
    }

    /** SRCP syntax: TERM <bus> LOCK */
    public void term(String pDevicegroup, int pAddr) throws SRCPException {
        session.getCommandChannel().send("SET " + bus + " LOCK " + pDevicegroup 
            + " " + pAddr);
    }
}

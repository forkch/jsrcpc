/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.client;

public class DESCRIPTION {
    private Session session;
    private int bus = 0;

    public DESCRIPTION(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: GET <bus> DESCRIPTION */
    public String get(int pBus) throws SRCPException {
        bus = pBus;
        return session.getCommandChannel().send("GET " + bus + " DESCRIPTION ");
    }

    /** SRCP syntax: GET <bus> DESCRIPTION <devicegroup> [<address>]*/
    public String get(int pBus, String pDevicegroup) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " DESCRIPTION "
            + pDevicegroup);
    }

    /** SRCP syntax: GET <bus> DESCRIPTION <devicegroup> [<address>]*/
    public String get(int pBus, String pDevicegroup, int pAddress) 
        throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " DESCRIPTION "
            + pDevicegroup + " " + pAddress);
    }
}

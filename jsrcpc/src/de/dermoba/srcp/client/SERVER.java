/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.client;

public class SERVER {
    private Session session;
    private int bus = 0;

    public SERVER(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: RESET <bus> SERVER */
    public void reset() throws SRCPException {
        session.getCommandChannel().send("RESET " + bus + " SERVER ");
    }

    /** SRCP syntax: TERM <bus> SERVER */
    public void term() throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " SERVER ");
    }
}

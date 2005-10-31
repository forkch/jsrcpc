/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.client;

public class SESSION {
    private Session session;
    private int bus = 0;

    public SESSION(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: GET <bus> SESSION */
    public String get(int pSessionID) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " SESSION " 
          + pSessionID);
    }

    /** SRCP syntax: TERM <bus> SESSION */
    public void term() throws SRCPException {
        //TODO: get sessionID from server
    }

    /** SRCP syntax: TERM <bus> SESSION [<sessionid>] */
    public void term(int pSessionID) throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " SESSION " 
          + pSessionID);
    }
}

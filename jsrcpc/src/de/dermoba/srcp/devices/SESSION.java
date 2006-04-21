/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.devices;

import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPException;

public class SESSION {
    private SRCPSession session;
    private int bus = 0;

    public SESSION(SRCPSession pSession) {
        session = pSession;
    }

    /** SRCP syntax: GET <bus> SESSION */
    public String get(int pSessionID) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " SESSION " 
          + pSessionID);
    }

    /** SRCP syntax: TERM <bus> SESSION */
    public String term() {
        //TODO: get sessionID from server
        return "";
    }

    /** SRCP syntax: TERM <bus> SESSION [<sessionid>] */
    public String term(int pSessionID) throws SRCPException {
        return session.getCommandChannel().send("TERM " + bus + " SESSION " 
          + pSessionID);
    }
}

/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.devices;

import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPException;

public class POWER {
    private SRCPSession session;
    private int bus;

    public POWER(SRCPSession pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT &lt;bus&gt; POWER */
    public String init(int pBus) {
        bus = pBus;
        /*
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("INIT " + bus + " POWER ");
        }
        */
        return "";
    }

    /** SRCP syntax GET &lt;bus&gt; POWER &lt;addr&gt; */
    public String get() throws SRCPException {
        if(session.isOldProtocol()) {
            return session.getCommandChannel().send("GET POWER ");
        }
        return session.getCommandChannel().send("GET " + bus + " POWER ");
    }

    /** SRCP syntax: SET &lt;bus&gt; POWER ON|OFF [&lt;freetext&gt;]*/
    public String set(boolean on) throws SRCPException {
        return set(on, "");
    }

    /** SRCP syntax: SET &lt;bus&gt; POWER ON|OFF [&lt;freetext&gt;]*/
    public String set(boolean on, String freetext) throws SRCPException {
        String power = "";
        if(on) {
            power = "ON";
        } else {
            power = "OFF";
        }
        if(session.isOldProtocol()) {
            return session.getCommandChannel().send("SET POWER " + power);
        }
        return session.getCommandChannel().send("SET " + bus + " POWER " + power 
                + " " + freetext);
    }

    /** SRCP syntax: TERM &lt;bus&gt; POWER */
    public String term() throws SRCPException {
        if(session.isOldProtocol()) {
            return "";
        }
        return session.getCommandChannel().send("TERM " + bus + " POWER ");
    }
}

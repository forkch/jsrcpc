/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.client;

public class POWER {
    private Session session;
    private int bus;

    public POWER(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> POWER */
    public void init(int pBus) throws SRCPException {
        bus = pBus;
        session.getCommandChannel().send("INIT " + bus + " POWER ");
    }

    /** SRCP syntax GET <bus> POWER <addr> */
    public String get() throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " POWER ");
    }

    /** SRCP syntax: SET <bus> POWER ON|OFF [<freetext>]*/
    public void set(boolean on) throws SRCPException {
        set(on, "");
    }

    /** SRCP syntax: SET <bus> POWER ON|OFF [<freetext>]*/
    public void set(boolean on, String freetext) throws SRCPException {
        System.out.println("hrer");
        String power = "";
        if(on) {
          power = "ON";
        } else {
          power = "OFF";
        }
        session.getCommandChannel().send("SET " + bus + " POWER " + power 
            + " " + freetext);
    }

    /** SRCP syntax: TERM <bus> POWER */
    public void term() throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " POWER ");
    }
}

/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

public class FB {

    private Session session;
    private int bus;

    public FB(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> FB <addr> <device protocol> [<parameter>.. ] */
    public void init(int pBus) throws SRCPException {
        bus = pBus;
        session.getCommandChannel().send("INIT " + bus + " FB ");
    }

    /** SRCP syntax GET <bus> FB <addr> */
    public String get(int address) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " FB " 
            + address);
    }

    /** SRCP syntax: TERM <bus> FB */
    public void term() throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " FB ");
    }

    /** SRCP syntax: WAIT <bus> FB <addr> <value> <timeout>*/
    public void term(int address, int value, int timeout) throws SRCPException {
        session.getCommandChannel().send("WAIT " + bus + " FB " + address 
            + " " + value + " " + timeout);
    }

}

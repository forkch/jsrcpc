/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

public class GA {

    private Session session = null;
    private int bus;
    private int address;
    private String protocol;

    public GA(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> GA <addr> <device protocol> [<parameter>.. ] */
    public void init(int pBus, int pAddress, String pProtocol) throws SRCPException {
        bus = pBus;
        address = pAddress;
        protocol = pProtocol;
        session.getCommandChannel().send("INIT " + bus + " GA " 
            + address + " " + protocol);
    }

    /** SRCP syntax SET <bus> GA <addr> <port> <value> <delay> */
    public void set(int port, int value, int delay) throws SRCPException {
        session.getCommandChannel().send("SET " + bus + " GA " + address + " " 
            + port + " " + value + " " + delay);
    }

    /** SRCP syntax GET <bus> GA <addr> <port> */
    public String get(int port) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " GA " + address 
            + " " + port);
    }

    /** SRCP syntax: TERM <bus> GA <addr> */
    public void term() throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " GA " + address);
    }
}

/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import de.dermoba.srcp.common.exception.SRCPException;

public class GA {

    private Session session = null;
    private int bus;
    private int address;
    private String protocol;
    private String[] parameters;

    public GA(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> GA <addr> <device protocol> [<parameter>.. ] */
    public String init(int pBus, int pAddress, String pProtocol) throws SRCPException {
        return init(pBus, pAddress, pProtocol, new String[0]);
    }

    /** SRCP syntax: INIT <bus> GA <addr> <device protocol> [<parameter>.. ] */
    public String init(int pBus, int pAddress, String pProtocol, String[] pParameters) throws SRCPException {
        bus = pBus;
        address = pAddress;
        protocol = pProtocol;
        parameters = pParameters;
        StringBuffer paramBuf = new StringBuffer();
        for(int i = 0; i < parameters.length; i++) {
            paramBuf.append(parameters[i]);
            paramBuf.append(" ");
        }
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("INIT " + bus + " GA " 
                + address + " " + protocol + " " + paramBuf.toString());
        } 
        return "";
    }

    /** SRCP syntax SET <bus> GA <addr> <port> <value> <delay> */
    public String set(int port, int value, int delay) throws SRCPException {
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send(
                "SET " + bus + " GA " + address + " " 
                + port + " " + value + " " + delay);
        } else {
            return session.getCommandChannel().send(
                "SET  GA " + protocol + " " + address + " " 
                + port + " " + value + " " + delay);
        }
    }

    /** SRCP syntax GET <bus> GA <addr> <port> */
    public String get(int port) throws SRCPException {
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("GET " + bus + " GA " + address 
                + " " + port);
        } else {
            return session.getCommandChannel().send("GET GA " + protocol + " " 
                + address + " " + port);
        }
    }

    /** SRCP syntax: TERM <bus> GA <addr> */
    public String term() throws SRCPException {
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("TERM " + bus + " GA " + address);
        }
        return "";
    }
}

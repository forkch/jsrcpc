/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;


public class GL {

    private Session session = null;
    private int bus = 0;
    private int address = 0;
    private String protocol = null;
    private String[] parameters = null;

    public GL(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> GL <addr> <protocol> [<parameter>.. ] */
    public void init(int pBus, int pAddress, String pProtocol) throws SRCPException {
        init(pBus, pAddress, pProtocol, new String[0]);
    }

    /** SRCP syntax: INIT <bus> GL <addr> <protocol> [<parameter>.. ] */
    public void init(int pBus, int pAddress, String pProtocol, String[] pParameters) throws SRCPException {
        bus = pBus;
        address = pAddress;
        protocol = pProtocol;
        parameters = pParameters;
        StringBuffer paramBuf = new StringBuffer();
        for(int i = 0; i < parameters.length; i++) {
            paramBuf.append(parameters[i]);
            paramBuf.append(" ");
        }
        session.getCommandChannel().send("INIT " + bus + " GL " + address 
            + " " + protocol + " " + paramBuf.toString());
    }

    /** SRCP syntax SET <bus> GL <addr> <drivemode> <V> <V_max> <f1> .. <fn> */
    public void set(String drivemode, int v, int vmax, boolean[]f) throws SRCPException {
        if (v < 0 || v > vmax) {
            return;
        }
        session.getCommandChannel().send("SET " + bus + " GL " + address + " " + drivemode + " " + v + " " + vmax);
    }

    /** SRCP syntax GET <bus> GL <addr> */
    public String get() throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " GL " 
            + address); 
    }

    /** SRCP syntax: TERM <bus> GL <addr> */
    public void term() throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " GL " + address);
    }
}

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

    public GL(Session pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> GL <addr> <protocol> [<parameter>.. ] */
    public void init(int pBus, int pAddress, String pProtocol) throws SRCPException {
        bus = pBus;
        address = pAddress;
        protocol = pProtocol;
        session.getCommandChannel().send("INIT " + bus + " GL " + address 
            + " " + protocol);
    }

    /** SRCP syntax SET <bus> GL <addr> <drivemode> <V> <V_max> <f1> .. <fn> */
    public void set(String drivemode, int v, int vmax, boolean[]f) throws SRCPException {
        if (v < 0 || v > vmax) {
            return;
        }
        session.getCommandChannel().send("SET " + bus + " GL " + address + " " + drivemode + " " + v + " " + vmax);
    }

    /** SRCP syntax: TERM <bus> GL <addr> */
    public void term() throws SRCPException {
        session.getCommandChannel().send("TERM " + bus + " GL " + address);
    }
}

/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.devices;

import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPException;

public class FB {

    private SRCPSession session;
    private int bus;

    public FB(SRCPSession pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> FB <addr> <device protocol> [<parameter>.. ] */
    public String init(int pBus) throws SRCPException {
        bus = pBus;
        return session.getCommandChannel().send("INIT " + bus + " FB ");
    }

    /** SRCP syntax GET <bus> FB <addr> */
    public String get(int address) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " FB " 
            + address);
    }

    /** SRCP syntax: SET <bus> FB <addr> <value> */
    public String set(int address, int value) throws SRCPException {
        return session.getCommandChannel().send("SET " + bus + " FB " + address 
            + " " + value);
    }

    /** SRCP syntax: TERM <bus> FB */
    public String term() throws SRCPException {
        return session.getCommandChannel().send("TERM " + bus + " FB ");
    }

    /** SRCP syntax: WAIT <bus> FB <addr> <value> <timeout>*/
    public String wait(int address, int value, int timeout) throws SRCPException {
        return session.getCommandChannel().send("WAIT " + bus + " FB " + address 
            + " " + value + " " + timeout);
    }

}

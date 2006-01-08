/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

import de.dermoba.srcp.common.exception.SRCPException;

public class GL {

    private SRCPSession session = null;
    private int bus = 0;
    private int address = 0;
    private String protocol = null;
    private String[] parameters = null;

    public GL(SRCPSession pSession) {
        session = pSession;
    }

    /** SRCP syntax: INIT <bus> GL <addr> <protocol> [<parameter>.. ] */
    public String init(int pBus, int pAddress, String pProtocol) throws SRCPException {
        return init(pBus, pAddress, pProtocol, new String[0]);
    }

    /** SRCP syntax: INIT <bus> GL <addr> <protocol> [<parameter>.. ] */
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
            return session.getCommandChannel().send("INIT " + bus + " GL " 
                + address + " " + protocol + " " + paramBuf.toString());
        }
        return "";
    }

    /** SRCP syntax SET <bus> GL <addr> <drivemode> <V> <V_max> <f1> .. <fn> */
    public String set(String drivemode, int v, int vmax, boolean[]f) throws SRCPException {
        //if (v < 0 || v > vmax) {
            //return;
        //}
        StringBuffer functionBuf = new StringBuffer();
        for(int i = 0; i < f.length; i++) {
            if(f[i]) {
                functionBuf.append("1 ");
            } else {
                functionBuf.append("0 ");
            }
        }

        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("SET " + bus + " GL " 
                + address + " " + drivemode + " " + v + " " + vmax + " " 
                + functionBuf);
        } else {
            return session.getCommandChannel().send("SET GL " + protocol + " " 
                + address + " " + drivemode + " " + v + " " + vmax + " " 
                + functionBuf);
        }
    }


    /** SRCP syntax GET <bus> GL <addr> */
    public String get() throws SRCPException {
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("GET " + bus + " GL " 
                + address); 
        } else {
            return session.getCommandChannel().send("GET GL " 
                + address); 
        }
    }

    /** SRCP syntax: TERM <bus> GL <addr> */
    public String term() throws SRCPException {
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("TERM " + bus 
                + " GL " + address);
        }
        return "";
    }
}

/*
 * Created on 31.10.2005
 *
 */
package de.dermoba.srcp.devices;

import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPException;

public class DESCRIPTION {
    private SRCPSession session;
    private int bus = 0;

    public DESCRIPTION(SRCPSession pSession) {
        session = pSession;
    }

    /** SRCP syntax: GET &lt;bus&gt; DESCRIPTION */
    public String get(int pBus) throws SRCPException {
        bus = pBus;
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send(
                "GET " + bus + " DESCRIPTION ");
        }
        return "";
    }

    /** SRCP syntax: GET &lt;bus&gt; DESCRIPTION &lt;devicegroup&gt; [&lt;address&gt;]*/
    public String get(int pBus, String pDevicegroup) throws SRCPException {
        if(!session.isOldProtocol()) {
            return session.getCommandChannel().send("GET " + bus + " DESCRIPTION "
                + pDevicegroup);
        }
        return "";
    }

    /** SRCP syntax: GET &lt;bus&gt; DESCRIPTION &lt;devicegroup&gt; [&lt;address&gt;]*/
    public String get(int pBus, String pDevicegroup, int pAddress) 
        throws SRCPException {
            if(!session.isOldProtocol()) {
                return session.getCommandChannel().send("GET " + bus 
                    + " DESCRIPTION " + pDevicegroup + " " + pAddress);
            }
        return "";
        }
}

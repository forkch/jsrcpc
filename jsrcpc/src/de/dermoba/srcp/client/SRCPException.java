/*
 * Created on 26.09.2005
 *
 */
package de.dermoba.srcp.client;

public class SRCPException extends Exception {

    /**
     * creates a new SRCPException object to indicate problems in SRCP communication.
     * 
     * @param reason
     */
    public SRCPException(String reason) {
        super(reason);
    }
}

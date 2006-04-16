/*
 * Created on 03.07.2005
 *
 */
package de.dermoba.srcp.common.exception;

public class SRCPInternalErrorException extends SRCPException {

    public final static int NUMBER = 601;

	public SRCPInternalErrorException() {
        super(NUMBER,"Internal server error");
	}

	public SRCPInternalErrorException(Throwable cause) {
        super(NUMBER,"Internal server error", cause);
	}
	
	public SRCPException cloneExc() {
    	return new SRCPInternalErrorException();
	}

}

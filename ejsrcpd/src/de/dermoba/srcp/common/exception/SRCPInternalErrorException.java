/*
 * Created on 03.07.2005
 *
 */
package de.dermoba.srcp.common.exception;

public class SRCPInternalErrorException extends SRCPException {

	public SRCPInternalErrorException() {
        super(601,"Internal server error");
	}
	
	public SRCPException cloneExc() {
    	return new SRCPInternalErrorException();
	}

}
